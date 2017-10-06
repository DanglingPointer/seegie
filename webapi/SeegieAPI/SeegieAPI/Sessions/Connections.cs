///
///   Copyright 2017 Mikhail Vasilyev
///
///   Licensed under the Apache License, Version 2.0 (the "License");
///   you may not use this file except in compliance with the License.
///   You may obtain a copy of the License at
///
///       http://www.apache.org/licenses/LICENSE-2.0
///
///   Unless required by applicable law or agreed to in writing, software
///   distributed under the License is distributed on an "AS IS" BASIS,
///   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
///   See the License for the specific language governing permissions and
///   limitations under the License.
///
using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Net;
using System.Net.WebSockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace SeegieAPI.Sessions
{
    public interface IConnectionManager
    {
        Task ListenAsync();
        Task CloseConnectionAsync();
    }
    public class ClientManager : IConnectionManager
    {
        private readonly WebSocket _socket;

        public event Action<string> DataReceived;
        public event Action<WebSocketReceiveResult> ConnectionClosed;
        public ClientManager(WebSocket socket, Guid sessionId, bool isSeed)
        {
            _socket = socket;
            SessionId = sessionId;
            IsSeed = isSeed;
            BufferSize = 1024 * 4;
        }
        public Guid SessionId { get; }
        public bool IsSeed { get; }
        public uint BufferSize { get; set; }
        public async Task ListenAsync()
        {
            //string who = (IsSeed ? "Seed" : "Leech");
            //Debug.WriteLine(who + " started listening");

            byte[] buffer = new byte[BufferSize];
            while (_socket.State == WebSocketState.Open) {
                WebSocketReceiveResult result =
                    await _socket.ReceiveAsync(new ArraySegment<byte>(buffer), CancellationToken.None);
                
                //Debug.WriteLine(who + " received a message");
                if (result.MessageType == WebSocketMessageType.Text) {
                    string message = Encoding.UTF8.GetString(buffer, 0, result.Count);
                    DataReceived?.Invoke(message);
                }
                else if (result.MessageType == WebSocketMessageType.Close) {
                    //Debug.WriteLine(who + " received a closing message");
                    ConnectionClosed?.Invoke(result);
                }
            }
        }
        public async Task CloseConnectionAsync()
        {
            if (_socket.State != WebSocketState.Closed)
                await _socket.CloseAsync(WebSocketCloseStatus.EndpointUnavailable, "No data available", CancellationToken.None);

            //string who = (IsSeed ? "Seed" : "Leech");
            //Debug.WriteLine(who + " closed connection");
        }
        public async Task SendMessageAsync(String text)
        {
            if (_socket.State != WebSocketState.Open)
                return;

            //string who = (IsSeed ? "Seed" : "Leech");
            //Debug.WriteLine(who + " is about to send a message");

            byte[] arr = Encoding.UTF8.GetBytes(text);
            var buffer = new ArraySegment<byte>(
                    array: arr,
                    offset: 0,
                    count: arr.Length
                    );

            await _socket.SendAsync(
                buffer: buffer,
                messageType: WebSocketMessageType.Text,
                endOfMessage: true,
                cancellationToken: CancellationToken.None
                );            
            //Debug.WriteLine(who + " has sent a message");
        }
    }
    public class SessionManager
    {
        private readonly IDictionary<Guid, ClientManager> _seeds;
        private readonly IDictionary<Guid, ICollection<ClientManager>> _leeches;
        private Action<Guid> _freeGuid;

        public SessionManager(Action<Guid> freeGuid)
        {
            _seeds = new ConcurrentDictionary<Guid, ClientManager>();
            _leeches = new ConcurrentDictionary<Guid, ICollection<ClientManager>>();
            _freeGuid = freeGuid;
        }
        public IConnectionManager AddSeed(Guid id, WebSocket sock)
        {
            var seed = new ClientManager(sock, id, true);
            seed.ConnectionClosed += async (result) => {
                await CloseSessionAsync(id);
            };
            _seeds.Add(id, seed);
            return seed;
        }
        public IConnectionManager AddLeech(Guid id, WebSocket sock)
        {
            if (!_seeds.ContainsKey(id)) {
                throw new InvalidOperationException("Invalid session id");
            }
            if (!_leeches.ContainsKey(id)) {
                _leeches.Add(id, new List<ClientManager>());
            }
            var newLeech = new ClientManager(sock, id, false);
            _leeches[id].Add(newLeech);

            var seed = _seeds[id];

            Action<string> onEegDataReceivedHandler = async (eegData) => {
                await newLeech.SendMessageAsync(eegData);
            };
            seed.DataReceived += onEegDataReceivedHandler;

            newLeech.ConnectionClosed += (result) => {
                // unsubscribe and remove dead leeches
                seed.DataReceived -= onEegDataReceivedHandler;
                if (_leeches.ContainsKey(id))
                    _leeches[id].Remove(newLeech);
            };
            newLeech.DataReceived += async (bciCmd) => {
                await seed.SendMessageAsync(bciCmd);
            };
            return newLeech;
        }
        public async Task CloseSessionAsync(Guid id)
        {
            if (_seeds.ContainsKey(id)) {
                var seed = _seeds[id];
                _seeds.Remove(id);
                await seed.CloseConnectionAsync();
            }
            if (_leeches.ContainsKey(id)) {
                var list = _leeches[id];
                _leeches.Remove(id);
                foreach(var leech in list)
                    await leech.CloseConnectionAsync();                
            }
            _freeGuid(id);
        }
    }

}

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
    public class ClientManager : IConnectionManager, IDisposable
    {
        private readonly WebSocket _socket;

        public event Action<string> DataReceived;
        public event Action<WebSocketReceiveResult> ConnectionClosed;
        public ClientManager(WebSocket socket, int sessionId, bool isSeed)
        {
            _socket = socket;
            SessionId = sessionId;
            IsSeed = isSeed;
            BufferSize = 1024 * 4;
        }
        public int SessionId { get; }
        public bool IsSeed { get; }
        public uint BufferSize { get; set; }
        public async Task ListenAsync()
        {
            try {
                byte[] buffer = new byte[BufferSize];
                while (_socket.State == WebSocketState.Open) {
                    WebSocketReceiveResult result =
                        await _socket.ReceiveAsync(new ArraySegment<byte>(buffer), CancellationToken.None);

                    if (result.MessageType == WebSocketMessageType.Text) {
                        string message = Encoding.UTF8.GetString(buffer, 0, result.Count);
                        DataReceived?.Invoke(message);
                    }
                    else if (result.MessageType == WebSocketMessageType.Close) {
                        ConnectionClosed?.Invoke(result);
                    }
                }
            }
            catch (Exception) {
                _socket.Abort();
                ConnectionClosed?.Invoke(null);
            }
        }
        public async Task CloseConnectionAsync()
        {
            if (_socket.State != WebSocketState.Closed)
                await _socket.CloseAsync(WebSocketCloseStatus.EndpointUnavailable, "No data available", CancellationToken.None);
        }
        public async Task SendMessageAsync(String text)
        {
            if (_socket.State != WebSocketState.Open)
                return;

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
        }
        public void Dispose()
        {
            if (_socket.State != WebSocketState.Aborted && _socket.State != WebSocketState.Closed)
                _socket.Abort();
            _socket.Dispose();
        }
    }
    public class SessionManager
    {
        private readonly IDictionary<int, ClientManager> _seeds;
        private readonly IDictionary<int, ICollection<ClientManager>> _leeches;
        private Action<int> _freeGuid;

        public SessionManager(Action<int> freeGuid)
        {
            _seeds = new ConcurrentDictionary<int, ClientManager>();
            _leeches = new ConcurrentDictionary<int, ICollection<ClientManager>>();
            _freeGuid = freeGuid;
        }
        public IConnectionManager AddSeed(int id, WebSocket sock)
        {
            var seed = new ClientManager(sock, id, true);
            seed.ConnectionClosed += async (result) => {
                await CloseSessionAsync(id);
            };
            _seeds.Add(id, seed);
            return seed;
        }
        public IConnectionManager AddLeech(int id, WebSocket sock)
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
                try {
                    await newLeech.SendMessageAsync(eegData);
                }
                catch (Exception) {
                    await newLeech.CloseConnectionAsync();
                }
            };
            seed.DataReceived += onEegDataReceivedHandler;

            newLeech.ConnectionClosed += (result) => {
                // unsubscribe and remove dead leeches
                seed.DataReceived -= onEegDataReceivedHandler;
                if (_leeches.ContainsKey(id))
                    _leeches[id].Remove(newLeech);
                newLeech.Dispose();
            };
            newLeech.DataReceived += async (bciCmd) => {
                await seed.SendMessageAsync(bciCmd);
            };
            return newLeech;
        }
        public async Task CloseSessionAsync(int id)
        {
            if (_seeds.ContainsKey(id)) {
                var seed = _seeds[id];
                _seeds.Remove(id);
                await seed.CloseConnectionAsync();
                seed.Dispose();
            }
            if (_leeches.ContainsKey(id)) {
                var list = _leeches[id];
                _leeches.Remove(id);
                foreach (var leech in list) {
                    await leech.CloseConnectionAsync();
                    leech.Dispose();
                }
            }
            _freeGuid(id);
        }
    }

}

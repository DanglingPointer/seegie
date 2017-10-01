/*
 *    Copyright 2017 Mikhail Vasilyev
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package java_websocket;

import java_websocket.drafts.Draft;
import java_websocket.exceptions.InvalidDataException;
import java_websocket.exceptions.InvalidHandshakeException;
import java_websocket.framing.Framedata;
import java_websocket.framing.PingFrame;
import java_websocket.framing.PongFrame;
import java_websocket.handshake.ClientHandshake;
import java_websocket.handshake.HandshakeImpl1Server;
import java_websocket.handshake.ServerHandshake;
import java_websocket.handshake.ServerHandshakeBuilder;

import java.net.InetSocketAddress;

/**
 * This class default implements all methods of the WebSocketListener that can be overridden optionally when advances functionalities is needed.<br>
 **/
public abstract class WebSocketAdapter implements WebSocketListener
{

	/**
	 * This default implementation does not do anything. Go ahead and overwrite it.
	 *
	 * @see WebSocketListener#onWebsocketHandshakeReceivedAsServer(WebSocket, Draft, ClientHandshake)
	 */
	@Override
	public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket conn, Draft draft, ClientHandshake request ) throws InvalidDataException {
		return new HandshakeImpl1Server();
	}

	@Override
	public void onWebsocketHandshakeReceivedAsClient(WebSocket conn, ClientHandshake request, ServerHandshake response ) throws InvalidDataException {
	}

	/**
	 * This default implementation does not do anything which will cause the connections to always progress.
	 *
	 * @see WebSocketListener#onWebsocketHandshakeSentAsClient(WebSocket, ClientHandshake)
	 */
	@Override
	public void onWebsocketHandshakeSentAsClient(WebSocket conn, ClientHandshake request ) throws InvalidDataException {
	}

	/**
	 * This default implementation does not do anything. Go ahead and overwrite it
	 *
	 * @see WebSocketListener#onWebsocketMessageFragment(WebSocket, Framedata)
	 */
	@Override
	public void onWebsocketMessageFragment(WebSocket conn, Framedata frame ) {
	}

	/**
	 * This default implementation will send a pong in response to the received ping.
	 * The pong frame will have the same payload as the ping frame.
	 *
	 * @see WebSocketListener#onWebsocketPing(WebSocket, Framedata)
	 */
	@Override
	public void onWebsocketPing(WebSocket conn, Framedata f ) {
		conn.sendFrame( new PongFrame((PingFrame)f ) );
	}

	/**
	 * This default implementation does not do anything. Go ahead and overwrite it.
	 *
	 * @see WebSocketListener#onWebsocketPong(WebSocket, Framedata)
	 */
	@Override
	public void onWebsocketPong(WebSocket conn, Framedata f ) {
	}

	/**
	 * Gets the XML string that should be returned if a client requests a Flash
	 * security policy.
	 * <p>
	 * The default implementation allows access from all remote domains, but
	 * only on the port that this WebSocketServer is listening on.
	 * <p>
	 * This is specifically implemented for gitime's WebSocket client for Flash:
	 * http://github.com/gimite/web-socket-js
	 *
	 * @return An XML String that comforts to Flash's security policy. You MUST
	 * not include the null char at the end, it is appended automatically.
	 * @throws InvalidDataException thrown when some data that is required to generate the flash-policy like the websocket local port could not be obtained e.g because the websocket is not connected.
	 */
	@Override
	public String getFlashPolicy( WebSocket conn ) throws InvalidDataException {
		InetSocketAddress adr = conn.getLocalSocketAddress();
		if( null == adr ) {
			throw new InvalidHandshakeException("socket not bound" );
		}

		return "<cross-domain-policy><allow-access-from domain=\"*\" to-ports=\"" + adr.getPort() + "\" /></cross-domain-policy>\0";
	}

}

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

import java.util.List;

public interface WebSocketFactory {
	/**
	 * Create a new Websocket with the provided listener, drafts and socket
	 * @param a The Listener for the WebsocketImpl
	 * @param d The draft which should be used
	 * @return A WebsocketImpl
	 */
	WebSocket createWebSocket(WebSocketAdapter a, Draft d);

	/**
	 * Create a new Websocket with the provided listener, drafts and socket
	 * @param a The Listener for the WebsocketImpl
	 * @param drafts The drafts which should be used
	 * @return A WebsocketImpl
	 */
	WebSocket createWebSocket(WebSocketAdapter a, List<Draft> drafts);

}

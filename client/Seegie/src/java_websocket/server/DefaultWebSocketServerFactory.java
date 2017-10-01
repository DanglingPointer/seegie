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

package java_websocket.server;

import java_websocket.WebSocketAdapter;
import java_websocket.WebSocketImpl;
import java_websocket.WebSocketServerFactory;
import java_websocket.drafts.Draft;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;

public class DefaultWebSocketServerFactory implements WebSocketServerFactory
{
	@Override
	public WebSocketImpl createWebSocket(WebSocketAdapter a, Draft d) {
		return new WebSocketImpl(a, d );
	}
	@Override
	public WebSocketImpl createWebSocket(WebSocketAdapter a, List<Draft> d) {
		return new WebSocketImpl(a, d );
	}
	@Override
	public SocketChannel wrapChannel( SocketChannel channel, SelectionKey key ) {
		return channel;
	}
	@Override
	public void close() {
	}
}
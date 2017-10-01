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

import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;

/**
 * Interface to encapsulate the required methods for a websocket factory
 */
public interface WebSocketServerFactory extends WebSocketFactory
{
    @Override
    WebSocketImpl createWebSocket(WebSocketAdapter a, Draft d);

    @Override
    WebSocketImpl createWebSocket(WebSocketAdapter a, List<Draft> drafts);

    /**
     * Allows to wrap the Socketchannel( key.channel() ) to insert a protocol layer( like ssl or proxy authentication) beyond the ws layer.
     *
     * @param channel The SocketChannel to wrap
     * @param key a SelectionKey of an open SocketChannel.
     * @return The channel on which the read and write operations will be performed.<br>
     * @throws IOException may be thrown while writing on the channel
     */
    ByteChannel wrapChannel(SocketChannel channel, SelectionKey key) throws IOException;

    /**
     * Allows to shutdown the websocket factory for a clean shutdown
     */
    void close();
}

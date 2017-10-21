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

package web;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebSocketWrapper extends WebSocketClient
{
    public interface Listener
    {
        void onOpened(ServerHandshake handshake);
        void onMessageReceived(String message);
        void onClosed(int code, String reason, boolean remote);
        void onErrorOccurred(Exception ex);
    }

    private Listener m_listener;

    public WebSocketWrapper(URI serverUri) {
        super(serverUri);
    }
    public void setListener(Listener listener) {
        m_listener = listener;
    }
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        if (m_listener != null)
            m_listener.onOpened(handshakedata);
    }
    @Override
    public void onMessage(String message) {
        if (m_listener != null)
            m_listener.onMessageReceived(message);
    }
    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (m_listener != null)
            m_listener.onClosed(code, reason, remote);
    }
    @Override
    public void onError(Exception ex) {
        if (m_listener != null)
            m_listener.onErrorOccurred(ex);
    }
}

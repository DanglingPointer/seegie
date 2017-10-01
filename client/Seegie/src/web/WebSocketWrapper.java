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

import java_websocket.client.WebSocketClient;
import java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

public class WebSocketWrapper extends WebSocketClient
{
    public interface Listener
    {
        void onOpened(ServerHandshake handshake);
        void onMessageReceived(String message);
        void onClosed(int code, String reason, boolean remote);
        void onErrorOccurred(Exception ex);
    }

    private final Set<Listener> m_listeners = new HashSet<>();

    public WebSocketWrapper(URI serverUri) {
        super(serverUri);
    }
    public void addListener(Listener listener) {
        m_listeners.add(listener);
    }
    public void removeListener(Listener listener) {
        m_listeners.remove(listener);
    }
    public void removeAllListeners() {
        m_listeners.clear();
    }
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        for (Listener l : m_listeners)
            l.onOpened(handshakedata);
    }
    @Override
    public void onMessage(String message) {
        for (Listener l : m_listeners)
            l.onMessageReceived(message);
    }
    @Override
    public void onClose(int code, String reason, boolean remote) {
        for (Listener l : m_listeners)
            l.onClosed(code, reason, remote);
    }
    @Override
    public void onError(Exception ex) {
        for (Listener l : m_listeners)
            l.onErrorOccurred(ex);
    }
}

/*
 *     Copyright (C) 2017  Mikhail Vasilyev
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
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

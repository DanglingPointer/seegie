/*
 * MIT License
 *
 * Copyright (c) 2017 Mikhail Vasilyev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package web;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class NetworkManager
{
    private static NetworkManager s_instance = new NetworkManager();
    public static NetworkManager getInstance() {
        return s_instance;
    }

    private final Map<String, WebSocketWrapper> m_sockets;

    private NetworkManager() {
        m_sockets = new HashMap<>();
    }
    public NetworkAdapter getAdapter(String wsLink) {
        WebSocketWrapper socket = null;
        if (m_sockets.containsKey(wsLink)) {
            socket = m_sockets.get(wsLink);
        }
        else {
            try {
                URI link = new URI(wsLink);
                socket = new WebSocketWrapper(link);
                m_sockets.put(wsLink, socket);
            }
            catch (Exception e) {
                throw new IllegalArgumentException(e.toString());
            }
        }
        return new NetworkAdapter(socket);
    }
}

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

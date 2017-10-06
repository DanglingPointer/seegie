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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
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
    public String reserveSessionId(String httpLink) throws IOException {
        URL link = new URL(httpLink);
        String reply = "";
        final int BUFFER_SIZE = 128;

        HttpURLConnection conn = (HttpURLConnection)link.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        int response = conn.getResponseCode();
        if (response != HttpURLConnection.HTTP_OK)
            throw new IOException("HTTP error: " + response);
        InputStream inStream = conn.getInputStream();
        if (inStream != null) {
            InputStreamReader reader = new InputStreamReader(inStream);
            char[] buffer = new char[BUFFER_SIZE];

            int lastRead = 0, offset = 0;
            while (lastRead != -1) {
                lastRead = reader.read(buffer, offset, BUFFER_SIZE - offset);
                offset += lastRead;
            }
            if (offset != -1)
                reply = new String(buffer, 0, offset + 1);
            inStream.close();
        }
        return reply;
    }
}

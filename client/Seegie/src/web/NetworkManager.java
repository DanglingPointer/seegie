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

import core.Settings;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class NetworkManager
{
    private static NetworkManager s_instance = new NetworkManager();
    public static NetworkManager getInstance() {
        return s_instance;
    }

    private final String         m_wsLink;
    private final String         m_httpLink;
    private       NetworkAdapter m_currentAdapter;

    private NetworkManager() {
        m_wsLink = Settings.getWsUrl();
        m_httpLink = Settings.getApiUrl();
        m_currentAdapter = null;
    }
    public NetworkAdapter getAdapter(String id, boolean isSeed) throws URISyntaxException {
        if (m_currentAdapter == null
            || !m_currentAdapter.getId().equals(id)
            || m_currentAdapter.isSeed() != isSeed) {
            String link = m_wsLink + "role=" + (isSeed ? "seed" : "leech") + "&sessionid=" + id;
            WebSocketWrapper socket = new WebSocketWrapper(new URI(link));
            m_currentAdapter = new NetworkAdapter(socket, id, isSeed);
        }
        return m_currentAdapter;
    }
    public String reserveSessionId() throws IOException {
        URL link = new URL(m_httpLink);
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

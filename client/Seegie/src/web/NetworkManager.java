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

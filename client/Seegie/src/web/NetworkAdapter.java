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

import io.CmdInEndpoint;
import io.CmdOutEndpoint;
import io.DataInEndpoint;
import io.DataOutEndpoint;
import org.java_websocket.handshake.ServerHandshake;
import models.BCICommand;
import models.EEGData;
import models.Serializer;

import java.util.HashSet;
import java.util.Set;

public class NetworkAdapter implements DataInEndpoint, DataOutEndpoint, CmdInEndpoint, CmdOutEndpoint
{
    private final WebSocketWrapper             m_socket;
    private final Set<CmdInEndpoint.Listener>  m_cmdListeners;
    private final Set<DataInEndpoint.Listener> m_dataListeners;
    private final String                       m_id;
    private final boolean                      m_seed;

    NetworkAdapter(WebSocketWrapper ws, String id, boolean isSeed) {
        m_id = id;
        m_seed = isSeed;
        m_cmdListeners = new HashSet<>();
        m_dataListeners = new HashSet<>();
        m_socket = ws;
        m_socket.setListener(new WebSocketWrapper.Listener()
        {
            @Override
            public void onOpened(ServerHandshake handshake) {}
            @Override
            public void onMessageReceived(String message) {
                if (message.contains(Serializer.TYPE_INFO)) {
                    for (DataInEndpoint.Listener l : m_dataListeners)
                        l.onInfoReceived(Serializer.json2Info(message));
                }
                else if (message.contains(Serializer.TYPE_DATA)) {
                    for (DataInEndpoint.Listener l : m_dataListeners)
                        l.onDataReceived(Serializer.json2Data(message));
                }
                else if (message.contains(Serializer.TYPE_CMD)) {
                    for (CmdInEndpoint.Listener l : m_cmdListeners)
                        l.onCmdReceived(Serializer.json2Command(message));
                }

            }
            @Override
            public void onClosed(int code, String reason, boolean remote) {}
            @Override
            public void onErrorOccurred(Exception ex) {}
        });
    }
    public String getId() {
        return m_id;
    }
    public boolean isSeed() {
        return m_seed;
    }
    @Override
    public void addListener(CmdInEndpoint.Listener listener) {
        m_cmdListeners.add(listener);
    }
    @Override
    public void sendCmd(BCICommand cmd) {
        String json = Serializer.command2Json(cmd);
        m_socket.send(json);
    }
    @Override
    public void addListener(DataInEndpoint.Listener listener) {
        m_dataListeners.add(listener);
    }
    @Override
    public void sendData(EEGData data) {
        try {
            String json = Serializer.data2Json(data);
            m_socket.send(json);
        }
        catch (Exception e) {
            System.out.println("Exception using websocket");
            e.printStackTrace();
        }
    }
    @Override
    public void sendInfo(String info) {
        try {
            String json = Serializer.info2Json(info);
            m_socket.send(json);
        }
        catch (Exception e) {
            System.out.println("Exception using websocket");
            e.printStackTrace();
        }
    }
    @Override
    public void unregisterListeners() {
        m_cmdListeners.clear();
        m_dataListeners.clear();
    }
    @Override
    public void open() {
        try {
            if (!m_socket.isOpen())
                m_socket.connectBlocking();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void close() {
        if (m_socket.isOpen())
            m_socket.close();
    }
}

/*
 *     Copyright (C) 2017-2018  Mikhail Vasilyev
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
    private final WebSocketWrapper             m_wsSocket;
    private final UdpClient                    m_udpSocket;
    private final Set<CmdInEndpoint.Listener>  m_cmdListeners;
    private final Set<DataInEndpoint.Listener> m_dataListeners;
    private final String                       m_id;
    private final boolean                      m_seed;

    NetworkAdapter(WebSocketWrapper ws, UdpClient udp, String id, boolean isSeed) {
        m_id = id;
        m_seed = isSeed;
        m_cmdListeners = new HashSet<>();
        m_dataListeners = new HashSet<>();
        m_wsSocket = ws;
        m_wsSocket.setListener(new WebSocketWrapper.Listener()
        {
            @Override
            public void onOpened(ServerHandshake handshake) {}
            @Override
            public void onMessageReceived(String message) {
                if (message.contains(Serializer.TYPE_INFO)) {
                    String info = Serializer.json2Info(message);
                    for (DataInEndpoint.Listener l : m_dataListeners)
                        l.onInfoReceived(info);
                }
                else if (message.contains(Serializer.TYPE_CMD)) {
                    BCICommand cmd = Serializer.json2Command(message);
                    for (CmdInEndpoint.Listener l : m_cmdListeners)
                        l.onCmdReceived(cmd);
                }
            }
            @Override
            public void onClosed(int code, String reason, boolean remote) {}
            @Override
            public void onErrorOccurred(Exception e) {
                System.out.println("Exception using websocket");
                e.printStackTrace();
            }
        });
        m_udpSocket = udp;
        m_udpSocket.setListener(new UdpClient.Listener()
        {
            @Override
            public void onReceived(String message) {
                if (message.contains(Serializer.TYPE_DATA)) {
                    EEGData data = Serializer.json2Data(message);
                    for (DataInEndpoint.Listener l : m_dataListeners)
                        l.onDataReceived(data);
                }
            }
            @Override
            public void onStopped() {}
            @Override
            public void onError(Exception e) {
                System.out.println("Exception using udp");
                e.printStackTrace();
            }
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
        try {
            String json = Serializer.command2Json(cmd);
            m_wsSocket.send(json);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void addListener(DataInEndpoint.Listener listener) {
        m_dataListeners.add(listener);
    }
    @Override
    public void sendData(EEGData data) {
        try {
            String json = Serializer.data2Json(m_id, data);
            m_udpSocket.send(json);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void sendInfo(String info) {
        try {
            String json = Serializer.info2Json(info);
            m_wsSocket.send(json);
        }
        catch (Exception e) {
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
            if (!m_seed && !m_udpSocket.isListening()) {
                final String invite = Serializer.invite2Json(m_id, m_udpSocket.getLocalPort());
                m_udpSocket.send(invite); // try to punch a hole?
                m_udpSocket.startListening();
            }
            if (!m_wsSocket.isOpen() && !m_wsSocket.isClosed())
                m_wsSocket.connectBlocking();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void close() {
        m_wsSocket.close();
        m_udpSocket.close();
    }
}

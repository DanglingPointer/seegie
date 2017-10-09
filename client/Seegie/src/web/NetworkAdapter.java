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
        String json = Serializer.data2Json(data);
        m_socket.send(json);
    }
    @Override
    public void sendInfo(String info) {
        String json = Serializer.info2Json(info);
        m_socket.send(json);
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
        catch (Exception e) {}
    }
    @Override
    public void close() {
        if (m_socket.isOpen())
            m_socket.close();
    }
}

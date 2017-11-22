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

package ui;

import core.AppManager;
import core.LeechEndpointFactory;
import core.SeedEndpointFactory;
import io.CmdInEndpoint;
import io.DataOutEndpoint;
import io.EndpointFactory;
import models.BCICommand;
import models.DataUnitsAdapter;
import models.EEGData;
import web.NetworkManager;

import java.util.HashSet;
import java.util.Set;

public class GuiAdapter implements CmdInEndpoint, DataOutEndpoint
{
    private final Set<CmdInEndpoint.Listener> m_listeners;
    private final GuiController               m_gui;
    private       int                         m_gain;

    public GuiAdapter(GuiController controller, int gain) {
        m_listeners = new HashSet<>();
        m_gain = gain;
        m_gui = controller;
        m_gui.setListener(new GuiController.Listener()
        {
            @Override
            public void onCommandCalled(BCICommand cmd) {
                for (CmdInEndpoint.Listener l : m_listeners)
                    l.onCmdReceived(cmd);
            }
            @Override
            public void onSeedModeSet(String comPort) {
                try {
                    String sessionId = NetworkManager.getInstance().reserveSessionId();
                    sendInfo("Session id: " + sessionId + "\n");
                    EndpointFactory factory = new SeedEndpointFactory(comPort, sessionId);
                    AppManager.getInstance().setMode(factory);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onLeechModeSet(String sessionId) {
                EndpointFactory factory = new LeechEndpointFactory(sessionId);
                AppManager.getInstance().setMode(factory);
            }
        });
    }
    @Override
    public void addListener(CmdInEndpoint.Listener listener) {
        m_listeners.add(listener);
    }
    @Override
    public void sendData(EEGData data) {
        m_gui.updateData(new DataUnitsAdapter(data, m_gain));
    }
    @Override
    public void sendInfo(String info) {
        m_gui.showInfo(info);
    }
    @Override
    public void unregisterListeners() {
        m_listeners.clear();
    }
    @Override
    public void open() {
        m_gui.clearData();
        m_gui.startGraph();
    }
    @Override
    public void close() {
        m_gui.stopGraph();
    }
}

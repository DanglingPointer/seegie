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
    /**
     * Sets gain used for unit conversions for the data displayed
     */
    public void setGain(int gain) {
        m_gain = gain;
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
        m_gui.showInfo(info.trim().substring(0, info.length() - 3)); // remove final $$$
    }
    @Override
    public void unregisterListeners() {
        m_listeners.clear();
    }
    @Override
    public void open() {

    }
    @Override
    public void close() {
        m_gui.clearData();
    }
}

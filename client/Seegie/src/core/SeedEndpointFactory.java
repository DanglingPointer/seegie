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

package core;

import io.CmdInEndpoint;
import io.CmdOutEndpoint;
import io.DataInEndpoint;
import io.DataOutEndpoint;
import io.EndpointFactory;
import serial.SerialAdapter;
import serial.SerialManager;
import ui.GuiAdapter;
import web.NetworkAdapter;
import web.NetworkManager;

public class SeedEndpointFactory implements EndpointFactory
{
    private final String m_serialName;
    private final String m_wsId;

    public SeedEndpointFactory(String serialPortName, String websockId) {
        m_serialName = serialPortName;
        m_wsId = websockId;
    }
    /**
     * @return serial port adapter
     */
    @Override
    public DataInEndpoint[] getDataInEndpoints() {
        SerialAdapter serial = SerialManager.getInstance().getAdapter(m_serialName);
        return new DataInEndpoint[]{ serial };
    }
    /**
     * @return network adapter, gui adapter
     */
    @Override
    public CmdInEndpoint[] getCmdInEndpoints() {
        NetworkAdapter net = null;
        try {
            net = NetworkManager.getInstance().getAdapter(m_wsId, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        GuiAdapter gui = AppManager.getInstance().getAdapter();
        return new CmdInEndpoint[]{ net, gui };
    }
    /**
     * @return network adapter, gui adapter
     */
    @Override
    public DataOutEndpoint[] getDataOutEndpoints() {
        NetworkAdapter net = null;
        try {
            net = NetworkManager.getInstance().getAdapter(m_wsId, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        GuiAdapter gui = AppManager.getInstance().getAdapter();
        return new DataOutEndpoint[]{ net, gui };
    }
    /**
     * @return Serial port adapter
     */
    @Override
    public CmdOutEndpoint[] getCmdOutEndpoints() {
        SerialAdapter serial = SerialManager.getInstance().getAdapter(m_serialName);
        return new CmdOutEndpoint[]{ serial };
    }
}

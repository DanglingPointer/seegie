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

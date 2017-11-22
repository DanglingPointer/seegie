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
import ui.GuiAdapter;
import web.NetworkAdapter;
import web.NetworkManager;

public class LeechEndpointFactory implements EndpointFactory
{
    private final String m_wsId;

    public LeechEndpointFactory(String wsSessionId) {
        m_wsId = wsSessionId;
    }
    /**
     * @return network adapter
     */
    @Override
    public DataInEndpoint[] getDataInEndpoints() {
        NetworkAdapter net = null;
        try {
            net = NetworkManager.getInstance().getAdapter(m_wsId, false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new DataInEndpoint[]{ net };
    }
    /**
     * @return gui adapter
     */
    @Override
    public CmdInEndpoint[] getCmdInEndpoints() {
        GuiAdapter gui = AppManager.getInstance().getAdapter();
        return new CmdInEndpoint[]{ gui };
    }
    /**
     * @return gui adapter
     */
    @Override
    public DataOutEndpoint[] getDataOutEndpoints() {
        GuiAdapter gui = AppManager.getInstance().getAdapter();
        return new DataOutEndpoint[]{ gui };
    }
    /**
     * @return network adapter
     */
    @Override
    public CmdOutEndpoint[] getCmdOutEndpoints() {
        NetworkAdapter net = null;
        try {
            net = NetworkManager.getInstance().getAdapter(m_wsId, false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new CmdOutEndpoint[]{ net };
    }
}

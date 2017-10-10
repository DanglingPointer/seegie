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
            System.out.println(e.toString());
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
            System.out.println(e.toString());
        }
        return new CmdOutEndpoint[]{ net };
    }
}

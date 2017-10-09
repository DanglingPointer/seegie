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
import web.NetworkAdapter;
import web.NetworkManager;

public class LeechEndpointFactory implements EndpointFactory
{
    private final String m_wsId;

    public LeechEndpointFactory(String websockId) {
        m_wsId = websockId;
    }
    /**
     * Websocket
     * @return
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
     * GUI
     * @return
     */
    @Override
    public CmdInEndpoint[] getCmdInEndpoints() {
        // TODO: 29.09.2017 obtain GUI adapter
        return new CmdInEndpoint[0];
    }
    /**
     * GUI
     * @return
     */
    @Override
    public DataOutEndpoint[] getDataOutEndpoints() {
        // TODO: 29.09.2017 obtain GUI adapter
        return new DataOutEndpoint[0];
    }
    /**
     * Websocket
     * @return
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

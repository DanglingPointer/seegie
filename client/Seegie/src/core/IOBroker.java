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
import io.InEndpoint;
import io.OutEndpoint;
import models.BCICommand;
import models.EEGData;

import java.util.HashSet;
import java.util.Set;


public class IOBroker
{
    private final Set<CmdInEndpoint>   m_cmdInEndpoints   = new HashSet<>();
    private final Set<CmdOutEndpoint>  m_cmdOutEndpoints  = new HashSet<>();
    private final Set<DataInEndpoint>  m_dataInEndpoints  = new HashSet<>();
    private final Set<DataOutEndpoint> m_dataOutEndpoints = new HashSet<>();

    public void registerEndpoint(DataInEndpoint ep) {
        m_dataInEndpoints.add(ep);
    }
    public void unregisterEndpoint(DataInEndpoint ep) {
        m_dataInEndpoints.remove(ep);
    }

    public void registerEndpoint(CmdInEndpoint ep) {
        m_cmdInEndpoints.add(ep);
    }
    public void unregisterEndpoint(CmdInEndpoint ep) {
        m_cmdInEndpoints.remove(ep);
    }

    public void registerEndpoint(DataOutEndpoint ep) {
        m_dataOutEndpoints.add(ep);
    }
    public void unregisterEndpoint(DataOutEndpoint ep) {
        m_dataOutEndpoints.remove(ep);
    }

    public void registerEndpoint(CmdOutEndpoint ep) {
        m_cmdOutEndpoints.add(ep);
    }
    public void unregisterEndpoint(CmdOutEndpoint ep) {
        m_cmdOutEndpoints.remove(ep);
    }

    public void setupEndpoints() {
        for (CmdInEndpoint iep : m_cmdInEndpoints) {
            iep.addListener(cmd -> {
                for (CmdOutEndpoint oep : m_cmdOutEndpoints)
                    oep.sendCmd(cmd);
            });
        }
        for (DataInEndpoint iep : m_dataInEndpoints) {
            iep.addListener(new DataInEndpoint.Listener()
            {
                @Override
                public void onDataReceived(EEGData data) {
                    for (DataOutEndpoint oep : m_dataOutEndpoints)
                        oep.sendData(data);
                }
                @Override
                public void onInfoReceived(String info) {
                    for (DataOutEndpoint oep : m_dataOutEndpoints)
                        oep.sendInfo(info);
                }
            });
        }
        for (DataOutEndpoint oep : m_dataOutEndpoints)
            oep.open();
        for (CmdOutEndpoint oep : m_cmdOutEndpoints)
            oep.open();
        for (DataInEndpoint iep : m_dataInEndpoints)
            iep.open();
        for (CmdInEndpoint iep : m_cmdInEndpoints)
            iep.open();
    }
    public void unregisterAll() {
        for (CmdInEndpoint iep : m_cmdInEndpoints) {
            iep.unregisterListeners();
            iep.close();
        }
        for (DataInEndpoint iep : m_dataInEndpoints) {
            iep.unregisterListeners();
            iep.close();
        }
        for (CmdOutEndpoint oep : m_cmdOutEndpoints) {
            oep.close();
        }
        for (DataOutEndpoint oep : m_dataOutEndpoints) {
            oep.close();
        }
        m_cmdInEndpoints.clear();
        m_cmdOutEndpoints.clear();
        m_dataInEndpoints.clear();
        m_dataOutEndpoints.clear();
    }
}

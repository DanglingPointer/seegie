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

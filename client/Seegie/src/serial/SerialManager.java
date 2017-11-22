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

package serial;

import com.fazecast.jSerialComm.SerialPort;

public class SerialManager
{
    private static SerialManager s_instance = new SerialManager();
    public static SerialManager getInstance() {
        return s_instance;
    }

    private SerialAdapter m_currentAdapter;
    private SerialPort[]  m_ports;

    private SerialManager() {
        m_currentAdapter = null;
        m_ports = null;
    }
    /**
     * Updates port list
     */
    public String[] getPorts() {
        m_ports = SerialPort.getCommPorts();
        String[] names = new String[m_ports.length];
        for (int i = 0; i < m_ports.length; ++i) {
            names[i] = m_ports[i].getSystemPortName();
        }
        return names;
    }
    /**
     * Uses the port list obtained during the last getPorts() call
     */
    public SerialAdapter getAdapter(String portName) {
        if (m_currentAdapter != null && m_currentAdapter.getPortName().equals(portName)) {
            return m_currentAdapter;
        }
        if (m_ports == null) {
            m_ports = SerialPort.getCommPorts();
        }
        for (SerialPort port : m_ports) {
            if (port.getSystemPortName().equals(portName)) {
                m_currentAdapter = new SerialAdapter(port);
                return m_currentAdapter;
            }
        }
        throw new IllegalArgumentException("Illegal port name");
    }
}

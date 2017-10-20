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

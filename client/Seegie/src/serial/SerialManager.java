/*
 * MIT License
 *
 * Copyright (c) 2017 Mikhail Vasilyev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package serial;

import com.fazecast.jSerialComm.SerialPort;

public class SerialManager
{
    private static SerialManager s_instance = new SerialManager();
    public static SerialManager getInstance() {
        return s_instance;
    }

    private SerialPort[] m_ports;

    private SerialManager() {
        m_ports = SerialPort.getCommPorts();
    }
    public String[] getPorts() {
        String[] names = new String[m_ports.length];
        for (int i = 0; i < m_ports.length; ++i) {
            names[i] = m_ports[i].getDescriptivePortName();
        }
        return names;
    }
    public SerialAdapter getAdapter(String portName) {
        return new SerialAdapter(portName);
    }
}

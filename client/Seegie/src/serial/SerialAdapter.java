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
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;
import io.CmdOutEndpoint;
import io.DataInEndpoint;
import models.BCICommand;
import models.EEGData;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class SerialAdapter implements DataInEndpoint, CmdOutEndpoint
{
    private final SerialPort                   m_port;
    private final Set<DataInEndpoint.Listener> m_listeners;

    SerialAdapter(SerialPort port) {
        m_port = port;
        m_listeners = new HashSet<>();
        m_port.addDataListener(new SerialPortPacketListener()
        {
            @Override
            public int getPacketSize() {
                return 33;
            }
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE | SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }
            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                byte[] data = new byte[m_port.bytesAvailable()];
                m_port.readBytes(data, data.length);

                switch (serialPortEvent.getEventType()) {
                    case SerialPort.LISTENING_EVENT_DATA_AVAILABLE:
                        String message = new String(data, StandardCharsets.US_ASCII);
                        for (DataInEndpoint.Listener l : m_listeners) {
                            l.onInfoReceived(message);
                        }
                        break;
                    case SerialPort.LISTENING_EVENT_DATA_RECEIVED:
                        EEGData values = new EEGData(data);
                        for (DataInEndpoint.Listener l : m_listeners) {
                            l.onDataReceived(values);
                        }
                        break;
                }
            }
        });
    }
    @Override
    public void sendCmd(BCICommand cmd) {
        byte[] data = cmd.getBytes();
        m_port.writeBytes(data, data.length);
    }
    @Override
    public void addListener(DataInEndpoint.Listener listener) {
        m_listeners.add(listener);
    }
    @Override
    public void unregisterListeners() {
        m_listeners.clear(); // do NOT remove listener from m_port!
    }
    @Override
    public void open() {
        m_port.openPort();
    }
    @Override
    public void close() {
        m_port.closePort();
    }
}

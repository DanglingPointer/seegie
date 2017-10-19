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
import com.fazecast.jSerialComm.SerialPortDataListener;
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
    private final int PACKET_LENGTH = 33;

    SerialAdapter(SerialPort port) {
        m_port = port;
        m_listeners = new HashSet<>();
//        m_port.addDataListener(new SerialPortDataListener()
//        {
//            @Override
//            public int getListeningEvents() {
//                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
//            }
//            @Override
//            public void serialEvent(SerialPortEvent serialPortEvent) {
//                System.out.println("Serial event " + serialPortEvent.getEventType());
//                System.out.println("Bytes available " + m_port.bytesAvailable());
//
//                if (serialPortEvent.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
//                    return;
//
//                byte[] data = new byte[m_port.bytesAvailable()];
//                int readCount = m_port.readBytes(data, data.length);
//
//                System.out.println("Data length is " + data.length);
//                System.out.println("Read count is " + readCount);
//
//                for (int pos = 0; pos < readCount; pos += PACKET_LENGTH) {
//                    byte[] packet = new byte[PACKET_LENGTH];
//
//                    int bytesToCopy = Math.min(PACKET_LENGTH, readCount-pos);
//                    System.arraycopy(data, pos, packet, 0, bytesToCopy);
//
//                    try {
//                        EEGData values = new EEGData(packet);
//                        System.out.println("Serial data received");
//                        for (DataInEndpoint.Listener l : m_listeners) {
//                            l.onDataReceived(values);
//                        }
//                    }
//                    catch (IllegalArgumentException e) {
//                        String message = new String(packet, StandardCharsets.UTF_8);
//                        System.out.println("Serial message received");
//                        for (DataInEndpoint.Listener l : m_listeners) {
//                            l.onInfoReceived(message);
//                        }
//                    }
//                }
//
//            }
//        });

        m_port.addDataListener(new SerialPortPacketListener()
        {
            @Override
            public int getPacketSize() {
                return PACKET_LENGTH;
            }
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }
            @Override
            public void serialEvent(SerialPortEvent serialPortEvent) {
                if (serialPortEvent.getEventType() != SerialPort.LISTENING_EVENT_DATA_RECEIVED)
                    return;

                byte[] data = serialPortEvent.getReceivedData();

                try {
                    EEGData values = new EEGData(data);
                    System.out.println("Serial data received");
                    for (DataInEndpoint.Listener l : m_listeners) {
                        l.onDataReceived(values);
                    }
                }
                catch (IllegalArgumentException e) {
                    String message = new String(data, StandardCharsets.UTF_8);
                    System.out.println("Serial message: " + message);
                    for (DataInEndpoint.Listener l : m_listeners) {
                        l.onInfoReceived(message);
                    }
                }
            }
        });
        m_port.setBaudRate(115200);
    }
    @Override
    public void sendCmd(BCICommand cmd) {
        byte[] data = cmd.getBytes();
        m_port.writeBytes(data, data.length);
        System.out.println("Command send to serial port");
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
        System.out.println("Serial port opened");
    }
    @Override
    public void close() {
        m_port.closePort();
    }
}

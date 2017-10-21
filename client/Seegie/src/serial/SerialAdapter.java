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
import io.CmdOutEndpoint;
import io.DataInEndpoint;
import models.BCICommand;
import models.EEGData;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class SerialAdapter implements DataInEndpoint, CmdOutEndpoint
{
    private class SerialParser implements SerialPortDataListener
    {
        private final ByteArrayOutputStream m_byteStream = new ByteArrayOutputStream();

        @Override
        public int getListeningEvents() {
            return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
        }
        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            if (serialPortEvent.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                return;

            byte[] data = new byte[m_port.bytesAvailable()];
            int readCount = m_port.readBytes(data, data.length);

            m_byteStream.write(data, 0, readCount);

            while (processBuffer())
                ;
        }
        private boolean processBuffer() {
            ByteBuffer buffer = ByteBuffer.wrap(m_byteStream.toByteArray());

            for (int pos = 0; pos < buffer.capacity(); ++pos) {

                if (pos + 2 < buffer.capacity()
                    && equalsUbyteChar(buffer.get(pos), '$')
                    && equalsUbyteChar(buffer.get(pos + 1), '$')
                    && equalsUbyteChar(buffer.get(pos + 2), '$')) {
                    // 3 consecutive $$$

                    extractInfo(buffer, pos + 3);

                    int restLength = rewriteRestToStream(buffer);
                    return restLength > 0;
                }
                if (equalsUbyteInt(buffer.get(pos), 0xA0) && buffer.capacity() - pos >= PACKET_LENGTH) {
                    // a complete data packet

                    if (pos > 0) {
                        // extract info preceding the start byte of the packet
                        extractInfo(buffer, pos); // ALTERNATIVELY: buffer.position(pos);
                    }
                    extractData(buffer);

                    int restLength = rewriteRestToStream(buffer);
                    return restLength > 0;
                }
            }
            return false;
        }
        private int rewriteRestToStream(ByteBuffer buffer) {
            byte[] restData = new byte[buffer.capacity() - buffer.position()];
            buffer.get(restData, 0, restData.length);

            m_byteStream.reset();
            m_byteStream.write(restData, 0, restData.length);
            return restData.length;
        }
        private void extractData(ByteBuffer buffer) {
            byte[] packetData = new byte[PACKET_LENGTH];
            buffer.get(packetData, 0, PACKET_LENGTH);
            EEGData values = new EEGData(packetData);
            for (DataInEndpoint.Listener l : m_listeners) {
                l.onDataReceived(values);
            }
        }
        private void extractInfo(ByteBuffer buffer, int length) {
            byte[] infoData = new byte[length];
            buffer.get(infoData, 0, infoData.length);

            int startPos = 0;
            for (int i = 0; i < length; ++i) {
                // skip regions with non-ascii symbols
                if ((infoData[i] & 0xFF) > 126)
                    startPos = i + 1;
            }
            if (startPos < length) {
                String message = new String(infoData, startPos, length - startPos, StandardCharsets.US_ASCII);
                for (DataInEndpoint.Listener l : m_listeners) {
                    l.onInfoReceived(message + "\n");
                }
            }
        }
        private boolean equalsUbyteChar(byte b, char c) {
            return (b & 0xFF) == (int)c;
        }
        private boolean equalsUbyteInt(byte b, int i) {
            return (b & 0xFF) == i;
        }
    }

    private final SerialPort                   m_port;
    private final Set<DataInEndpoint.Listener> m_listeners;
    private static final int PACKET_LENGTH = 33;

    SerialAdapter(SerialPort port) {
        m_port = port;
        m_port.removeDataListener();
        m_port.addDataListener(new SerialParser());
        m_listeners = new HashSet<>();
    }
    public String getPortName() {
        return m_port.getSystemPortName();
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
        m_listeners.clear();
    }
    @Override
    public void open() {
        m_port.setBaudRate(115200);
        m_port.openPort();
        System.out.println("Serial port opened");
    }
    @Override
    public void close() {
        m_port.closePort();
    }
}

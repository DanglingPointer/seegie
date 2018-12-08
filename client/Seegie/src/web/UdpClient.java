/*
 *     Copyright (C) 2018  Mikhail Vasilyev
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

package web;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URI;

public class UdpClient implements AutoCloseable
{
    public interface Listener
    {
        void onReceived(String msg);
        void onStopped();
        void onError(Exception e);
    }

    private final byte[] m_revcBuffer = new byte[512];

    private final    InetAddress    m_serverHostname;
    private final    int            m_serverPort;
    private final    DatagramSocket m_socket;
    private volatile Listener       m_callbacks;
    private          Thread         m_worker;

    public UdpClient(URI url) throws IOException {
        m_serverHostname = InetAddress.getByName(url.getHost());
        m_socket = new DatagramSocket();
        m_socket.setReuseAddress(true);
        m_serverPort = url.getPort();
    }
    public int getLocalPort() {
        return m_socket.getLocalPort();
    }
    public boolean isListening() {
        return m_worker != null;
    }
    public void setListener(Listener cb) {
        m_callbacks = cb;
    }
    public void send(String msg) throws IOException {
        byte[] data = msg.getBytes();
        m_socket.send(new DatagramPacket(data, data.length, m_serverHostname, m_serverPort));
    }
    public void startListening() {
        if (m_worker != null)
            return;
        m_worker = new Thread(this::listenBlocking);
        m_worker.start();
    }
    @Override
    public void close() {
        if (m_worker == null)
            return;
        m_socket.close();
        try {
            m_worker.join();
        }
        catch (InterruptedException e) {}
        m_worker = null;
    }
    private void listenBlocking() {
        if (m_callbacks == null)
            return;
        try {
            while (true) {
                DatagramPacket incoming = new DatagramPacket(m_revcBuffer, m_revcBuffer.length);
                m_socket.receive(incoming);
                if (!(incoming.getAddress().equals(m_serverHostname) && incoming.getPort() == m_serverPort))
                    continue;
                String msg = new String(incoming.getData(), incoming.getOffset(), incoming.getLength());
                m_callbacks.onReceived(msg);
            }
        }
        catch (SocketException e) {
            m_callbacks.onStopped();
        }
        catch (Exception e) {
            m_callbacks.onError(e);
        }
    }
}

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

package web;

import io.CmdInEndpoint;
import io.CmdOutEndpoint;
import io.DataInEndpoint;
import io.DataOutEndpoint;
import java_websocket.handshake.ServerHandshake;
import models.BCICmd;
import models.EEGData;

public class NetworkAdapter implements DataInEndpoint, DataOutEndpoint, CmdInEndpoint, CmdOutEndpoint
{
    private final WebSocketWrapper m_socket;

    NetworkAdapter(WebSocketWrapper websocket) {
        m_socket = websocket;
    }
    @Override
    public void addListener(CmdInEndpoint.Listener listener) {
        m_socket.addListener(new WebSocketWrapper.Listener()
        {
            @Override
            public void onOpened(ServerHandshake handshake) {}
            @Override
            public void onMessageReceived(String message) {
                listener.onCmdReceived(new BCICmd(message)); // temp
            }
            @Override
            public void onClosed(int code, String reason, boolean remote) {}
            @Override
            public void onErrorOccurred(Exception ex) {}
        });
    }
    @Override
    public void sendCmd(BCICmd cmd) {

    }
    @Override
    public void addListener(DataInEndpoint.Listener listener) {
        m_socket.addListener(new WebSocketWrapper.Listener()
        {
            @Override
            public void onOpened(ServerHandshake handshake) {}
            @Override
            public void onMessageReceived(String message) {
                listener.onDataReceived(new EEGData(message)); // temp
            }
            @Override
            public void onClosed(int code, String reason, boolean remote) {}
            @Override
            public void onErrorOccurred(Exception ex) {}
        });
    }
    @Override
    public void sendData(EEGData data) {

    }
    @Override
    public void unregisterListeners() {
        m_socket.removeAllListeners();
    }
    @Override
    public void open() {
        // TODO: 29.09.2017 open only if not already opened
    }
    @Override
    public void close() {
        // TODO: 29.09.2017 close only if not already closed
    }
}

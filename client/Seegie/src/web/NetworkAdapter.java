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
import models.BCICmd;
import models.EEGData;
import web.java_websocket.client.WebSocketClient;
import web.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class NetworkAdapter implements DataInEndpoint, DataOutEndpoint, CmdInEndpoint, CmdOutEndpoint
{
    private final WebSocketClient m_client;

    NetworkAdapter(String wsUrl) {
        try {
            URI link = new URI(wsUrl);
            m_client = new WebSocketClient(link)
            {
                @Override
                public void onOpen(ServerHandshake handshakedata) {

                }
                @Override
                public void onMessage(String message) {

                }
                @Override
                public void onClose(int code, String reason, boolean remote) {

                }
                @Override
                public void onError(Exception ex) {

                }
            };
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e.toString());
        }
    }
    @Override
    public void addListener(CmdInEndpoint.Listener listener) {

    }
    @Override
    public void sendCmd(BCICmd cmd) {

    }
    @Override
    public void addListener(DataInEndpoint.Listener listener) {

    }
    @Override
    public void sendData(EEGData data) {

    }
    @Override
    public void unregisterListeners() {

    }
    @Override
    public void open() {

    }
    @Override
    public void closed() {

    }
}

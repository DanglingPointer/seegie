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

package java_websocket.framing;

/**
 * Class to represent a pong frame
 */
public class PongFrame extends ControlFrame
{

    /**
     * constructor which sets the opcode of this frame to pong
     */
    public PongFrame() {
        super(Opcode.PONG);
    }

    /**
     * constructor which sets the opcode of this frame to ping copying over the payload of the ping
     *
     * @param pingFrame the PingFrame which payload is to copy
     */
    public PongFrame(PingFrame pingFrame) {
        super(Opcode.PONG);
        setPayload(pingFrame.getPayloadData());
    }
}

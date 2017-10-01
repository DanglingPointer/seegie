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

package java_websocket.exceptions;

import java_websocket.framing.CloseFrame;

/**
 * exception which indicates that a invalid handshake was recieved (CloseFrame.PROTOCOL_ERROR)
 */
public class InvalidHandshakeException extends InvalidDataException
{

    /**
     * Serializable
     */
    private static final long serialVersionUID = -1426533877490484964L;

    /**
     * constructor for a InvalidHandshakeException
     * <p>
     * calling InvalidDataException with closecode PROTOCOL_ERROR
     */
    public InvalidHandshakeException() {
        super(CloseFrame.PROTOCOL_ERROR);
    }

    /**
     * constructor for a InvalidHandshakeException
     * <p>
     * calling InvalidDataException with closecode PROTOCOL_ERROR
     *
     * @param s the detail message.
     * @param t the throwable causing this exception.
     */
    public InvalidHandshakeException(String s, Throwable t) {
        super(CloseFrame.PROTOCOL_ERROR, s, t);
    }

    /**
     * constructor for a InvalidHandshakeException
     * <p>
     * calling InvalidDataException with closecode PROTOCOL_ERROR
     *
     * @param s the detail message.
     */
    public InvalidHandshakeException(String s) {
        super(CloseFrame.PROTOCOL_ERROR, s);
    }

    /**
     * constructor for a InvalidHandshakeException
     * <p>
     * calling InvalidDataException with closecode PROTOCOL_ERROR
     *
     * @param t the throwable causing this exception.
     */
    public InvalidHandshakeException(Throwable t) {
        super(CloseFrame.PROTOCOL_ERROR, t);
    }

}

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

/**
 * exception which indicates the frame payload is not sendable
 */
public class NotSendableException extends RuntimeException {

    /**
     * Serializable
     */
    private static final long serialVersionUID = -6468967874576651628L;

    /**
     * constructor for a NotSendableException
     *
     * @param s the detail message.
     */
    public NotSendableException(String s) {
        super(s);
    }

    /**
     * constructor for a NotSendableException
     *
     * @param t the throwable causing this exception.
     */
    public NotSendableException(Throwable t) {
        super(t);
    }

    /**
     * constructor for a NotSendableException
     *
     * @param s the detail message.
     * @param t the throwable causing this exception.
     */
    public NotSendableException(String s, Throwable t) {
        super(s, t);
    }

}

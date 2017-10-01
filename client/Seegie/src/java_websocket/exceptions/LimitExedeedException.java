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
 * exception which indicates that the message limited was exedeeded (CloseFrame.TOOBIG)
 */
public class LimitExedeedException extends InvalidDataException
{

    /**
     * Serializable
     */
    private static final long serialVersionUID = 6908339749836826785L;

    /**
     * constructor for a LimitExedeedException
     * <p>
     * calling InvalidDataException with closecode TOOBIG
     */
    public LimitExedeedException() {
        super(CloseFrame.TOOBIG);
    }

    /**
     * constructor for a LimitExedeedException
     * <p>
     * calling InvalidDataException with closecode TOOBIG
     *
     * @param s the detail message.
     */
    public LimitExedeedException(String s) {
        super(CloseFrame.TOOBIG, s);
    }

}

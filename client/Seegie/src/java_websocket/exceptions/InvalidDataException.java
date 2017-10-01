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
 * exception which indicates that a invalid data was recieved
 */
public class InvalidDataException extends Exception {

    /**
     * Serializable
     */
    private static final long serialVersionUID = 3731842424390998726L;

    /**
     * attribut which closecode will be returned
     */
    private int closecode;

    /**
     * constructor for a InvalidDataException
     *
     * @param closecode the closecode which will be returned
     */
    public InvalidDataException(int closecode) {
        this.closecode = closecode;
    }

    /**
     * constructor for a InvalidDataException.
     *
     * @param closecode the closecode which will be returned.
     * @param s         the detail message.
     */
    public InvalidDataException(int closecode, String s) {
        super(s);
        this.closecode = closecode;
    }

    /**
     * constructor for a InvalidDataException.
     *
     * @param closecode the closecode which will be returned.
     * @param t         the throwable causing this exception.
     */
    public InvalidDataException(int closecode, Throwable t) {
        super(t);
        this.closecode = closecode;
    }

    /**
     * constructor for a InvalidDataException.
     *
     * @param closecode the closecode which will be returned.
     * @param s         the detail message.
     * @param t         the throwable causing this exception.
     */
    public InvalidDataException(int closecode, String s, Throwable t) {
        super(s, t);
        this.closecode = closecode;
    }

    /**
     * Getter closecode
     *
     * @return the closecode
     */
    public int getCloseCode() {
        return closecode;
    }

}

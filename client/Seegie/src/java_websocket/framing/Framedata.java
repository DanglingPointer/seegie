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

import java.nio.ByteBuffer;

public interface Framedata {
	/**
	 * Enum which contains the different valid opcodes
	 */
	enum Opcode {
		CONTINUOUS, TEXT, BINARY, PING, PONG, CLOSING
		// more to come
	}

	/**
	 * Indicates that this is the final fragment in a message.  The first fragment MAY also be the final fragment.
	 * @return true, if this frame is the final fragment
	 */
	boolean isFin();

	/**
	 * Indicates that this frame has the rsv1 bit set.
	 * @return true, if this frame has the rsv1 bit set
	 */
	boolean isRSV1();

	/**
	 * Indicates that this frame has the rsv2 bit set.
	 * @return true, if this frame has the rsv2 bit set
	 */
	boolean isRSV2();

	/**
	 * Indicates that this frame has the rsv3 bit set.
	 * @return true, if this frame has the rsv3 bit set
	 */
	boolean isRSV3();

	/**
	 * Defines whether the "Payload data" is masked.
	 * @return true, "Payload data" is masked
	 */
	boolean getTransfereMasked();

	/**
	 * Defines the interpretation of the "Payload data".
	 * @return the interpretation as a Opcode
	 */
	Opcode getOpcode();

	/**
	 * The "Payload data" which was sent in this frame
	 * @return the "Payload data" as ByteBuffer
	 */
	ByteBuffer getPayloadData();// TODO the separation of the application data and the extension data is yet to be done

	/**
	 * Appends an additional frame to the current frame
	 *
	 * This methods does not override the opcode, but does override the fin
	 * @param nextframe the additional frame
	 */
	void append(Framedata nextframe);
}

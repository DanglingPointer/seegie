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

import java_websocket.exceptions.InvalidDataException;
import java_websocket.exceptions.InvalidFrameException;

/**
 * Absstract class to represent control frames
 */
public abstract class ControlFrame extends FramedataImpl1
{

	/**
	 * Class to represent a control frame
	 * @param opcode the opcode to use
	 */
	public ControlFrame( Opcode opcode ) {
		super( opcode );
	}

	@Override
	public void isValid() throws InvalidDataException {
		if( !isFin() ) {
			throw new InvalidFrameException("Control frame cant have fin==false set" );
		}
		if( isRSV1() ) {
			throw new InvalidFrameException("Control frame cant have rsv1==true set" );
		}
		if( isRSV2() ) {
			throw new InvalidFrameException("Control frame cant have rsv2==true set" );
		}
		if( isRSV3() ) {
			throw new InvalidFrameException("Control frame cant have rsv3==true set" );
		}
	}
}

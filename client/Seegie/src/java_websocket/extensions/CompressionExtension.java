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

package java_websocket.extensions;

import java_websocket.exceptions.InvalidDataException;
import java_websocket.exceptions.InvalidFrameException;
import java_websocket.framing.ControlFrame;
import java_websocket.framing.DataFrame;
import java_websocket.framing.Framedata;

/**
 * Implementation for a compression extension specified by https://tools.ietf.org/html/rfc7692
 */
public abstract class CompressionExtension extends DefaultExtension
{

	@Override
	public void isFrameValid( Framedata inputFrame ) throws InvalidDataException {
		if(( inputFrame instanceof DataFrame) && (inputFrame.isRSV2() || inputFrame.isRSV3() )) {
			throw new InvalidFrameException("bad rsv RSV1: " + inputFrame.isRSV1() + " RSV2: " + inputFrame.isRSV2() + " RSV3: " + inputFrame.isRSV3() );
		}
		if(( inputFrame instanceof ControlFrame) && (inputFrame.isRSV1() || inputFrame.isRSV2() || inputFrame.isRSV3() )) {
			throw new InvalidFrameException("bad rsv RSV1: " + inputFrame.isRSV1() + " RSV2: " + inputFrame.isRSV2() + " RSV3: " + inputFrame.isRSV3() );
		}
	}
}

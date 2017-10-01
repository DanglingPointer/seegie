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
import java_websocket.framing.Framedata;

/**
 * Class which represents the normal websocket implementation specified by rfc6455.
 *
 * This is a fallback and will always be available for a Draft_6455
 *
 */
public class DefaultExtension implements IExtension
{

	@Override
	public void decodeFrame( Framedata inputFrame ) throws InvalidDataException {
		//Nothing to do here
	}

	@Override
	public void encodeFrame( Framedata inputFrame ) {
		//Nothing to do here
	}

	@Override
	public boolean acceptProvidedExtensionAsServer( String inputExtension ) {
		return true;
	}

	@Override
	public boolean acceptProvidedExtensionAsClient( String inputExtension ) {
		return true;
	}

	@Override
	public void isFrameValid( Framedata inputFrame ) throws InvalidDataException {
		if( inputFrame.isRSV1() || inputFrame.isRSV2() || inputFrame.isRSV3() ) {
			throw new InvalidFrameException("bad rsv RSV1: " + inputFrame.isRSV1() + " RSV2: " + inputFrame.isRSV2() + " RSV3: " + inputFrame.isRSV3() );
		}
	}

	@Override
	public String getProvidedExtensionAsClient() {
		return "";
	}

	@Override
	public String getProvidedExtensionAsServer() {
		return "";
	}

	@Override
	public IExtension copyInstance() {
		return new DefaultExtension();
	}

	public void reset() {
		//Nothing to do here. No internal stats.
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null ) return false;
		return getClass() == o.getClass();
	}
}

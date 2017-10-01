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

package java_websocket.client;

import java_websocket.AbstractWrappedByteChannel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

public abstract class AbstractClientProxyChannel extends AbstractWrappedByteChannel
{
	protected final ByteBuffer proxyHandshake;


	/**
	 * @param towrap
	 *            The channel to the proxy server
	 **/
	public AbstractClientProxyChannel( ByteChannel towrap ) {
		super( towrap );
		try {
			proxyHandshake = ByteBuffer.wrap( buildHandShake().getBytes( "ASCII" ) );
		} catch ( UnsupportedEncodingException e ) {
			throw new RuntimeException( e );
		}
	}

	@Override
	public int write( ByteBuffer src ) throws IOException {
		if( !proxyHandshake.hasRemaining() ) {
			return super.write( src );
		} else {
			return super.write( proxyHandshake );
		}
	}

	public abstract String buildHandShake();

}

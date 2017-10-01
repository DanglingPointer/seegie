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

package java_websocket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;


public class AbstractWrappedByteChannel implements WrappedByteChannel
{

	private final ByteChannel channel;

	public AbstractWrappedByteChannel( ByteChannel towrap ) {
		this.channel = towrap;
	}

	public AbstractWrappedByteChannel( WrappedByteChannel towrap ) {
		this.channel = towrap;
	}

	@Override
	public int read( ByteBuffer dst ) throws IOException {
		return channel.read( dst );
	}

	@Override
	public boolean isOpen() {
		return channel.isOpen();
	}

	@Override
	public void close() throws IOException {
		channel.close();
	}

	@Override
	public int write( ByteBuffer src ) throws IOException {
		return channel.write( src );
	}

	@Override
	public boolean isNeedWrite() {
		return channel instanceof WrappedByteChannel && ((WrappedByteChannel) channel).isNeedWrite();
	}

	@Override
	public void writeMore() throws IOException {
		if( channel instanceof WrappedByteChannel)
			( (WrappedByteChannel) channel ).writeMore();

	}

	@Override
	public boolean isNeedRead() {
		return channel instanceof WrappedByteChannel && ((WrappedByteChannel) channel).isNeedRead();

	}

	@Override
	public int readMore( ByteBuffer dst ) throws IOException {
		return channel instanceof WrappedByteChannel ? ( (WrappedByteChannel) channel ).readMore(dst ) : 0;
	}

	@Override
	public boolean isBlocking() {
		if( channel instanceof SocketChannel )
			return ( (SocketChannel) channel ).isBlocking();
		else if( channel instanceof WrappedByteChannel)
			return ( (WrappedByteChannel) channel ).isBlocking();
		return false;
	}

}

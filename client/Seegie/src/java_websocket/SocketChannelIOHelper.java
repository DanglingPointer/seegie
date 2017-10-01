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

import java_websocket.WebSocket.Role;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

public class SocketChannelIOHelper {

	public static boolean read(final ByteBuffer buf, WebSocketImpl ws, ByteChannel channel ) throws IOException {
		buf.clear();
		int read = channel.read( buf );
		buf.flip();

		if( read == -1 ) {
			ws.eot();
			return false;
		}
		return read != 0;
	}

	/**
	 * @see WrappedByteChannel#readMore(ByteBuffer)
	 * @param buf The ByteBuffer to read from
	 * @param ws The WebSocketImpl associated with the channels
	 * @param channel The channel to read from
	 * @return returns Whether there is more data left which can be obtained via {@link WrappedByteChannel#readMore(ByteBuffer)}
	 * @throws IOException May be thrown by {@link WrappedByteChannel#readMore(ByteBuffer)}#
	 **/
	public static boolean readMore(final ByteBuffer buf, WebSocketImpl ws, WrappedByteChannel channel ) throws IOException {
		buf.clear();
		int read = channel.readMore( buf );
		buf.flip();

		if( read == -1 ) {
			ws.eot();
			return false;
		}
		return channel.isNeedRead();
	}

	/** Returns whether the whole outQueue has been flushed
	 * @param ws The WebSocketImpl associated with the channels
	 * @param sockchannel The channel to write to
	 * @throws IOException May be thrown by {@link WrappedByteChannel#writeMore()}
	 * @return returns Whether there is more data to write
	 */
	public static boolean batch(WebSocketImpl ws, ByteChannel sockchannel ) throws IOException {
		ByteBuffer buffer = ws.outQueue.peek();
		WrappedByteChannel c = null;

		if( buffer == null ) {
			if( sockchannel instanceof WrappedByteChannel) {
				c = (WrappedByteChannel) sockchannel;
				if( c.isNeedWrite() ) {
					c.writeMore();
				}
			}
		} else {
			do {// FIXME writing as much as possible is unfair!!
				/*int written = */sockchannel.write( buffer );
				if( buffer.remaining() > 0 ) {
					return false;
				} else {
					ws.outQueue.poll(); // Buffer finished. Remove it.
					buffer = ws.outQueue.peek();
				}
			} while ( buffer != null );
		}

		if( ws != null && ws.outQueue.isEmpty() && ws.isFlushAndClose() && ws.getDraft() != null && ws.getDraft().getRole() != null && ws.getDraft().getRole() == Role.SERVER ) {//
			synchronized ( ws ) {
				ws.closeConnection();
			}
		}
		return c == null || !((WrappedByteChannel) sockchannel).isNeedWrite();
	}
}

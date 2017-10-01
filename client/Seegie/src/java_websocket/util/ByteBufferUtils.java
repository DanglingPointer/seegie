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

package java_websocket.util;

import java.nio.ByteBuffer;

/**
 * Utility class for ByteBuffers
 */
public class ByteBufferUtils {

	/**
	 * Private constructor for static class
	 */
	private ByteBufferUtils() {
	}

	/**
	 * Transfer from one ByteBuffer to another ByteBuffer
	 *
	 * @param source the ByteBuffer to copy from
	 * @param dest   the ByteBuffer to copy to
	 * @return the number of transferred bytes
	 */
	public static int transferByteBuffer( ByteBuffer source, ByteBuffer dest ) {
		if( source == null || dest == null ) {
			throw new IllegalArgumentException();
		}
		int fremain = source.remaining();
		int toremain = dest.remaining();
		if( fremain > toremain ) {
			int limit = Math.min( fremain, toremain );
			source.limit( limit );
			dest.put( source );
			return limit;
		} else {
			dest.put( source );
			return fremain;
		}
	}

	/**
	 * Get a ByteBuffer with zero capacity
	 *
	 * @return empty ByteBuffer
	 */
	public static ByteBuffer getEmptyByteBuffer() {
		return ByteBuffer.allocate( 0 );
	}
}

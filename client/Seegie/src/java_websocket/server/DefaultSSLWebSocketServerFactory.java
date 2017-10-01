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

package java_websocket.server;

import java_websocket.SSLSocketChannel2;
import java_websocket.WebSocketAdapter;
import java_websocket.WebSocketImpl;
import java_websocket.WebSocketServerFactory;
import java_websocket.drafts.Draft;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.IOException;
import java.nio.channels.ByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultSSLWebSocketServerFactory implements WebSocketServerFactory
{
	protected SSLContext sslcontext;
	protected ExecutorService exec;

	public DefaultSSLWebSocketServerFactory( SSLContext sslContext ) {
		this( sslContext, Executors.newSingleThreadScheduledExecutor() );
	}

	public DefaultSSLWebSocketServerFactory( SSLContext sslContext , ExecutorService exec ) {
		if( sslContext == null || exec == null )
			throw new IllegalArgumentException();
		this.sslcontext = sslContext;
		this.exec = exec;
	}

	@Override
	public ByteChannel wrapChannel( SocketChannel channel, SelectionKey key ) throws IOException {
		SSLEngine e = sslcontext.createSSLEngine();
		/**
		 * See https://github.com/TooTallNate/Java-WebSocket/issues/466
		 *
		 * We remove TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256 from the enabled ciphers since it is just available when you patch your java installation directly.
		 * E.g. firefox requests this cipher and this causes some dcs/instable connections
		 */
		List<String> ciphers = new ArrayList<String>( Arrays.asList(e.getEnabledCipherSuites()));
		ciphers.remove("TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256");
		e.setEnabledCipherSuites( ciphers.toArray(new String[]{}));
		e.setUseClientMode( false );
		return new SSLSocketChannel2(channel, e, exec, key );
	}

	@Override
	public WebSocketImpl createWebSocket(WebSocketAdapter a, Draft d) {
		return new WebSocketImpl(a, d );
	}

	@Override
	public WebSocketImpl createWebSocket(WebSocketAdapter a, List<Draft> d) {
		return new WebSocketImpl(a, d );
	}
	@Override
	public void close() {
		exec.shutdown();
	}
}
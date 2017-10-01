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

package java_websocket.drafts;

import java_websocket.extensions.IExtension;
import java_websocket.handshake.ClientHandshakeBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description:
 * Base on Draft_6455
 * You can use this Draft to connect the WebRTC Candidate server(WebSocket)
 * The official Candidate server run on go programming language
 * I found The ws/wss HandShake request must have origin param
 * If not it will return http code 403
 * If you get http code 403 and you ws/wss server run on go, you can try this Draft
 * Author:totoroYang 2017/8/8.
 */

public class Draft_6455_WebRTC extends Draft_6455
{

    /**
     * Constructor for the websocket protocol specified by RFC 6455 with default extensions for the WebRTC Candidate server(WebSocket)
     */
    public Draft_6455_WebRTC() {
        this( Collections.<IExtension>emptyList() );
    }

    /**
     * Constructor for the websocket protocol specified by RFC 6455 with custom extensions for the WebRTC Candidate server(WebSocket)
     *
     * @param inputExtension the extension which should be used for this draft
     */
    public Draft_6455_WebRTC( IExtension inputExtension ) {
        this( Collections.singletonList( inputExtension ) );
    }

    /**
     * Constructor for the websocket protocol specified by RFC 6455 with custom extensions for the WebRTC Candidate server(WebSocket)
     *
     * @param inputExtensions the extensions which should be used for this draft
     */
    public Draft_6455_WebRTC( List<IExtension> inputExtensions ) {
        super(inputExtensions);
    }

    @Override
    public ClientHandshakeBuilder postProcessHandshakeRequestAsClient(ClientHandshakeBuilder request) {
        super.postProcessHandshakeRequestAsClient(request);
		request.put("origin", request.getFieldValue("host"));
        return request;
    }

    @Override
    public Draft copyInstance() {
        ArrayList<IExtension> newExtensions = new ArrayList<IExtension>();
        for( IExtension extension : knownExtensions ) {
            newExtensions.add( extension.copyInstance() );
        }
        return new Draft_6455_WebRTC(newExtensions );
    }
}
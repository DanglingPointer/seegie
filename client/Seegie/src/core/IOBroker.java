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

package core;

import io.CmdInEndpoint;
import io.CmdOutEndpoint;
import io.DataInEndpoint;
import io.DataOutEndpoint;
import io.InEndpoint;
import io.OutEndpoint;

import java.util.HashSet;
import java.util.Set;


public class IOBroker
{
    private final Set<InEndpoint>  m_inEndpoints  = new HashSet<>();
    private final Set<OutEndpoint> m_outEndpoints = new HashSet<>();

    public void registerEndpoint(DataInEndpoint ep) {
        // TODO: 29.09.2017
    }
    public void unregisterEndpoint(DataInEndpoint ep) {
        // TODO: 29.09.2017
    }

    public void registerEndpoint(CmdInEndpoint ep) {
        // TODO: 29.09.2017
    }
    public void unregisterEndpoint(CmdInEndpoint ep) {
        // TODO: 29.09.2017
    }

    public void registerEndpoint(DataOutEndpoint ep) {
        // TODO: 29.09.2017
    }
    public void unregisterEndpoint(DataOutEndpoint ep) {
        // TODO: 29.09.2017
    }

    public void registerEndpoint(CmdOutEndpoint ep) {
        // TODO: 29.09.2017
    }
    public void unregisterEndpoint(CmdOutEndpoint ep) {
        // TODO: 29.09.2017
    }

    public void setupEndpoints() {
        // TODO: 29.09.2017 add listeners between registered endpoints. Do we need it??
    }

    public void unregisterAll() {
        // TODO: 29.09.2017 close all endpoints and clear all lists
    }
}

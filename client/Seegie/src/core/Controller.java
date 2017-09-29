/*
 * MIT License
 *
 * Copyright (c) 2017 Mikhail Vasilyev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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


public class Controller
{
    private final Set<InEndpoint>  m_inEndpts     = new HashSet<>();
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

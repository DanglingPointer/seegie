/*
 *     Copyright (C) 2017-2018  Mikhail Vasilyev
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package models;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;

public final class Serializer
{
    private static class Datagram1
    {
        public Datagram1() {}
        public Datagram1(String type, String content) {
            this.type = type;
            this.content = content;
        }
        public String type;
        public String content;
    }
    private static class Datagram2
    {
        public Datagram2() {}
        public Datagram2(String type, String sessionId, EEGData content) {
            this.type = type;
            this.sessionId = sessionId;
            this.content = content;
        }
        public String type;
        public String sessionId;
        public EEGData content;
    }
    private static class Datagram3
    {
        public Datagram3(String type, String sessionId, int recvPort) {
            this.type = type;
            this.sessionId = sessionId;
            this.recvPort = recvPort;
        }
        public String type;
        public String sessionId;
        public int recvPort;
    }

    public static final String TYPE_DATA = "&data&";
    public static final String TYPE_INFO = "&info&";
    public static final String TYPE_CMD  = "&cmd&";
    public static final String TYPE_INVITE = "&invite&";

    public static String command2Json(BCICommand cmd) {
        Datagram1 dgram = new Datagram1(TYPE_CMD, cmd.toString());
        return JsonStream.serialize(dgram);
    }
    public static BCICommand json2Command(String json) {
        Datagram1 dgram = JsonIterator.deserialize(json, Datagram1.class);
        return dgram.type.equals(TYPE_CMD) ? new BCICommandImpl(dgram.content) : null;
    }

    public static String info2Json(String info) {
        Datagram1 dgram = new Datagram1(TYPE_INFO, info);
        return JsonStream.serialize(dgram);
    }
    public static String json2Info(String json) {
        Datagram1 dgram = JsonIterator.deserialize(json, Datagram1.class);
        return dgram.type.equals(TYPE_INFO) ? dgram.content : null;
    }

    public static String data2Json(String sessionId, EEGData data) {
        Datagram2 dgram = new Datagram2(TYPE_DATA, sessionId, data);
        return JsonStream.serialize(dgram);
    }
    public static EEGData json2Data(String json) {
        Datagram2 dgram = JsonIterator.deserialize(json, Datagram2.class);
        return dgram.type.equals(TYPE_DATA) ? dgram.content : null;
    }

    public static String invite2Json(String sessionId, int localPort) {
        Datagram3 dgram = new Datagram3(TYPE_INVITE, sessionId, localPort);
        return JsonStream.serialize(dgram);
    }
}

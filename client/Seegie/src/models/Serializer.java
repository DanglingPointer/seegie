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
        public Datagram2(String type, EEGData content) {
            this.type = type;
            this.content = content;
        }
        public String type;
        public EEGData content;
    }

    public static final String TYPE_DATA = "&data&";
    public static final String TYPE_INFO = "&info&";
    public static final String TYPE_CMD  = "&cmd&";

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

    public static String data2Json(EEGData data) {
        Datagram2 dgram = new Datagram2(TYPE_DATA, data);
        return JsonStream.serialize(dgram);
    }
    public static EEGData json2Data(String json) {
        Datagram2 dgram = JsonIterator.deserialize(json, Datagram2.class);
        return dgram.content;
    }

}

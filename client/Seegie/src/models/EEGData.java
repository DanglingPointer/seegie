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


public class EEGData
{
    public int  sampleNum;
    public long timeStamp;
    public final int[] channelData = new int[8];
    public int     acclX;
    public int     acclY;
    public int     acclZ;
    public boolean timeStampSet;

    public EEGData(byte[] rawData) {
        if (rawData.length != 33 || ubyteToInt32(rawData[0]) != 0xA0) {
            throw new IllegalArgumentException("Data length = " + rawData.length);
        }
        parseBytes(rawData);
    }
    private EEGData() {} // for deserialization

    private void parseBytes(byte[] rawData) {
        int channelIndex = 0;
        for (int rawIndex = 2; rawIndex <= 23; rawIndex += 3) {
            byte[] value = new byte[3];
            System.arraycopy(rawData, rawIndex, value, 0, 3);
            channelData[channelIndex] = int24ToInt32(value);
            channelIndex++;
        }
        sampleNum = ubyteToInt32(rawData[1]);

        int stopByte = ubyteToInt32(rawData[32]);
        if (stopByte == 0xC0) {
            byte[] accelX = new byte[2];
            System.arraycopy(rawData, 26, accelX, 0, 2);
            acclX = int16ToInt32(accelX);

            byte[] accelY = new byte[2];
            System.arraycopy(rawData, 28, accelY, 0, 2);
            acclY = int16ToInt32(accelY);

            byte[] accelZ = new byte[2];
            System.arraycopy(rawData, 30, accelZ, 0, 2);
            acclZ = int16ToInt32(accelZ);
        }
        else if (stopByte >= 0xC3 && stopByte <= 0xC6) {
            byte[] timeStamp = new byte[4];
            System.arraycopy(rawData, 28, timeStamp, 0, 4);
            this.timeStamp = uint32ToLong(timeStamp);
        }
        timeStampSet = (stopByte == 0xC3 || stopByte == 0xC5);
    }
    private static int ubyteToInt32(byte unsigned) {
        return (0xFF & unsigned);
    }
    private static long uint32ToLong(byte[] uint32) {
        long value = (
                ((0xFF & uint32[0]) << 24) |
                ((0xFF & uint32[1]) << 16) |
                ((0xFF & uint32[2]) << 8) |
                (0xFF & uint32[3])
        );
        value &= 0x00000000FFFFFFFF;
        return value;
    }
    private static int int24ToInt32(byte[] int24) {
        int int32 = (
                ((0xFF & int24[0]) << 16) |
                ((0xFF & int24[1]) << 8) |
                (0xFF & int24[2])
        );
        if ((int32 & 0x00800000) > 0)
            int32 |= 0xFF000000;
        else
            int32 &= 0x00FFFFFF;
        return int32;
    }
    private static int int16ToInt32(byte[] int16) {
        int int32 = (
                ((0xFF & int16[0]) << 8) |
                (0xFF & int16[1])
        );
        if ((int32 & 0x00008000) > 0)
            int32 |= 0xFFFF0000;
        else
            int32 &= 0x0000FFFF;
        return int32;
    }
}

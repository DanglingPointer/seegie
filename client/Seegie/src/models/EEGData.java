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

import java.util.Arrays;

public class EEGData
{
    private int  m_sampleNumber;
    private long m_timeStamp;
    private final int[] m_channelData = new int[8];
    private int     m_accelerationX;
    private int     m_accelerationY;
    private int     m_accelerationZ;
    private boolean m_timeStampSet;

    public EEGData(byte[] rawData) {
        if (rawData.length != 33) {
            throw new IllegalArgumentException("Data length = " + rawData.length);
        }
        parseBytes(rawData);
    }
    public int getSampleNumber() {
        return m_sampleNumber;
    }
    public long getTimeStamp() {
        return m_timeStamp;
    }
    public boolean getTimeStampSet() {
        return m_timeStampSet;
    }
    public int[] getChannelData() {
        return Arrays.copyOf(m_channelData, m_channelData.length);
    }
    public int getAccelerationX() {
        return m_accelerationX;
    }
    public int getAccelerationY() {
        return m_accelerationY;
    }
    public int getAccelerationZ() {
        return m_accelerationZ;
    }
    private void parseBytes(byte[] rawData) {
        int channelIndex = 0;
        for (int rawIndex = 2; rawIndex <= 23; rawIndex += 3) {
            byte[] value = new byte[3];
            System.arraycopy(rawData, rawIndex, value, 0, 3);
            m_channelData[channelIndex] = int24ToInt32(value);
            channelIndex++;
        }
        m_sampleNumber = ubyteToInt32(rawData[1]);

        int stopByte = ubyteToInt32(rawData[32]);
        if (stopByte == 0xC0) {
            byte[] accelX = new byte[2];
            System.arraycopy(rawData, 26, accelX, 0, 2);
            m_accelerationX = int16ToInt32(accelX);

            byte[] accelY = new byte[2];
            System.arraycopy(rawData, 28, accelY, 0, 2);
            m_accelerationY = int16ToInt32(accelY);

            byte[] accelZ = new byte[2];
            System.arraycopy(rawData, 30, accelZ, 0, 2);
            m_accelerationZ = int16ToInt32(accelZ);
        }
        else if (stopByte >= 0xC3 && stopByte <= 0xC6) {
            byte[] timeStamp = new byte[4];
            System.arraycopy(rawData, 28, timeStamp, 0, 4);
            m_timeStamp = uint32ToLong(timeStamp);
        }
        m_timeStampSet = (stopByte == 0xC3 || stopByte == 0xC5);
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

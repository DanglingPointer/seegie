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

public class DataUnitsAdapter
{
    private final EEGData m_data;
    private final double[] m_voltsData = new double[8];
    private double m_accelerationX;
    private double m_accelerationY;
    private double m_accelerationZ;

    public DataUnitsAdapter(EEGData rawData, int gain) {
        m_data = rawData;
        double eegScaleFactor = 4.5d / gain / 8388607.0d;
        double aclScaleFactor = 0.000125d;

        int[] countsData = m_data.channelData;
        for (int i = 0; i < 8; ++i) {
            m_voltsData[i] = countsData[i] * eegScaleFactor;
        }
        m_accelerationX = m_data.acclX * aclScaleFactor;
        m_accelerationY = m_data.acclY * aclScaleFactor;
        m_accelerationZ = m_data.acclZ * aclScaleFactor;
    }
    public double[] getVoltsData() {
        return Arrays.copyOf(m_voltsData, m_voltsData.length);
    }
    public double getAccelerationX() {
        return m_accelerationX;
    }
    public double getAccelerationY() {
        return m_accelerationY;
    }
    public double getAccelerationZ() {
        return m_accelerationZ;
    }
    public int getSampleNumber() {
        return m_data.sampleNum;
    }
    public long getTimeStamp() {
        return m_data.timeStamp;
    }
    public boolean getTimeStampSet() {
        return m_data.timeStampSet;
    }
}

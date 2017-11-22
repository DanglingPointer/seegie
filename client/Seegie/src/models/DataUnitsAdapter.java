/*
 *     Copyright (C) 2017  Mikhail Vasilyev
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

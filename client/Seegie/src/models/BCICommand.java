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

import java.nio.charset.StandardCharsets;

public interface BCICommand
{
    // misc
    char START_STREAM          = 'b';
    char STOP_STREAM           = 's';
    char RESET                 = 'v';
    char USE_8CHANS            = 'c';
    char USE_16CHANS           = 'C';
    char CHAN_SETTINGS_DEFAULT = 'd';
    char CHAN_SETTINGS_QUERY   = 'D';
    char REGS_SETTINGS_QUERY   = '?';

    // SD card
    char LOG_5MIN  = 'A';
    char LOG_15MIN = 'S';
    char LOG_30MIN = 'F';
    char LOG_1HR   = 'G';
    char LOG_2HR   = 'H';
    char LOG_4HR   = 'J';
    char LOG_12HR  = 'K';
    char LOG_24HR  = 'L';
    char LOG_TEST  = 'a';
    char LOG_CLOSE = 'j';

    // channels on/off
    char CHAN1_OFF = '1';
    char CHAN2_OFF = '2';
    char CHAN3_OFF = '3';
    char CHAN4_OFF = '4';
    char CHAN5_OFF = '5';
    char CHAN6_OFF = '6';
    char CHAN7_OFF = '7';
    char CHAN8_OFF = '8';

    char CHAN9_OFF  = 'q';
    char CHAN10_OFF = 'w';
    char CHAN11_OFF = 'e';
    char CHAN12_OFF = 'r';
    char CHAN13_OFF = 't';
    char CHAN14_OFF = 'y';
    char CHAN15_OFF = 'u';
    char CHAN16_OFF = 'i';

    char CHAN1_ON = '!';
    char CHAN2_ON = '@';
    char CHAN3_ON = '#';
    char CHAN4_ON = '$';
    char CHAN5_ON = '%';
    char CHAN6_ON = '^';
    char CHAN7_ON = '%';
    char CHAN8_ON = '*';

    char CHAN9_ON  = 'Q';
    char CHAN10_ON = 'W';
    char CHAN11_ON = 'E';
    char CHAN12_ON = 'R';
    char CHAN13_ON = 'T';
    char CHAN14_ON = 'Y';
    char CHAN15_ON = 'U';
    char CHAN16_ON = 'I';

    // channel signal tests
    char TEST_GND       = '0';
    char TEST_1AMP_SLOW = '-';
    char TEST_1AMP_FAST = '=';
    char TEST_DC        = 'p';
    char TEST_2AMP_SLOW = '[';
    char TEST_2AMP_FAST = ']';

    // channel settings
    char ADSINPUT_NORMAL    = '0';
    char ADSINPUT_SHORTED   = '1';
    char ADSINPUT_BIAS_MEAS = '2';
    char ADSINPUT_MVDD      = '3';
    char ADSINPUT_TEMP      = '4';
    char ADSINPUT_TESTSIG   = '5';
    char ADSINPUT_BIAS_DRP  = '6';
    char ADSINPUT_BIAS_DRN  = '7';

    // firmware v2.0
    char FW_TIMESTAMP_ON      = '<';
    char FW_TIMESTAMP_OFF     = '>';
    char FW_GET_CHAN          = 0x00;
    char FW_SET_CHAN          = 0x01;
    char FW_SET_CHAN_OVERRIDE = 0x02;
    char FW_GET_POLL_TIME     = 0x03;
    char FW_SET_POLL_TIME     = 0x04;
    char FW_SET_BAUD_DEFAULT  = 0x05;
    char FW_SET_BAUD_HIGH     = 0x06;
    char FW_SET_BAUD_HYPER    = 0x0A;
    char FW_GET_STATUS        = 0x07;

    byte[] getBytes();
    String toString();
    boolean equals(Object obj);
    int hashCode();
    BCICommand clone();

    class Builder
    {
        private BCICommandImpl m_cmd;

        public Builder() {
            m_cmd = new BCICommandImpl();
        }
        /**
         * Single-symbol command.
         * @param command: channels on/off, start/stop stream, test signal, default channel settings, SD card logging,
         *                 misc, channel count
         * @return this builder
         */
        public Builder addSimpleCmd(char command) {
            m_cmd.addSymbol(command);
            return this;
        }
        /**
         * LeadOFF Impedance command.
         * @param channelNo: 1-8 for 8-channel system, 1-16 for 16-channel system
         * @param pchan:     measure impedance on P input
         * @param nchan:     measure impedance on N input
         * @return this builder
         */
        public Builder addLeadoffImpedanceCmd(int channelNo, boolean pchan, boolean nchan) {
            m_cmd.addSymbol('z');
            m_cmd.addSymbol(getChannelNo(channelNo));
            m_cmd.addSymbol(pchan ? '1' : '0');
            m_cmd.addSymbol(nchan ? '1' : '0');
            m_cmd.addSymbol('Z');
            return this;
        }
        /**
         * Channel settings command.
         * @param channelNo:    1-8 for 8-channel system, 1-16 for 16-channel system
         * @param powerOn:      true for on, false for off
         * @param gainSet:      1, 2, 4, 6, 8, 12 or 24
         * @param adsInputType: one of the commands starting with 'ADSINPUT_'
         * @param bias:         include in BIAS
         * @param srb2:         connect this input to SRB2
         * @param srb1:         connect all inputs to SRB1
         * @return this builder
         */
        public Builder addChanSettingsCmd(int channelNo, boolean powerOn, int gainSet, char adsInputType, boolean bias, boolean srb2, boolean srb1) {
            m_cmd.addSymbol('x');

            m_cmd.addSymbol(getChannelNo(channelNo));

            m_cmd.addSymbol(powerOn ? '0' : '1');

            char gain = '0';
            switch (gainSet) {
                case 1: gain = '0'; break;
                case 2: gain = '1'; break;
                case 4: gain = '2'; break;
                case 6: gain = '3'; break;
                case 8: gain = '4'; break;
                case 12: gain = '5'; break;
                case 24: gain = '6'; break;
            }
            m_cmd.addSymbol(gain);

            char inputType = '0';
            if (adsInputType >= '0' && adsInputType <= '7') {
                inputType = adsInputType;
            }
            m_cmd.addSymbol(inputType);

            m_cmd.addSymbol(bias ? '1' : '0');
            m_cmd.addSymbol(srb2 ? '1' : '0');
            m_cmd.addSymbol(srb1 ? '1' : '0');

            m_cmd.addSymbol('X');
            return this;
        }
        /**
         * Firmware v2.0 command with no arguments
         * @param fwCommand: one of the commands starting with 'FW_'
         * @return this builder
         */
        public Builder addFirmwareCmd(char fwCommand) {
            m_cmd.addSymbol((char)0x0F);
            m_cmd.addSymbol(fwCommand);
            return this;
        }
        /**
         * Firmware v2.0 command requiring one argument
         * @param fwCommand: one of the commands starting with 'FW_'
         * @param arg:       channel number or poll time, depending on fwCommand
         * @return this builder
         */
        public Builder addFirmwareCmd(char fwCommand, int arg) {
            addFirmwareCmd(fwCommand);
            m_cmd.addSymbol((char)arg);
            return this;
        }
        public BCICommand build() {
            return m_cmd;
        }
        private static char getChannelNo(int channelNo) {
            char channel = '1';
            if (channelNo > 0 && channelNo < 9) {
                channel = (char)(channelNo + '0');
            }
            else if (channelNo > 8 && channelNo < 17) {
                switch (channelNo) {
                    case 9: channel = 'Q'; break;
                    case 10: channel = 'W'; break;
                    case 11: channel = 'E'; break;
                    case 12: channel = 'R'; break;
                    case 13: channel = 'T'; break;
                    case 14: channel = 'Y'; break;
                    case 15: channel = 'U'; break;
                    case 16: channel = 'I'; break;
                }
            }
            return channel;
        }
    }
}

class BCICommandImpl implements BCICommand
{
    private final StringBuilder m_data;

    public BCICommandImpl() {
        m_data = new StringBuilder();
    }
    public BCICommandImpl(String cmd) {
        m_data = new StringBuilder(cmd);
    }
    public void addSymbol(char symbol) {
        m_data.append(symbol);
    }
    public byte[] getBytes() {
        return m_data.toString().getBytes(StandardCharsets.US_ASCII);
    }
    @Override
    public String toString() {
        return m_data.toString();
    }
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof BCICommand) && (toString().equals(obj.toString()));
    }
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    @Override
    public BCICommand clone() {
        return new BCICommandImpl(toString());
    }
}

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

package models;

class BCICmd
{
    public enum SingleLetterCmd
    {
        START('b'), STOP('s'), RESET('v'),
        CHAN1_OFF('1'), CHAN2_OFF('2'), CHAN3_OFF('3'), CHAN4_OFF('4'), CHAN5_OFF('5'), CHAN6_OFF('6'), CHAN7_OFF('7'), CHAN8_OFF('8'),
        CHAN1_ON('!'), CHAN2_ON('@'), CHAN3_ON('#'), CHAN4_ON('$'), CHAN5_ON('%'), CHAN6_ON('^'), CHAN7_ON('&'), CHAN8_ON('*'),
        TEST_GND('0'), TEST_1AMP_SLOW('-'), TEST_1AMP_FAST('='), TEST_DC('p'), TEST_2AMP_SLOW('['), TEST_2AMP_FAST(']');

        private byte m_symbol;
        SingleLetterCmd(char cmd) {
            m_symbol = (byte)cmd;
        }
        public byte getByte() {
            return m_symbol;
        }
    }

    public BCICmd(String cmd) {
        // temp
    }
    public byte[] toByteArray() {
        // TODO: 30.09.2017
        return new byte[0];
    }
}

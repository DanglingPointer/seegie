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

package ui;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {

//        try {
//            SerialPort[] ports = SerialPort.getCommPorts();
//            SerialPort com1 = ports[0];
//            SerialPort com2 = ports[1];
//
//            com1.openPort();
//            System.out.println("COM1 open: " + com1.isOpen());
//            com2.openPort();
//            System.out.println("COM2 open: " + com2.isOpen());
//            com2.addDataListener(new SerialPortDataListener()
//            {
//                @Override
//                public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
//                @Override
//                public void serialEvent(SerialPortEvent event)
//                {
//                    System.out.println("Serial event triggered");
//                    if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
//                        return;
//                    byte[] newData = new byte[com2.bytesAvailable()];
//                    int numRead = com2.readBytes(newData, newData.length);
//                    System.out.println("Read " + numRead + " bytes.");
//                    for (byte b : newData) {
//                        System.out.print(b);
//                    }
//                }
//            });
//
//            com1.writeBytes(new byte[]{ 0, 1, 2, 3, 4, 5 }, 6);
//
//            com1.closePort();
//            com2.closePort();
//
//            System.out.println("\nCOM1 open: " + com1.isOpen());
//            System.out.println("COM2 open: " + com2.isOpen());
//        }
//        catch (Exception e) {
//            System.out.println(e.toString());
//            e.printStackTrace();
//        }

        launch(args);
    }
}

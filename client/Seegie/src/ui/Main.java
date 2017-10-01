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

        byte b = 0x0C;

        int i = b;

        if (i == 0x0C)
            System.out.println("lol");

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

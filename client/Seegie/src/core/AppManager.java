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

package core;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import io.CmdInEndpoint;
import io.CmdOutEndpoint;
import io.DataInEndpoint;
import io.DataOutEndpoint;
import io.EndpointFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import models.BCICommand;
import models.EEGData;
import models.Serializer;
import ui.GuiAdapter;
import ui.GuiController;
import web.NetworkAdapter;
import web.NetworkManager;

import java.util.Random;

public class AppManager extends Application
{
    private static AppManager s_instance = null;
    public static AppManager getInstance() {
        return s_instance;
    }

    private GuiAdapter m_adapter;
    private final IOBroker m_broker = new IOBroker();

    public AppManager() {
        super();
        s_instance = this;
    }
    public GuiAdapter getAdapter() {
        return m_adapter;
    }
    /**
     * Sets up program mode (seed/leech) depending on the factory type, and starts the program
     */
    public void setMode(EndpointFactory factory) {
        m_broker.unregisterAll();

        DataInEndpoint[] dInEp = factory.getDataInEndpoints();
        for (DataInEndpoint ep : dInEp) {
            m_broker.registerEndpoint(ep);
        }
        CmdInEndpoint[] cInEp = factory.getCmdInEndpoints();
        for (CmdInEndpoint ep : cInEp) {
            m_broker.registerEndpoint(ep);
        }
        DataOutEndpoint[] dOutEp = factory.getDataOutEndpoints();
        for (DataOutEndpoint ep : dOutEp) {
            m_broker.registerEndpoint(ep);
        }
        CmdOutEndpoint[] cOutEp = factory.getCmdOutEndpoints();
        for (CmdOutEndpoint ep : cOutEp) {
            m_broker.registerEndpoint(ep);
        }
        m_broker.setupEndpoints();
    }
    @Override
    public void stop() throws Exception {
        m_broker.unregisterAll();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(GuiController.class.getResource("layout.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));

        GuiController controller = (GuiController)loader.getController();
        m_adapter = new GuiAdapter(controller, Settings.getGain());

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setWidth(bounds.getWidth() * 0.75);
        primaryStage.setHeight(bounds.getHeight() * 0.75);

        primaryStage.setTitle("Seegie Client");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}

final class Tests
{
    static void networkTest() {
        NetworkManager man = NetworkManager.getInstance();
        try {
            String sessionId = man.reserveSessionId();
            System.out.println("Session id: " + sessionId);
            NetworkAdapter adapter = man.getAdapter(sessionId, true);
            adapter.addListener(cmd -> System.out.println("Cmd received: " + cmd.toString()));
            adapter.open();
            for (int sample = 0; true; ++sample) {
                Random r = new Random();
                byte[] raw = new byte[33];
                r.nextBytes(raw);
                raw[0] = (byte)0xA0;
                raw[32] = (byte)(0xC3 & 0x000000FF);
                EEGData d = new EEGData(raw);
                d.sampleNum = sample;

                adapter.sendData(d);
//                System.out.println("Data sent successfully");
                Thread.sleep(20);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void serialTest() {
        try {
            SerialPort[] ports = SerialPort.getCommPorts();
            SerialPort com1 = ports[0];
            SerialPort com2 = ports[1];

            com1.openPort();
            System.out.println("COM1 open: " + com1.isOpen());
            com2.openPort();
            System.out.println("COM2 open: " + com2.isOpen());
            com2.addDataListener(new SerialPortDataListener()
            {
                @Override
                public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
                @Override
                public void serialEvent(SerialPortEvent event) {
                    System.out.println("Serial event triggered");
                    if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                        return;
                    byte[] newData = new byte[com2.bytesAvailable()];
                    int numRead = com2.readBytes(newData, newData.length);
                    System.out.println("Read " + numRead + " bytes.");
                    for (byte b : newData) {
                        System.out.print(b);
                    }
                }
            });

            com1.writeBytes(new byte[]{ 0, 1, 2, 3, 4, 5 }, 6);

            com1.closePort();
            com2.closePort();

            System.out.println("\nCOM1 open: " + com1.isOpen());
            System.out.println("COM2 open: " + com2.isOpen());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void serializeCmdTest() {
        BCICommand.Builder b = new BCICommand.Builder();
        b.addChanSettingsCmd(3, true, 4, BCICommand.ADSINPUT_NORMAL, false, true, true);
        BCICommand cmd = b.build();

        String json = Serializer.command2Json(cmd);
        System.out.println(json);

        BCICommand newCmd = Serializer.json2Command(json);
        System.out.println("Commands are equal: " + newCmd.equals(cmd));
    }
    static void serializeDataTest() {
        Random r = new Random(15);
        byte[] raw = new byte[33];
        r.nextBytes(raw);
        raw[0] = (byte)0xA0;
        raw[32] = (byte)(0xC0 & 0x000000FF);
        EEGData dd = new EEGData(raw);

        System.out.println("X = " + dd.acclX);
        System.out.println("Y = " + dd.acclY);
        System.out.println("Z = " + dd.acclZ);
        for (int b : dd.channelData) {
            System.out.println(b);
        }
        System.out.println("Sample number = " + dd.sampleNum);
        System.out.println("Timestamp = " + dd.timeStamp);
        System.out.println("Timestamp set = " + dd.timeStampSet);


        String jsonData = Serializer.data2Json(dd);
        System.out.println(jsonData);

        EEGData d = Serializer.json2Data(jsonData);
        System.out.println("X = " + d.acclX);
        System.out.println("Y = " + d.acclY);
        System.out.println("Z = " + d.acclZ);
        for (int b : d.channelData) {
            System.out.println(b);
        }
        System.out.println("Sample number = " + d.sampleNum);
        System.out.println("Timestamp = " + d.timeStamp);
        System.out.println("Timestamp set = " + d.timeStampSet);
    }
}

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

import core.Settings;
import javafx.animation.AnimationTimer;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import models.BCICommand;
import models.DataUnitsAdapter;
import models.EEGData;
import serial.SerialManager;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class GuiController implements Initializable
{
    public interface Listener
    {
        void onCommandCalled(BCICommand cmd);
        void onSeedModeSet(String comPort);
        void onLeechModeSet(String sessionId);
    }

    private Listener m_listener; // NB! Remember nullcheck before calling methods on it!
    private Boolean m_seedMode = null;

    private final ConcurrentLinkedQueue<DataUnitsAdapter> m_dataQ = new ConcurrentLinkedQueue<>();
    private AnimationTimer m_timer;


    private final int MAX_X_POINTS = 1000;
    private       int m_currentX   = 0;

    @FXML
    private GridPane  m_graphPane;
    @FXML
    private TextField m_connectField;
    @FXML
    private Label     m_status;
    @FXML
    private TextArea  m_infoText;
    @FXML
    private Label     m_connectLabel;

    public void setListener(Listener listener) {
        m_listener = listener;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Remove focus from the connect field upon startup
        final BooleanProperty firstTime = new SimpleBooleanProperty(true);
        m_connectField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (firstTime.get()) {
                m_graphPane.requestFocus();
                firstTime.setValue(false);
            }
        });

        // setting up data chart
        NumberAxis xAxis = new NumberAxis(0, MAX_X_POINTS, MAX_X_POINTS / 100);
        xAxis.setForceZeroInRange(false);
        xAxis.setAutoRanging(false);
        xAxis.setTickLabelsVisible(false);
        xAxis.setTickMarkVisible(false);
        xAxis.setMinorTickVisible(false);

        NumberAxis yAxis = new NumberAxis(/*-1d, 1d, 0.1*/);
        yAxis.setAutoRanging(true);

        AreaChart<Number, Number> graph = new AreaChart<Number, Number>(xAxis, yAxis)
        {
            @Override
            protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) { /*empty for performance */}
        };
        graph.setAnimated(false);
        graph.setTitle("EEG data");

        AreaChart.Series<Number, Number> series = new AreaChart.Series<Number, Number>();
        graph.getData().add(series);

        m_graphPane.add(graph, 0, 0);

        m_timer = new AnimationTimer()
        {
            @Override
            public void handle(long now) {
                if (m_dataQ.isEmpty())
                    return;

                DataUnitsAdapter dataPoint = null;
                for (int i = 0; i < 100; ++i) {
                    if (m_dataQ.isEmpty())
                        break;
                    dataPoint = m_dataQ.remove();
                    series.getData().add(new AreaChart.Data(/*dataPoint.getSampleNumber()*/m_currentX, dataPoint.getVoltsData()[0]));
                    System.out.println("X: " + m_currentX + ", Y: " + dataPoint.getVoltsData()[0]);
                }
                System.out.println("---Data removed from Q---");

                int seriesLength = series.getData().size();
                if (seriesLength > MAX_X_POINTS) {
                    series.getData().remove(0, seriesLength - MAX_X_POINTS);
                    xAxis.setLowerBound((int)series.getData().get(0).getXValue());
                    xAxis.setUpperBound(/*dataPoint.getSampleNumber()*/m_currentX);
                }
                m_currentX++;
            }
        };
//        startGraph(); // temp
    }
    public void startGraph() {
//        // BEGIN TEMP
//        ExecutorService executor = Executors.newCachedThreadPool(r -> {
//            Thread thread = new Thread(r);
//            thread.setDaemon(true);
//            return thread;
//        });
//        executor.execute(() -> {
//            try {
//                int gain = Settings.getGain();
//                for (int sample = 0; true; ++sample) {
//
//                    Random r = new Random();
//                    byte[] raw = new byte[33];
//                    r.nextBytes(raw);
//                    raw[0] = (byte)0xA0;
//                    raw[32] = (byte)(0xC3 & 0x000000FF);
//                    EEGData d = new EEGData(raw);
//                    d.sampleNum = sample;
//
//                    m_dataQ.add(new DataUnitsAdapter(d, gain));
//                    Thread.sleep(2);
//                }
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        // END TEMP

        if (m_timer != null)
            m_timer.start();
    }
    public void stopGraph() {
        if (m_timer != null)
            m_timer.stop();
    }
    public void updateData(DataUnitsAdapter data) {
        m_dataQ.add(data);
        System.out.println("---data added to Q ---");
    }
    public void clearData() {
        AreaChart<Number, Number> graph = (AreaChart<Number, Number>)m_graphPane.getChildren().get(0);
        graph.getData().clear();
        m_currentX = 0;
    }
    public void showInfo(String info) {
        m_infoText.setText(m_infoText.getText() + info);
    }
    /**
     * Connect-button handler
     */
    public void onConnectPressed() {
        if (m_seedMode == null) {
            m_status.setText("Error: mode not chosen");
            return;
        }
        if (m_listener != null) {
            String input = m_connectField.getText();
            if (m_seedMode)
                m_listener.onSeedModeSet(input);
            else
                m_listener.onLeechModeSet(input);
            m_status.setText("Connected");
        }
        else {
            m_status.setText("Setup error");
        }
    }
    /**
     * Menu radio check item handler
     */
    public void onSeedModeSelected() {
        m_connectField.setPromptText("Enter serial port name...");
        m_status.setText("Mode: seed");

        String[] ports = SerialManager.getInstance().getPorts();
        m_connectLabel.setText("Ports: " + String.join(", ", ports));

        m_seedMode = true;
    }
    /**
     * Menu radio check item handler
     */
    public void onLeechModeSelected() {
        m_connectField.setPromptText("Enter session id...");
        m_status.setText("Mode: leech");
        m_connectLabel.setText("");

        m_seedMode = false;
    }
    /**
     * Start button handler
     */
    public void onStartPressed() {
        sendCommand(BCICommand.START_STREAM);
    }
    /**
     * Stop button handler
     */
    public void onStopPressed() {
        sendCommand(BCICommand.STOP_STREAM);
    }
    /**
     * Reset handler button
     */
    public void onResetPressed() {
//        clearData();
        sendCommand(BCICommand.RESET);
    }
    private void sendCommand(char simpleCmd) {
        BCICommand.Builder b = new BCICommand.Builder();
        BCICommand cmd = b.addSimpleCmd(simpleCmd).build();
        if (m_listener != null) {
            m_listener.onCommandCalled(cmd);
        }
    }
}

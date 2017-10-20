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
import javafx.scene.Node;
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
import java.util.ArrayList;
import java.util.List;
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

    private final int  MAX_X_POINTS = 1000;
    private       long m_currentX   = 0;

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

        List<AreaChart<Number, Number>> charts = new ArrayList<>();
        for (int row = 0; row < 8; ++row) {
            // setting up data chart
            NumberAxis xAxis = new NumberAxis(0, MAX_X_POINTS, MAX_X_POINTS / 50);
            xAxis.setForceZeroInRange(false);
            xAxis.setAutoRanging(false);
            xAxis.setTickLabelsVisible(false);
            xAxis.setTickMarkVisible(false);
            xAxis.setMinorTickVisible(false);

            NumberAxis yAxis = new NumberAxis();
            yAxis.setAutoRanging(true);

            final AreaChart<Number, Number> graph = new AreaChart<>(xAxis, yAxis)
            {
                @Override
                protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) { /*empty for performance */}
            };
            graph.setAnimated(false);
            graph.setTitle("Channel " + (row + 1));

            final AreaChart.Series<Number, Number> series = new AreaChart.Series<>();
            graph.getData().add(series);
            m_graphPane.add(graph, 0, row);

            charts.add(graph);
        }

        m_timer = new AnimationTimer()
        {
            @Override
            public void handle(long now) {
                if (m_dataQ.isEmpty())
                    return;

                for (int i = 0; i < 100; ++i) {

                    if (m_dataQ.isEmpty())
                        break;

                    DataUnitsAdapter dataPoint = m_dataQ.remove();

                    int channel = 0;
                    for (AreaChart<Number, Number> graph : charts) {
                        AreaChart.Series<Number, Number> series = graph.getData().get(0);
                        series.getData().add(new AreaChart.Data(m_currentX, dataPoint.getVoltsData()[channel++]));
                    }
                    m_currentX++;
                }

                for (AreaChart<Number, Number> graph : charts) {
                    AreaChart.Series<Number, Number> series = graph.getData().get(0);
                    NumberAxis xAxis = (NumberAxis)graph.getXAxis();
                    int seriesLength = series.getData().size();
                    if (seriesLength > MAX_X_POINTS) {
                        series.getData().remove(0, seriesLength - MAX_X_POINTS);
                        xAxis.setLowerBound((long)series.getData().get(0).getXValue());
                        xAxis.setUpperBound(m_currentX - 1);
                    }
                }
            }
        };
    }
    public void startGraph() {
        if (m_timer != null)
            m_timer.start();
    }
    public void stopGraph() {
        if (m_timer != null)
            m_timer.stop();
    }
    public void updateData(DataUnitsAdapter data) {
        m_dataQ.add(data);
    }
    public void clearData() {
        for (Node child : m_graphPane.getChildren()) {
            AreaChart<Number, Number> graph = (AreaChart<Number, Number>)child;
            AreaChart.Series<Number, Number> series = graph.getData().get(0);
            series.getData().clear();
        }
        m_currentX = 0;
    }
    public void showInfo(String info) {
        String infoToShow = /*m_infoText.getText().length() > 500 ? info : */m_infoText.getText() + info;
        m_infoText.setText(infoToShow);
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
            m_infoText.setText("");
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

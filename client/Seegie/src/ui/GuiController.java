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

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.BCICommand;
import models.DataUnitsAdapter;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable
{
    public interface Listener
    {
        void onCommandCalled(BCICommand cmd);
        void onSeedModeSet(String comPort);
        void onLeechModeSet(String sessionId);
    }

    private Listener m_listener; // NB! Remember nullcheck before calling methods on it!

    @FXML
    private TextField m_connectField;
    @FXML
    private AreaChart m_graph;
    @FXML
    private Label     m_status;
    @FXML
    private TextArea  m_infoText;

    public void setListener(Listener listener) {
        m_listener = listener;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        final BooleanProperty firstTime = new SimpleBooleanProperty(true);
        m_connectField.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (firstTime.get()) {
                    m_graph.requestFocus();
                    firstTime.setValue(false);
                }
            }
        });

//        m_gridRoot.add(createTestChart(), 0, 0);

        // Series data of 2014
        XYChart.Series<Number, Number> series2014 = new XYChart.Series<Number, Number>();
        series2014.setName("2014");
        series2014.getData().add(new XYChart.Data<Number, Number>(1, 400));
        series2014.getData().add(new XYChart.Data<Number, Number>(3, 1000));
        series2014.getData().add(new XYChart.Data<Number, Number>(4, 1500));
        series2014.getData().add(new XYChart.Data<Number, Number>(5, 800));
        series2014.getData().add(new XYChart.Data<Number, Number>(7, 500));
        series2014.getData().add(new XYChart.Data<Number, Number>(8, 1800));
        series2014.getData().add(new XYChart.Data<Number, Number>(10, 1500));
        series2014.getData().add(new XYChart.Data<Number, Number>(12, 1300));

        // Series data of 2015
        XYChart.Series<Number, Number> series2015 = new XYChart.Series<Number, Number>();
        series2015.setName("2015");
        series2015.getData().add(new XYChart.Data<Number, Number>(1, 2000));
        series2015.getData().add(new XYChart.Data<Number, Number>(3, 1500));
        series2015.getData().add(new XYChart.Data<Number, Number>(4, 1300));
        series2015.getData().add(new XYChart.Data<Number, Number>(5, 1200));
        series2015.getData().add(new XYChart.Data<Number, Number>(7, 1400));
        series2015.getData().add(new XYChart.Data<Number, Number>(8, 1080));
        series2015.getData().add(new XYChart.Data<Number, Number>(10, 2050));
        series2015.getData().add(new XYChart.Data<Number, Number>(12, 2005));

        m_graph.getData().addAll(series2014, series2015);
    }
    public void updateData(DataUnitsAdapter data) {

    }
    public void clearData() {

    }
    public void showInfo(String info) {
        m_infoText.setText(info);
    }
    /**
     * Connect-button handler
     */
    public void onConnectPressed() {
        m_status.setText("Connecting...");

        // TODO: 18.10.2017
    }
    /**
     * Menu radio check item handler
     */
    public void onSeedModeSelected() {
        m_connectField.setPromptText("Enter session id...");
        m_status.setText("Mode: seed");
        if (m_listener != null) {
            m_listener.onSeedModeSet(m_connectField.getText());
        }
    }
    /**
     * Menu radio check item handler
     */
    public void onLeechModeSelected() {
        m_connectField.setPromptText("Enter serial port name...");
        m_status.setText("Mode: leech");
        if (m_listener != null) {
            m_listener.onLeechModeSet(m_connectField.getText());
        }
    }
//    /**
//     * Chart test
//     */
//    private AreaChart<Number, Number> createTestChart() {
//        final NumberAxis xAxis = new NumberAxis(1, 12, 1);
//        final NumberAxis yAxis = new NumberAxis();
//        final AreaChart<Number, Number> areaChart = new AreaChart<Number, Number>(xAxis, yAxis);
//        areaChart.setTitle("Revenue");
//
//        areaChart.setLegendSide(Side.LEFT);
//
//        // Series data of 2014
//        XYChart.Series<Number, Number> series2014 = new XYChart.Series<Number, Number>();
//        series2014.setName("2014");
//        series2014.getData().add(new XYChart.Data<Number, Number>(1, 400));
//        series2014.getData().add(new XYChart.Data<Number, Number>(3, 1000));
//        series2014.getData().add(new XYChart.Data<Number, Number>(4, 1500));
//        series2014.getData().add(new XYChart.Data<Number, Number>(5, 800));
//        series2014.getData().add(new XYChart.Data<Number, Number>(7, 500));
//        series2014.getData().add(new XYChart.Data<Number, Number>(8, 1800));
//        series2014.getData().add(new XYChart.Data<Number, Number>(10, 1500));
//        series2014.getData().add(new XYChart.Data<Number, Number>(12, 1300));
//
//        // Series data of 2015
//        XYChart.Series<Number, Number> series2015 = new XYChart.Series<Number, Number>();
//        series2015.setName("2015");
//        series2015.getData().add(new XYChart.Data<Number, Number>(1, 2000));
//        series2015.getData().add(new XYChart.Data<Number, Number>(3, 1500));
//        series2015.getData().add(new XYChart.Data<Number, Number>(4, 1300));
//        series2015.getData().add(new XYChart.Data<Number, Number>(5, 1200));
//        series2015.getData().add(new XYChart.Data<Number, Number>(7, 1400));
//        series2015.getData().add(new XYChart.Data<Number, Number>(8, 1080));
//        series2015.getData().add(new XYChart.Data<Number, Number>(10, 2050));
//        series2015.getData().add(new XYChart.Data<Number, Number>(12, 2005));
//
//        areaChart.getData().addAll(series2014, series2015);
//
//        return areaChart;
//    }
}

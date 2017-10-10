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

import javafx.fxml.Initializable;
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

    public void setListener(Listener listener) {
        m_listener = listener;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void updateData(DataUnitsAdapter data) {

    }
    public void clearData() {

    }
    public void showInfo(String info) {

    }
}

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

import web.NetworkManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Config file
 */
public final class Settings
{
    private static final String PROP_WS_URL  = "websocketUrl";
    private static final String PROP_API_URL = "apiUrl";
    private static final String PROP_GAIN    = "gain";

    private static final Properties s_props = new Properties();
    static {
        try {
            // try read config
            String path = NetworkManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath() + "\\config.txt";
            FileInputStream fin = new FileInputStream(path);
            s_props.load(fin);
        }
        catch (Exception e) {
            // create config if none found
            s_props.setProperty(PROP_WS_URL, "ws://seegieapi.azurewebsites.net?");
            s_props.setProperty(PROP_API_URL, "http://seegieapi.azurewebsites.net/");
            s_props.setProperty(PROP_GAIN, "24");
            try {
                String path = NetworkManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath() + "\\config.txt";
                FileOutputStream fout = new FileOutputStream(path);
                s_props.store(fout, "Default config");
            }
            catch (Exception ex) { /* Who cares? */ }
        }
    }
    public static String getWsUrl() {
        return s_props.getProperty(PROP_WS_URL);
    }
    public static String getApiUrl() {
        return s_props.getProperty(PROP_API_URL);
    }
    public static int getGain() {
        String gain = s_props.getProperty(PROP_GAIN);
        return Integer.parseInt(gain);
    }
}

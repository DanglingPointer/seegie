/*
 *     Copyright (C) 2017-2018  Mikhail Vasilyev
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
    private static final String PROP_UDP_URL = "udpUrl";
    private static final String PROP_GAIN    = "gain";

    private static final Properties s_props = new Properties();
    static {
        try {
            // try read config
            String path = NetworkManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath() + "config.txt";
            FileInputStream fin = new FileInputStream(path);
            s_props.load(fin);
        }
        catch (Exception e) {
            // create config if none found
            s_props.setProperty(PROP_WS_URL, "ws://seegieapi.azurewebsites.net?");
            s_props.setProperty(PROP_API_URL, "http://seegieapi.azurewebsites.net/");
            s_props.setProperty(PROP_UDP_URL, "udp://seegieapi.azurewebsites.net:15010");
            s_props.setProperty(PROP_GAIN, "24");
            try {
                String path = NetworkManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath() + "config.txt";
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
    public static String getUdpUrl() {
        return s_props.getProperty(PROP_UDP_URL);
    }
    public static int getGain() {
        String gain = s_props.getProperty(PROP_GAIN);
        return Integer.parseInt(gain);
    }
}

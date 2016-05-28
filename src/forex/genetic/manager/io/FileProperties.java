/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author ricardorq85
 */
public class FileProperties {

    private static Properties properties = null;

    public static void load() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream("Forex.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPropertyString(String key) {
        return properties.getProperty(key);
    }

    public static int getPropertyInt(String key) {
        return Integer.parseInt(getPropertyString(key));
    }

    public static double getPropertyDouble(String key) {
        return Double.parseDouble(getPropertyString(key));
    }
}

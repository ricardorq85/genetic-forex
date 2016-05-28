/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author ricardorq85
 */
public class FileProperties {

    private static Properties properties = new Properties();

    public synchronized static void load() {
        try {
            properties.clear();
            InputStream is = new FileInputStream("Forex.properties");
            properties.load(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Properties getProperties() {
        return properties;
    }

    public static synchronized void setProperties(Properties properties) {
        FileProperties.properties = properties;
    }

    public static synchronized String getPropertyString(String key) {
        return properties.getProperty(key);
    }

    public static synchronized int getPropertyInt(String key) {
        return Integer.parseInt(getPropertyString(key));
    }

    public static synchronized double getPropertyDouble(String key) {
        return Double.parseDouble(getPropertyString(key));
    }
}

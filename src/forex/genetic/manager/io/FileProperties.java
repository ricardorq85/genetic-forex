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

    /**
     *
     */
    public synchronized static void load() {
        try {
            properties.clear();
            try (InputStream is = new FileInputStream("Forex.properties")) {
                properties.load(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public static synchronized Properties getProperties() {
        return properties;
    }

    /**
     *
     * @param properties
     */
    public static synchronized void setProperties(Properties properties) {
        FileProperties.properties = properties;
    }

    /**
     *
     * @param key
     * @return
     */
    public static synchronized String getPropertyString(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("key not found: " + key);
        }
        return value;
    }

    /**
     *
     * @param key
     * @return
     */
    public static synchronized int getPropertyInt(String key) {
        return Integer.parseInt(getPropertyString(key));
    }

    /**
     *
     * @param key
     * @return
     */
    public static synchronized double getPropertyDouble(String key) {
        return Double.parseDouble(getPropertyString(key));
    }

    /**
     *
     * @return
     */
    public static String propertiesString() {
        return properties.toString();
    }
    
}

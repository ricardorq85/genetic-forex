/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.manager.io.FileProperties;
import forex.genetic.util.Constants;
import forex.genetic.util.Constants.FortalezaType;
import forex.genetic.util.Constants.OperationType;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class PropertiesManager {

    private static Thread t = null;

    public PropertiesManager() {
    }

    public static Thread load() {
        t = new Thread() {

            public void run() {
                FileProperties.load();
                LogUtil.logTime(FileProperties.propertiesString(), 1);
            }
        };
        t.start();
        return t;
    }

    public static OperationType getOperationType() {
        try {
            if (FileProperties.getProperties().isEmpty()) {
                t.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        OperationType ot = null;
        String s = getPropertyString(Constants.OPERATION_TYPE);
        if (s.contains("Sell")) {
            ot = OperationType.Sell;
        } else if (s.contains("Buy")) {
            ot = OperationType.Buy;
        }
        return ot;
    }

    public static FortalezaType getFortalezaType() {
        try {
            if (FileProperties.getProperties().isEmpty()) {
                t.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        FortalezaType t = null;
        String s = getPropertyString(Constants.FORTALEZA_TYPE);
        if (s.contains("Stable")) {
            t = Constants.FortalezaType.Stable;
        } else if (s.contains("Pips")) {
            t = Constants.FortalezaType.Pips;
        } else if (s.contains("Embudo")) {
            t = Constants.FortalezaType.Embudo;
        }
        return t;
    }

    public static int getPointsControl() {
        try {
            if (FileProperties.getProperties().isEmpty()) {
                t.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        int points = 1;
        String s = getPropertyString(Constants.POINTS_CONTROL);
        if (s.contains("Integer.MAX_VALUE")) {
            points = Integer.MAX_VALUE;
        } else if (s.contains("Integer.MAX_VALUE")) {
            points = Integer.MIN_VALUE;
        } else {
            points = getPropertyInt(Constants.POINTS_CONTROL);
        }
        return points;
    }

    public static String getPropertyString(String key) {
        try {
            if (FileProperties.getProperties().isEmpty()) {
                t.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return FileProperties.getPropertyString(key);
    }

    public static int getPropertyInt(String key) {
        try {
            if (FileProperties.getProperties().isEmpty()) {
                t.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return Integer.parseInt(FileProperties.getPropertyString(key));
    }

    public static long getPropertyLong(String key) {
        try {
            if (FileProperties.getProperties().isEmpty()) {
                t.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return Long.parseLong(FileProperties.getPropertyString(key));
    }

    public static boolean getPropertyBoolean(String key) {
        try {
            if (FileProperties.getProperties().isEmpty()) {
                t.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return Boolean.parseBoolean(FileProperties.getPropertyString(key));
    }

    public static double getPropertyDouble(String key) {
        try {
            if (FileProperties.getProperties().isEmpty()) {
                t.join();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return Double.parseDouble(FileProperties.getPropertyString(key));
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.manager.io.FileProperties;
import forex.genetic.util.Constants;
import forex.genetic.util.Constants.FortalezaType;
import forex.genetic.util.Constants.OperationType;

/**
 *
 * @author ricardorq85
 */
public class PropertiesManager {

    public PropertiesManager() {
    }

    public static void load() {
        FileProperties.load();
    }

    public static OperationType getOperationType() {
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
        FortalezaType t = null;
        String s = getPropertyString(Constants.FORTALEZA_TYPE);
        if (s.contains("Stable")) {
            t = Constants.FortalezaType.Stable;
        } else if (s.contains("Pips")) {
            t = Constants.FortalezaType.Pips;
        }
        return t;
    }

    public static int getPointsControl() {
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
        return FileProperties.getPropertyString(key);
    }

    public static int getPropertyInt(String key) {
        return Integer.parseInt(FileProperties.getPropertyString(key));
    }

    public static boolean getPropertyBoolean(String key) {
        return Boolean.parseBoolean(FileProperties.getPropertyString(key));
    }

    public static double getPropertyDouble(String key) {
        return Double.parseDouble(FileProperties.getPropertyString(key));
    }
}

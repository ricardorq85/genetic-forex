/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.util;

import java.util.Date;

import forex.genetic.manager.PropertiesManager;

/**
 *
 * @author ricardorq85
 */
public class LogUtil {

    /**
     *
     * @param name
     * @param logLevel
     */
    public static void logTime(String name, int logLevel) {
        LogUtil.logTime(name, logLevel, 0);
    }

    /**
     *
     * @param name
     * @param logLevel
     * @param tabLevel
     */
    public static void logTime(String name, int logLevel, int tabLevel) {
        if (logLevel <= PropertiesManager.getPropertyInt(Constants.LOG_LEVEL)) {
            StringBuilder buffer = new StringBuilder();
            //buffer.append("<log> ");
            buffer.append(name);
            buffer.append(" <- ");
            buffer.append(DateUtil.getDateString(new Date()));
            for (int i = 0; i < tabLevel; i++) {
                buffer.append("\t");
            }
            System.out.println(buffer.toString());
        }
    }
    
    public static void logAvance(String c, int logLevel) {
        if (logLevel <= PropertiesManager.getPropertyInt(Constants.LOG_LEVEL)) {
            StringBuilder buffer = new StringBuilder();
            buffer.append(c);
            System.out.print(buffer.toString());
        }
    	
    }
    
    public static void logAvance(int logLevel) {
    	logAvance(".", logLevel);
    }

    public static void logEnter(int logLevel) {
        if (logLevel <= PropertiesManager.getPropertyInt(Constants.LOG_LEVEL)) {
            System.out.println("");
        }
    }

}

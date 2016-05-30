/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.util;

import forex.genetic.manager.PropertiesManager;
import java.util.Date;

/**
 *
 * @author ricardorq85
 */
public class LogUtil {

    public static void logTime(String name, int logLevel) {
        LogUtil.logTime(name, logLevel, 10);
    }

    public static void logTime(String name, int logLevel, int tabLevel) {
        if (logLevel <= PropertiesManager.getPropertyInt(Constants.LOG_LEVEL)) {
            StringBuilder buffer = new StringBuilder();
            buffer.append("<log> ");
            buffer.append(name);
            buffer.append(" <-- ");
            buffer.append(DateUtil.getDateString(new Date()));
            for (int i = 0; i < tabLevel; i++) {
                buffer.append("\t");
            }
            System.out.println(buffer.toString());
        }
    }
}

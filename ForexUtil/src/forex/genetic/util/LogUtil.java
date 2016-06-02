/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.util;

import java.util.Date;

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
        LogUtil.logTime(name, logLevel, 10, 5);
    }

    /**
     *
     * @param name
     * @param logLevel
     * @param tabLevel
     */
    public static void logTime(String name, int logLevel, int tabLevel, int configuredLogLevel) {
        if (logLevel <= configuredLogLevel) {
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

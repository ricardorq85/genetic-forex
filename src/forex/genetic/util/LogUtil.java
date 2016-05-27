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

    public static void logTime(String name) {
        System.out.println("<log> " + name + ":" + new Date());
    }
}

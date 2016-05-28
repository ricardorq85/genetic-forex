/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricardorq85
 */
public class ThreadUtil {

    public static void joinThread(Thread t) {
        if ((t != null) && (t.isAlive())) {
            try {                
                LogUtil.logTime("Init Waiting for Thread " + t.getName(), 1);
                t.join();
                LogUtil.logTime("End Waiting for Thread " + t.getName(), 1);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}

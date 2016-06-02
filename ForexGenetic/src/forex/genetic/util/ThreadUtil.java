/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.util;

import forex.genetic.manager.PropertiesManager;

/**
 *
 * @author ricardorq85
 */
public class ThreadUtil {
        
    /**
     *
     * @param t
     */
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

    /**
     *
     * @param t
     */
    public static void launchThread(Thread t) {
        if (PropertiesManager.isThread()) {
            t.start();
        } else {
            t.run();
        }
    }
}

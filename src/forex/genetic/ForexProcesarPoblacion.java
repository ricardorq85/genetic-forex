/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import forex.genetic.delegate.GeneticTesterDelegate;
import forex.genetic.delegate.PoblacionDelegate;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 *
 * @author ricardorq85
 */
public class ForexProcesarPoblacion {

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        long id = System.currentTimeMillis();
        PropertiesManager.load().join();
        LogUtil.logTime("ForexProcesarPoblacion: " + id, 1);
        GeneticTesterDelegate.id = "" + id;
        PrintStream out = new PrintStream(PropertiesManager.getPropertyString(Constants.LOG_PATH) + "ProcesarPoblacion_" + id + ".log");
        System.setOut(out);
        System.setErr(out);
        PoblacionDelegate delegate = new PoblacionDelegate();
        //delegate.procesarPoblacion();
        
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import forex.genetic.delegate.GeneticDelegate;
import forex.genetic.delegate.GeneticDelegateBD;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author ricardorq85
 */
public class ForexGeneticBD {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        long id = System.currentTimeMillis();
        PropertiesManager.load().join();
        LogUtil.logTime("ForexGeneticBD ProcesarOperaciones: " + id, 1);
        String name = PropertiesManager.getPropertyString(Constants.LOG_PATH)
                + "ProcesarOperaciones_" + id + "_log.log";
        PrintStream out = new PrintStream(name);
        System.setOut(out);
        System.setErr(out);
        LogUtil.logTime("Inicio: " + id, 1);
        GeneticDelegate.id = Long.toString(id);
        GeneticDelegateBD delegate = new GeneticDelegateBD();
        delegate.process();
        delegate.getFileOutManager().close();
        LogUtil.logTime("Fin: " + id, 1);
    }
}

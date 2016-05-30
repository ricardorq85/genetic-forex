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
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author ricardorq85
 */
public class ForexInsertDatosHistoricos {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        long id = System.currentTimeMillis();
        PropertiesManager.load().join();
        LogUtil.logTime("ForexInsertDatosHistoricos: " + id, 1);
        GeneticTesterDelegate.id = "" + id;
        PrintStream out = new PrintStream(PropertiesManager.getPropertyString(Constants.LOG_PATH) + "InsertDatosHistoricos_" + PropertiesManager.getOperationType() + PropertiesManager.getPair() + id + ".log");
        System.setOut(out);
        System.setErr(out);
        PoblacionDelegate delegate = new PoblacionDelegate();
        LogUtil.logTime("Init Insert Datos Historicos", 1);
        delegate.cargarDatosHistoricos();
        LogUtil.logTime("End Insert Datos Historicos", 1);
    }
}

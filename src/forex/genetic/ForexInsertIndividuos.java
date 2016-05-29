/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import forex.genetic.delegate.GeneticTesterDelegate;
import forex.genetic.delegate.PoblacionDelegate;
import forex.genetic.entities.Learning;
import forex.genetic.entities.Poblacion;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.io.SerializationPoblacionManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author ricardorq85
 */
public class ForexInsertIndividuos {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        long id = System.currentTimeMillis();
        PropertiesManager.load().join();
        LogUtil.logTime("ForexInsertIndividuos: " + id, 1);
        GeneticTesterDelegate.id = "" + id;
        PrintStream out = new PrintStream(PropertiesManager.getPropertyString(Constants.LOG_PATH) + "InsertIndividuos_" + PropertiesManager.getOperationType() + PropertiesManager.getPair() + id + ".log");
        System.setOut(out);
        System.setErr(out);
        SerializationPoblacionManager serializationManager = new SerializationPoblacionManager();
        String serPath = PropertiesManager.getSerialicePath();
        Poblacion poblacion = null;
        PoblacionDelegate delegate = new PoblacionDelegate();
        for (int i = 0; i < 100000; i++) {
            LogUtil.logTime("Init Insert Poblacion i=" + i, 1);
            poblacion = serializationManager.readAll(serPath, 1000, -1, -1, new Learning());
            delegate.cargarPoblacion(poblacion);
            LogUtil.logTime("End Insert Poblacion i=" + i, 1);
        }
    }
}

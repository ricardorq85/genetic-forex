/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import static forex.genetic.delegate.GeneticDelegate.setId;
import static forex.genetic.manager.PropertiesManager.getOperationType;
import static forex.genetic.manager.PropertiesManager.getPair;
import static forex.genetic.manager.PropertiesManager.getPropertyString;
import static forex.genetic.manager.PropertiesManager.getSerialicePath;
import static forex.genetic.manager.PropertiesManager.load;
import static forex.genetic.util.Constants.LOG_PATH;
import static forex.genetic.util.LogUtil.logTime;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.setErr;
import static java.lang.System.setOut;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import forex.genetic.delegate.PoblacionDelegate;
import forex.genetic.entities.Learning;
import forex.genetic.entities.Poblacion;
import forex.genetic.manager.io.SerializationPoblacionManager;

/**
 *
 * @author ricardorq85
 */
public class ForexInsertIndividuos {

    /**
     *
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        long id = currentTimeMillis();
        load().join();
        logTime("ForexInsertIndividuos: " + id, 1);
        setId("" + id);
        StringBuilder name = new StringBuilder();
        name.append(getPropertyString(LOG_PATH)).append("InsertIndividuos_");
        name.append(getOperationType()).append(getPair()).append(id).append(".log");
        PrintStream out = new PrintStream(name.toString(), Charset.defaultCharset().name());
        setOut(out);
        setErr(out);
        SerializationPoblacionManager serializationManager = new SerializationPoblacionManager();
        String serPath = getSerialicePath();
        Poblacion poblacion = null;
        PoblacionDelegate delegate = new PoblacionDelegate();
        for (int i = 0; i < 100_000; i++) {
            logTime("Init Insert Poblacion i=" + i, 1);
            poblacion = serializationManager.readAll(serPath, 1_000, -1, -1, new Learning());
            delegate.cargarPoblacion(poblacion);
            logTime("End Insert Poblacion i=" + i, 1);
        }
    }
    private static final Logger LOG = Logger.getLogger(ForexInsertIndividuos.class.getName());
}

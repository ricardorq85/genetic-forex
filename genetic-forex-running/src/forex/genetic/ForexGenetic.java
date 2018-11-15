/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import static forex.genetic.delegate.GeneticDelegate.setId;
import static forex.genetic.manager.PropertiesManager.getOperationType;
import static forex.genetic.manager.PropertiesManager.getPair;
import static forex.genetic.manager.PropertiesManager.getPropertyString;
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

import forex.genetic.delegate.GeneticDelegate;
import forex.genetic.entities.Poblacion;

/**
 *
 * @author ricardorq85
 */
public class ForexGenetic {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        long id = currentTimeMillis();
        load().join();
        logTime("ForexGenetic: " + id, 1);
        StringBuilder name = new StringBuilder(getPropertyString(LOG_PATH));
        name.append(getOperationType()).append(getPair()).append(id).append("_log.log");
        PrintStream out = new PrintStream(name.toString(), Charset.defaultCharset().name());
        setOut(out);
        setErr(out);
        logTime("Inicio: " + id, 1);
        setId(Long.toString(id));
        GeneticDelegate delegate = new GeneticDelegate();
        Poblacion poblacion = delegate.process();
        delegate.getFileOutManager().close();
        logTime("Fin: " + id, 1);
    }
    private static final Logger LOG = Logger.getLogger(ForexGenetic.class.getName());
}

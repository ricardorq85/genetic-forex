/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import static forex.genetic.delegate.GeneticDelegate.setId;
import static forex.genetic.manager.PropertiesManager.getPropertyString;
import static forex.genetic.manager.PropertiesManager.load;
import static forex.genetic.util.Constants.LOG_PATH;
import static forex.genetic.util.LogUtil.logTime;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.setErr;
import static java.lang.System.setOut;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import forex.genetic.delegate.PoblacionDelegate;

/**
 *
 * @author ricardorq85
 */
public class ForexProcesarPoblacion {

    /**
     *
     * @param args
     * @throws InterruptedException
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
        long id = currentTimeMillis();
        load().join();
        logTime("ForexProcesarPoblacion: " + id, 1);
        setId("" + id);
        String name = getPropertyString(LOG_PATH) + "ProcesarPoblacion_" + id + ".log";
        PrintStream out = new PrintStream(name, Charset.defaultCharset().name());
        setOut(out);
        setErr(out);
        PoblacionDelegate delegate = new PoblacionDelegate();
        //delegate.procesarPoblacion();
        
    }
    private static final Logger LOG = Logger.getLogger(ForexProcesarPoblacion.class.getName());
}

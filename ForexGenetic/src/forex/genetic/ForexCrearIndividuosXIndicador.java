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

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.logging.Logger;

import forex.genetic.manager.IndividuoXIndicadorManager;

/**
 *
 * @author ricardorq85
 */
public class ForexCrearIndividuosXIndicador {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, SQLException {
        long id = currentTimeMillis();
        load().join();
        logTime("ForexCrearIndividuosXIndicador.java: " + id, 1);
        String name = getPropertyString(LOG_PATH)
                + "CrearIndividuosXIndicador_"
                + id + ".log";
        PrintStream out = new PrintStream(name, Charset.defaultCharset().name());
        setOut(out);
        setErr(out);
        logTime("Inicio: " + id, 1);
        setId(Long.toString(id));
        IndividuoXIndicadorManager manager = new IndividuoXIndicadorManager();
        manager.crearIndividuos();
        logTime("Fin: " + id, 1);
    }
}

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

import forex.genetic.manager.BorradoDuplicadosManager;
import forex.genetic.manager.BorradoManager;

/**
 *
 * @author ricardorq85
 */
public class ForexBorrarDuplicados {

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, SQLException {
        long id = currentTimeMillis();
        load().join();
        logTime("ForexBorrarDuplicados: " + id, 1);
        String name = getPropertyString(LOG_PATH)
                + "BorrarDuplicados_"
                + id + "_log.log";
        PrintStream out = new PrintStream(name, Charset.defaultCharset().name());
        setOut(out);
        setErr(out);
        logTime("Inicio: " + id, 1);
        setId(Long.toString(id));
        BorradoManager manager = new BorradoDuplicadosManager();
        manager.borrarIndividuos();
        logTime("Fin: " + id, 1);
    }
    private static final Logger LOG = Logger.getLogger(ForexBorrarDuplicados.class.getName());
}

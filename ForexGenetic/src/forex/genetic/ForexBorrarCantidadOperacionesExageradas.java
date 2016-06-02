/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import static forex.genetic.delegate.GeneticDelegate.setId;
import forex.genetic.manager.BorradoCantidadOperacionesExageradasManager;
import forex.genetic.manager.BorradoManager;
import static forex.genetic.manager.PropertiesManager.getPropertyString;
import static forex.genetic.manager.PropertiesManager.load;
import static forex.genetic.util.Constants.LOG_PATH;
import static forex.genetic.util.LogUtil.logTime;
import java.io.IOException;
import java.io.PrintStream;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.setErr;
import static java.lang.System.setOut;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 *
 * @author ricardorq85
 */
public class ForexBorrarCantidadOperacionesExageradas {

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, SQLException {
        long id = currentTimeMillis();
        load().join();
        logTime("ForexBorrarCantidadOperacionesExageradas: " + id, 1);
        String name = getPropertyString(LOG_PATH)
                + "BorrarCantidadOperacionesExageradas_"
                + id + ".log";
        PrintStream out = new PrintStream(name, Charset.defaultCharset().name());
        setOut(out);
        setErr(out);
        logTime("Inicio: " + id, 1);
        setId(Long.toString(id));
        BorradoManager manager = new BorradoCantidadOperacionesExageradasManager();
        manager.borrarIndividuos();
        logTime("Fin: " + id, 1);
    }
    private static final Logger LOG = Logger.getLogger(ForexBorrarCantidadOperacionesExageradas.class.getName());
}

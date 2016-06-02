/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import static forex.genetic.delegate.GeneticDelegate.setId;
import forex.genetic.exception.GeneticException;
import static forex.genetic.manager.PropertiesManager.getPropertyString;
import static forex.genetic.manager.PropertiesManager.load;
import forex.genetic.manager.TendenciaGeneticaManager;
import static forex.genetic.util.Constants.LOG_PATH;
import static forex.genetic.util.LogUtil.logTime;
import java.io.IOException;
import java.io.PrintStream;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.setErr;
import static java.lang.System.setOut;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Logger;

/**
 *
 * @author ricardorq85
 */
public class TendenciaGenetica {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InterruptedException
     * @throws java.text.ParseException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, ParseException {
        long id = currentTimeMillis();
        load().join();
        logTime("TendenciaGenetica: " + id, 1);
        String name = getPropertyString(LOG_PATH)
                + "TendenciaGenetica" + id + "_log.log";
        PrintStream out = new PrintStream(name, Charset.defaultCharset().name());
        setOut(out);
        setErr(out);
        logTime("Inicio: " + id, 1);
        setId(Long.toString(id));
        try {
            TendenciaGeneticaManager manager = new TendenciaGeneticaManager();
            manager.procesarGenetica();
        } catch (SQLException | GeneticException ex) {
            ex.printStackTrace();
        }
        logTime("Fin: " + id, 1);
    }
    private static final Logger LOG = Logger.getLogger(TendenciaGenetica.class.getName());
}

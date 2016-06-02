/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import static forex.genetic.delegate.GeneticDelegate.setId;
import forex.genetic.manager.IndividuosOptimosManager;
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
import java.text.ParseException;
import java.util.logging.Logger;

/**
 *
 * @author ricardorq85
 */
public class ForexIndividuosOptimos {

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, ParseException {
        long id = currentTimeMillis();
        load().join();
        logTime("ForexIndividuoOptimos: " + id, 1);
        StringBuilder name = new StringBuilder(getPropertyString(LOG_PATH));
        name.append("IndividuosOptimos_").append(id).append("_log.log");
        PrintStream out = new PrintStream(name.toString(), Charset.defaultCharset().name());
        setOut(out);
        setErr(out);
        logTime("Inicio: " + id, 1);
        setId(Long.toString(id));
        for (int i = 0; i < 3; i++) {
            try {
                IndividuosOptimosManager manager = new IndividuosOptimosManager();
                manager.obtenerIndividuosOptimos();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        logTime("Fin: " + id, 1);
    }
    private static final Logger LOG = Logger.getLogger(ForexIndividuosOptimos.class.getName());
}

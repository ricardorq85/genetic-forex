/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import forex.genetic.delegate.GeneticDelegate;
import forex.genetic.manager.IndividuosOptimosManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.text.ParseException;

/**
 *
 * @author ricardorq85
 */
public class ForexIndividuosOptimos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, ParseException {
        long id = System.currentTimeMillis();
        PropertiesManager.load().join();
        LogUtil.logTime("ForexIndividuoOptimos: " + id, 1);
        String name = PropertiesManager.getPropertyString(Constants.LOG_PATH)
                + "IndividuosOptimos_" + id + "_log.log";
        PrintStream out = new PrintStream(name);
        System.setOut(out);
        System.setErr(out);
        LogUtil.logTime("Inicio: " + id, 1);
        GeneticDelegate.id = Long.toString(id);
        for (int i = 0; i < 3; i++) {
            try {
                IndividuosOptimosManager manager = new IndividuosOptimosManager();
                manager.obtenerIndividuosOptimos();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        LogUtil.logTime("Fin: " + id, 1);
    }
}

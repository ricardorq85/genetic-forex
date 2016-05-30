/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import forex.genetic.delegate.GeneticDelegate;
import forex.genetic.exception.GeneticException;
import forex.genetic.manager.ProcesarTendenciasMaxMinManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricardorq85
 */
public class ForexProcesarTendenciasMaxMin {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, ParseException {
        long id = System.currentTimeMillis();
        PropertiesManager.load().join();
        LogUtil.logTime("ForexProcesarTendenciasMaxMin: " + id, 1);
        String name = PropertiesManager.getPropertyString(Constants.LOG_PATH)
                + "ProcesarTendenciasMaxMin_" + id + "_log.log";
        PrintStream out = new PrintStream(name);
        System.setOut(out);
        System.setErr(out);
        LogUtil.logTime("Inicio: " + id, 1);
        GeneticDelegate.id = Long.toString(id);
        try {
            ProcesarTendenciasMaxMinManager manager = new ProcesarTendenciasMaxMinManager();
            manager.procesarTendencias();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (GeneticException ex) {
            ex.printStackTrace();
        }
        LogUtil.logTime("Fin: " + id, 1);
    }
}

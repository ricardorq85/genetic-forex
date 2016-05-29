/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import forex.genetic.delegate.GeneticDelegate;
import forex.genetic.manager.ProcesarTendenciasManager;
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
public class ForexProcesarTendencias {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, ParseException {
        long id = System.currentTimeMillis();
        PropertiesManager.load().join();
        LogUtil.logTime("ForexProcesarTendencias: " + id, 1);
        String name = PropertiesManager.getPropertyString(Constants.LOG_PATH)
                + "ProcesarTendencias_" + id + "_log.log";
        PrintStream out = new PrintStream(name);
        System.setOut(out);
        System.setErr(out);
        LogUtil.logTime("Inicio: " + id, 1);
        GeneticDelegate.id = Long.toString(id);
        try {
            ProcesarTendenciasManager manager = new ProcesarTendenciasManager();
            manager.procesarTendencias();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        LogUtil.logTime("Fin: " + id, 1);
    }
}

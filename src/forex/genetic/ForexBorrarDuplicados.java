/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import forex.genetic.delegate.GeneticDelegate;
import forex.genetic.manager.DuplicadosManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;

/**
 *
 * @author ricardorq85
 */
public class ForexBorrarDuplicados {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, SQLException {
        long id = System.currentTimeMillis();
        PropertiesManager.load().join();
        LogUtil.logTime("ForexBorrarDuplicados: " + id, 1);
        String name = PropertiesManager.getPropertyString(Constants.LOG_PATH)
                + "BorrarDuplicados_"
                + id + "_log.log";
        PrintStream out = new PrintStream(name);
        System.setOut(out);
        System.setErr(out);
        LogUtil.logTime("Inicio: " + id, 1);
        GeneticDelegate.id = Long.toString(id);
        DuplicadosManager manager = new DuplicadosManager();
        manager.borrarDuplicados("DUPLICADO_OPERACIONES");
        LogUtil.logTime("Fin: " + id, 1);
    }
}

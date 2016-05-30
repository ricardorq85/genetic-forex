/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import forex.genetic.delegate.GeneticDelegate;
import forex.genetic.exception.GeneticException;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.TendenciaGeneticaManager;
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
public class TendenciaGenetica {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InterruptedException
     * @throws java.text.ParseException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, ParseException {
        long id = System.currentTimeMillis();
        PropertiesManager.load().join();
        LogUtil.logTime("TendenciaGenetica: " + id, 1);
        String name = PropertiesManager.getPropertyString(Constants.LOG_PATH)
                + "TendenciaGenetica" + id + "_log.log";
        PrintStream out = new PrintStream(name);
        System.setOut(out);
        System.setErr(out);
        LogUtil.logTime("Inicio: " + id, 1);
        GeneticDelegate.id = Long.toString(id);
        try {
            TendenciaGeneticaManager manager = new TendenciaGeneticaManager();
            manager.procesarGenetica();
        } catch (SQLException | GeneticException ex) {
            ex.printStackTrace();
        }
        LogUtil.logTime("Fin: " + id, 1);
    }
}

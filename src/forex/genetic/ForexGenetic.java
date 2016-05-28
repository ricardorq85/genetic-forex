/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import forex.genetic.entities.Poblacion;
import forex.genetic.delegate.GeneticDelegate;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author ricardorq85
 */
public class ForexGenetic {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        long id = System.currentTimeMillis();
        PropertiesManager.load();
        PrintStream out = new PrintStream(PropertiesManager.getPropertyString(Constants.LOG_PATH) + PropertiesManager.getOperationType() + PropertiesManager.getPropertyString(Constants.PAIR) + id + ".log");
        System.setOut(out);
        System.setErr(out);
        GeneticDelegate delegate = new GeneticDelegate();
        GeneticDelegate.id = Long.toString(id);
        Poblacion poblacion = delegate.process();
    }
}

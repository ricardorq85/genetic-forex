/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import forex.genetic.entities.Poblacion;
import forex.genetic.delegate.GeneticDelegate;
import forex.genetic.util.Constants;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author ricardorq85
 * TODO: Recalcular estrategia para actuar en el "futuro"
 */
public class ForexGenetic {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        long id = System.currentTimeMillis();
        java.io.
        PrintStream out = new PrintStream(Constants.LOG_PATH + id + ".log");
        System.setOut(out);
        System.setErr(out);
        GeneticDelegate delegate = new GeneticDelegate(id);
        Poblacion poblacion = delegate.process(Constants.POBLACION_COUNTER);
    }
}

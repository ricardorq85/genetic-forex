/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import forex.genetic.entities.Poblacion;
import forex.genetic.delegate.GeneticDelegate;
import forex.genetic.entities.indicator.Average;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.indicator.Macd;
import forex.genetic.entities.indicator.Sar;
import forex.genetic.util.Constants;
import forex.genetic.util.NumberUtil;
import java.util.List;

/**
 *
 * @author ricardorq85
 * TODO: Recalcular estrategia para actuar en el "futuro"
 */
public class ForexGenetic {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GeneticDelegate delegate = new GeneticDelegate();
        Poblacion poblacion = delegate.process();
        //delegate.outPoblacion(poblacion);


        /*GeneticTesterDelegate testerDelegate = new GeneticTesterDelegate();
        testerDelegate.process(poblacion.getFirst());
        delegate.outPoblacion(poblacion.getFirst());*/
    }
}

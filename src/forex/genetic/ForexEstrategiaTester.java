/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import forex.genetic.entities.Average;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.delegate.GeneticTesterDelegate;
import forex.genetic.entities.Indicator;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Macd;
import java.util.List;
import java.util.Vector;
import static forex.genetic.util.Constants.*;

/**
 *
 * @author ricardorq85
 */
public class ForexEstrategiaTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        IndividuoEstrategia individuoEstrategia = new IndividuoEstrategia();
        individuoEstrategia.setTakeProfit(875);
        individuoEstrategia.setStopLoss(638);
        individuoEstrategia.setLot(1);
        List<Indicator> indicators = new Vector<Indicator>(INDICATOR_NUMBER);
        Average average = new Average();
        average.setAverage(0.0);
        average.setInterval(new Interval(-1.4E-4, 0.65712));
        indicators.add(average);

        Macd macd = new Macd();
        macd.setMacdValue(0.0);
        macd.setMacdSignal(0.0);
        macd.setInterval(new Interval(-5.34E-6, -2.35E-5));
        indicators.add(macd);

        individuoEstrategia.setOpenIndicators(indicators);

        GeneticTesterDelegate delegate = new GeneticTesterDelegate();
        delegate.process(individuoEstrategia);

        System.out.println(individuoEstrategia.toString());
    }
}

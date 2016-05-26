/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import forex.genetic.entities.indicator.Average;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.delegate.GeneticTesterDelegate;
import forex.genetic.entities.Indicator;
import forex.genetic.entities.Interval;
import forex.genetic.entities.indicator.Macd;
import forex.genetic.entities.indicator.Sar;
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
        individuoEstrategia.setTakeProfit(490);
        individuoEstrategia.setStopLoss(497);
        individuoEstrategia.setLot(1);
        individuoEstrategia.setInitialBalance(3000);
        List<Indicator> indicators = new Vector<Indicator>(INDICATOR_NUMBER);

        Average average = new Average();
        average.setAverage(0.0);
        average.setInterval(new Interval(-0.00464, 0.00336));
        indicators.add(null);

        Macd macd = new Macd();
        macd.setMacdValue(0.0);
        macd.setMacdSignal(0.0);
        macd.setInterval(new Interval(-0.00002, 0.000001));
        indicators.add(macd);

        average = new Average();
        average.setAverage(0.0);
        average.setInterval(new Interval(-0.00639, 0.00006));
        indicators.add(null);

        Sar sar = new Sar();
        sar.setSar(0.0);
        sar.setInterval(new Interval(-0.00047, 0.00127));
        indicators.add(null);

        individuoEstrategia.setOpenIndicators(indicators);

        indicators = new Vector<Indicator>(INDICATOR_NUMBER);

        average = new Average();
        average.setAverage(0.0);
        average.setInterval(new Interval(0.00272, 0.00305));
        indicators.add(average);

        macd = new Macd();
        macd.setMacdValue(0.0);
        macd.setMacdSignal(0.0);
        macd.setInterval(new Interval(-0.00106, -0.00053));
        indicators.add(macd);

        average = new Average();
        average.setAverage(0.0);
        average.setInterval(new Interval(-0.00731, 0.00259));
        indicators.add(average);

        sar = new Sar();
        sar.setSar(0.0);
        sar.setInterval(new Interval(0.00067, 0.0010));
        indicators.add(sar);

        individuoEstrategia.setCloseIndicators(indicators);

        GeneticTesterDelegate delegate = new GeneticTesterDelegate();
        delegate.process(individuoEstrategia);

        System.out.println(individuoEstrategia.toString());
    }
}

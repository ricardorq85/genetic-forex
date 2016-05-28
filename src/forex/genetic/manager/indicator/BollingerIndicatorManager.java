/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Bollinger;
import forex.genetic.manager.IntervalManager;

/**
 *
 * @author ricardorq85
 */
public class BollingerIndicatorManager extends IntervalIndicatorManager<Bollinger> {

    public BollingerIndicatorManager() {
        super(false, "Bollinger");
    }

    public Bollinger getIndicatorInstance() {
        return new Bollinger("Bollinger");
    }

    public Indicator generate(Bollinger indicator, Point point) {
        Interval interval = null;
        Bollinger bollingerBand = this.getIndicatorInstance();
        if (indicator != null) {
            bollingerBand.setPeriod(indicator.getPeriod());
            bollingerBand.setDesviation(indicator.getDesviation());
            double value = indicator.getUpper() - indicator.getLower();
            interval = intervalManager.generate(value, -value * 0.1, value * 0.1);
        } else {
            interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
        }
        bollingerBand.setInterval(interval);
        return bollingerBand;
    }

    @Override
    public boolean operate(Bollinger bollingerBandIndividuo, Bollinger iBollinger, Point point) {
        return intervalManager.operate(bollingerBandIndividuo.getInterval(), iBollinger.getUpper() - iBollinger.getLower(), 0.0);
    }

/*    public Indicator optimize(Bollinger individuo, Bollinger optimizedIndividuo, Bollinger indicator, Point point) {
        Bollinger optimized = this.getIndicatorInstance();
        double value = indicator.getUpper() - indicator.getLower();
        Interval generated = intervalManager.generate(value, 0.0, 0.0);
        intervalManager.round(generated);
        Interval intersected = IntervalManager.intersect(generated, individuo.getInterval());
        Interval optimizedInterval = intervalManager.optimize((optimizedIndividuo == null) ? null : optimizedIndividuo.getInterval(), intersected);
        optimized.setInterval(optimizedInterval);
        if (optimized.getInterval() == null) {
            optimized = optimizedIndividuo;
        }
        return optimized;
    }
*/
    @Override
    public double getValue(Bollinger indicator, Point prevPoint, Point point) {
        double value = indicator.getUpper() - indicator.getLower();
        return value;
    }
}

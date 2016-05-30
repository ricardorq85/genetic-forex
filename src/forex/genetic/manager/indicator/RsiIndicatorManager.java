/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Rsi;

/**
 *
 * @author ricardorq85
 */
public class RsiIndicatorManager extends IntervalIndicatorManager<Rsi> {

    public RsiIndicatorManager() {
        super(false, "Rsi");
        this.id = "RSI";
    }

    public Rsi getIndicatorInstance() {
        return new Rsi("Rsi");
    }

    public Indicator generate(Rsi indicator, Point point) {
        Interval interval = null;
        Rsi rsi = new Rsi("Rsi");
        if (indicator != null) {
            rsi.setRsi(indicator.getRsi());
            double value = indicator.getRsi();
            interval = intervalManager.generate(value, -value * 0.1, value * 0.1);
        } else {
            interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
        }
        rsi.setInterval(interval);

        return rsi;
    }

    @Override
    public boolean operate(Rsi rsiIndividuo, Rsi iRsi, Point point) {
        return intervalManager.operate(rsiIndividuo.getInterval(), iRsi.getRsi(), 0.0);
    }

  /*  public Indicator optimize(Rsi individuo, Rsi optimizedIndividuo, Rsi indicator, Point point) {
        Rsi optimized = this.getIndicatorInstance();
        double value = indicator.getRsi();
        Interval generated = intervalManager.generate(value, 0.0, 0.0);
        intervalManager.round(generated);
        Interval intersected = IntervalManager.intersect(generated, individuo.getInterval());
        optimized.setInterval(intervalManager.optimize((optimizedIndividuo == null) ? null : optimizedIndividuo.getInterval(),
                intersected));
        if (optimized.getInterval() == null) {
            optimized = optimizedIndividuo;
        }
        return optimized;
    }
*/
    @Override
    public double getValue(Rsi indicator, Point prevPoint, Point point) {
        double value = indicator.getRsi();
        return value;
    }
}

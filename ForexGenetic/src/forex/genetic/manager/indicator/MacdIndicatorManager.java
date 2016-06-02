/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.Macd;

/**
 *
 * @author ricardorq85
 */
public class MacdIndicatorManager extends IntervalIndicatorManager<Macd> {

    /**
     *
     */
    public MacdIndicatorManager() {
        super(false, "Macd");
        this.id = "MACD";
    }

    /**
     *
     * @return
     */
    @Override
    public Macd getIndicatorInstance() {
        return new Macd("Macd");
    }

    /**
     *
     * @param indicator
     * @param point
     * @return
     */
    @Override
    public Indicator generate(Macd indicator, Point point) {
        Interval interval = null;
        Macd macd = new Macd("Macd");
        if (indicator != null) {
            macd.setMacdSignal(indicator.getMacdSignal());
            macd.setMacdValue(indicator.getMacdValue());
            double value = indicator.getMacdValue() - indicator.getMacdSignal();
            interval = intervalManager.generate(value, -value * 0.1, value * 0.1);
        } else {
            interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
        }
        macd.setInterval(interval);
        return macd;
    }

    /**
     *
     * @param macdIndividuo
     * @param iMacd
     * @param point
     * @return
     */
    @Override
    public boolean operate(Macd macdIndividuo, Macd iMacd, Point point) {
        return intervalManager.operate(macdIndividuo.getInterval(), iMacd.getMacdValue() - iMacd.getMacdSignal(), 0.0);
    }
    /*
    public Indicator optimize(Macd individuo, Macd optimizedIndividuo, Macd indicator, Point point) {
    Macd optimized = this.getIndicatorInstance();
    double value = indicator.getMacdValue() - indicator.getMacdSignal();
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

    /**
     *
     * @param indicator
     * @param prevPoint
     * @param point
     * @return
     */
    @Override
    public double getValue(Macd indicator, Point prevPoint, Point point) {
        double value = indicator.getMacdValue() - indicator.getMacdSignal();
        return value;
    }
}

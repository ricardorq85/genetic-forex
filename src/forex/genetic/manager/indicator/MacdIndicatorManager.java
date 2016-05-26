/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.Indicator;
import forex.genetic.entities.Interval;
import forex.genetic.entities.indicator.Macd;
import forex.genetic.entities.Point;
import forex.genetic.manager.IntervalManager;

/**
 *
 * @author ricardorq85
 */
public class MacdIndicatorManager extends IndicatorManager<Macd> {

    private IntervalManager intervalManager = new IntervalManager();

    public MacdIndicatorManager() {
        super(false);
    }

    public Indicator generate(Macd indicator, Point point) {
        Interval interval = null;
        Macd macd = new Macd();
        if (indicator != null) {
            macd.setMacdSignal(indicator.getMacdSignal());
            macd.setMacdValue(indicator.getMacdValue());
            interval = intervalManager.generate(indicator.getMacdValue(), indicator.getMacdSignal(), Double.NaN);
        } else {
            interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
        }
        macd.setInterval(interval);
        return macd;
    }

    public boolean operate(Macd macdIndividuo, Macd iMacd, Point point) {
        return intervalManager.operate(macdIndividuo.getInterval(), iMacd.getMacdValue(), iMacd.getMacdSignal());
    }

    public Indicator crossover(Macd macd1, Macd macd2) {
        Macd macdHijo = new Macd();
        Interval interval = null;
        if ((macd1 == null) && (macd2 == null)) {
            macdHijo = null;
        } else {
            interval = intervalManager.crossover(
                    (macd1 == null) ? null : macd1.getInterval(),
                    (macd2 == null) ? null : macd2.getInterval());
            macdHijo.setInterval(interval);
        }
        return macdHijo;
    }

    public Indicator mutate(Macd macd) {
        Macd macdHijo = new Macd();
        Interval interval = intervalManager.mutate((macd == null) ? null : macd.getInterval());
        macdHijo.setInterval(interval);
        return macdHijo;
    }

    public Interval calculateInterval(Macd macdIndividuo, Macd iMacd, Point point) {
        throw new UnsupportedOperationException("Not supported.");
    }
}

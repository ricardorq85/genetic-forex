/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.Interval;
import forex.genetic.entities.indicator.Macd;
import forex.genetic.entities.Point;

/**
 *
 * @author ricardorq85
 */
public class MacdIndicatorManager extends IntervalIndicatorManager<Macd> {

    public MacdIndicatorManager() {
        super(false, "Macd");
    }

    public Macd getIndicatorInstance() {
        return new Macd("Macd");
    }

    public Indicator generate(Macd indicator, Point point) {
        Interval interval = null;
        Macd macd = new Macd("Macd");
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

    @Override
    public boolean operate(Macd macdIndividuo, Macd iMacd, Point point) {
        return intervalManager.operate(macdIndividuo.getInterval(), iMacd.getMacdValue(), iMacd.getMacdSignal());
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Adx;

/**
 *
 * @author ricardorq85
 */
public class AdxIndicatorManager extends IntervalIndicatorManager<Adx> {

    private static final int ADX_TREND_VALUE = 30;

    public AdxIndicatorManager() {
        super(false, "Adx");
    }

    public Adx getIndicatorInstance() {
        return new Adx("Adx");
    }

    public Indicator generate(Adx indicator, Point point) {
        Interval interval = null;
        Adx adx = new Adx("Adx");
        if (indicator != null) {
            adx.setAdxValue(indicator.getAdxValue());
            adx.setAdxPlus(indicator.getAdxPlus());
            adx.setAdxMinus(indicator.getAdxMinus());
            //interval = intervalManager.generate(indicator.getAdxPlus(), adx.getAdxMinus(), Double.NaN);
            interval = intervalManager.generate(indicator.getAdxValue() * (indicator.getAdxPlus()-indicator.getAdxMinus()), -10000.00, 10000.00);
        } else {
            interval = intervalManager.generate(Double.NaN, -10000.00, 10000.00);
        }
        adx.setInterval(interval);
        return adx;
    }

    @Override
    public boolean operate(Adx adxIndividuo, Adx iAdx, Point point) {
        return intervalManager.operate(adxIndividuo.getInterval(), iAdx.getAdxValue() * (iAdx.getAdxPlus()-iAdx.getAdxMinus()), 10000.00);
    }
}

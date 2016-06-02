/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Adx;
import forex.genetic.entities.indicator.Indicator;

/**
 *
 * @author ricardorq85
 */
public class AdxIndicatorManager extends IntervalIndicatorManager<Adx> {

    /**
     *
     */
    public AdxIndicatorManager() {
        super(false, true, "Adx");
        this.id = "ADX";
    }

    /**
     *
     * @return
     */
    @Override
    public Adx getIndicatorInstance() {
        return new Adx("Adx");
    }

    /**
     *
     * @param indicator
     * @param point
     * @return
     */
    @Override
    public Indicator generate(Adx indicator, Point point) {
        Interval interval = null;
        Adx adx = new Adx("Adx");
        if (indicator != null) {
            adx.setAdxValue(indicator.getAdxValue());
            adx.setAdxPlus(indicator.getAdxPlus());
            adx.setAdxMinus(indicator.getAdxMinus());
            //interval = intervalManager.generate(indicator.getAdxPlus(), adx.getAdxMinus(), Double.NaN);
            double value = indicator.getAdxValue() * (indicator.getAdxPlus() - indicator.getAdxMinus());
            interval = intervalManager.generate(value, -value * 0.1, value * 0.1);
        } else {
            interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
        }
        adx.setInterval(interval);
        return adx;
    }

    /**
     *
     * @param adxIndividuo
     * @param iAdx
     * @param point
     * @return
     */
    @Override
    public boolean operate(Adx adxIndividuo, Adx iAdx, Point point) {
        return intervalManager.operate(adxIndividuo.getInterval(), iAdx.getAdxValue() * (iAdx.getAdxPlus() - iAdx.getAdxMinus()), 0.0);
    }
    
    /**
     *
     * @param indicator
     * @param prevPoint
     * @param point
     * @return
     */
    @Override
    public double getValue(Adx indicator, Point prevPoint, Point point) {
        double value = indicator.getAdxValue() * (indicator.getAdxPlus() - indicator.getAdxMinus());
        return value;
    }
}

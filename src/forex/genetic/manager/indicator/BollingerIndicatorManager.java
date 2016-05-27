/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Bollinger;

/**
 *
 * @author ricardorq85
 */
public class BollingerIndicatorManager extends IntervalIndicatorManager<Bollinger> {

    public BollingerIndicatorManager() {
        super(true, "Bollinger");
    }

    public Bollinger getIndicatorInstance() {
        return new Bollinger("Bollinger");
    }

    public Indicator generate(Bollinger indicator, Point point) {
        Interval interval = null;
        Bollinger bollingerBand = new Bollinger("Bollinger");
        if (indicator != null) {
            bollingerBand.setPeriod(indicator.getPeriod());
            bollingerBand.setDesviation(indicator.getDesviation());
            interval = intervalManager.generate(indicator.getUpper() - indicator.getLower(), point.getLow(), point.getHigh());
        } else {
            interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
        }
        bollingerBand.setInterval(interval);
        return bollingerBand;
    }

    @Override
    public boolean operate(Bollinger bollingerBandIndividuo, Bollinger iBollinger, Point point) {
        return intervalManager.operate(bollingerBandIndividuo.getInterval(), iBollinger.getUpper() - iBollinger.getLower(), point);
    }

    @Override
    public Interval calculateInterval(Bollinger bollingerBandIndividuo, Bollinger iBollinger, Point point) {
        return intervalManager.calculateInterval(bollingerBandIndividuo.getInterval(), iBollinger.getUpper() - iBollinger.getLower(), point);
    }
}

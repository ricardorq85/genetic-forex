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

    protected RsiIndicatorManager() {
        super(false, "Rsi");
    }

    public Rsi getIndicatorInstance() {
        return new Rsi("Rsi");
    }

    public Indicator generate(Rsi indicator, Point point) {
        Interval interval = null;
        Rsi rsi = new Rsi("Rsi");
        if (indicator != null) {
            rsi.setRsi(indicator.getRsi());
            interval = intervalManager.generate(indicator.getRsi(), 0.0, 100.0);
        } else {
            interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
        }
        rsi.setInterval(interval);

        return rsi;
    }

    @Override
    public boolean operate(Rsi rsiIndividuo, Rsi iRsi, Point point) {
        return intervalManager.operate(rsiIndividuo.getInterval(), iRsi.getRsi(), 100);
    }
}

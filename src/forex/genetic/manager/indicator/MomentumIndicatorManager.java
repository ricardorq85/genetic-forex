/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Momentum;

/**
 *
 * @author ricardorq85
 */
public class MomentumIndicatorManager extends IntervalIndicatorManager<Momentum> {

    protected MomentumIndicatorManager() {
        super(false, "Momentum");
    }

    public Momentum getIndicatorInstance() {
        return new Momentum("Momentum");
    }

    public Indicator generate(Momentum indicator, Point point) {
        Interval interval = null;
        Momentum momentum = new Momentum("Momentum");
        if (indicator != null) {
            momentum.setMomentum(indicator.getMomentum());
            interval = intervalManager.generate(indicator.getMomentum(), 0.0, 100.0);
        } else {
            interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
        }
        momentum.setInterval(interval);

        return momentum;
    }

    @Override
    public boolean operate(Momentum momentumIndividuo, Momentum iMomentum, Point point) {
        return intervalManager.operate(momentumIndividuo.getInterval(), iMomentum.getMomentum(), 100);
    }
}

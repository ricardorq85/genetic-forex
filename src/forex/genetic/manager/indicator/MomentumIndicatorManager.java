/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.Momentum;

/**
 *
 * @author ricardorq85
 */
public class MomentumIndicatorManager extends IntervalIndicatorManager<Momentum> {

    public MomentumIndicatorManager() {
        super(false, "Momentum");
        this.id = "MOMENTUM";
    }

    public Momentum getIndicatorInstance() {
        return new Momentum("Momentum");
    }

    public Indicator generate(Momentum indicator, Point point) {
        Interval interval = null;
        Momentum momentum = new Momentum("Momentum");
        if (indicator != null) {
            momentum.setMomentum(indicator.getMomentum());
            double value = indicator.getMomentum();
            interval = intervalManager.generate(value, -value * 0.1, value * 0.1);
        } else {
            interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
        }
        momentum.setInterval(interval);

        return momentum;
    }

    @Override
    public boolean operate(Momentum momentumIndividuo, Momentum iMomentum, Point point) {
        return intervalManager.operate(momentumIndividuo.getInterval(), iMomentum.getMomentum(), 0.0);
    }

    /*  public Indicator optimize(Momentum individuo, Momentum optimizedIndividuo, Momentum indicator, Point point) {
    Momentum optimized = this.getIndicatorInstance();
    double value = indicator.getMomentum();
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
    public double getValue(Momentum indicator, Point prevPoint, Point point) {
        double value = indicator.getMomentum();
        return value;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.Interval;
import forex.genetic.util.NumberUtil;

/**
 *
 * @author ricardorq85
 */
public class EqualIntervalManager extends IntervalManager {

    public EqualIntervalManager(String name) {
        super(name);
    }

    @Override
    public Interval crossover(Interval<Double> interval1, Interval<Double> interval2) {
        Interval interval = new DoubleInterval(this.name);
        if ((interval1 == null) && (interval2 == null)) {
            interval.setLowInterval(null);
            interval.setHighInterval(null);
        } else if (interval1 == null) {
            if (random.nextBoolean()) {
                interval.setLowInterval(null);
                interval.setHighInterval(null);
            } else {
                interval.setLowInterval(interval2.getLowInterval());
                interval.setHighInterval(interval2.getHighInterval());
            }
        } else if (interval2 == null) {
            if (random.nextBoolean()) {
                interval.setLowInterval(null);
                interval.setHighInterval(null);
            } else {
                interval.setLowInterval(interval1.getLowInterval());
                interval.setHighInterval(interval1.getHighInterval());
            }
        } else {
            if (random.nextBoolean()) {
                interval.setLowInterval(interval1.getLowInterval());
                interval.setHighInterval(interval1.getHighInterval());
            } else {
                interval.setLowInterval(interval2.getLowInterval());
                interval.setHighInterval(interval2.getHighInterval());
            }
        }

        return interval;
    }

    @Override
    public Interval mutate(Interval<Double> interval) {
        Interval<Double> intervalHijo = new DoubleInterval(this.name);
        double interval1;
        if (interval == null) {
            interval1 = generateDefault();
        } else {
            interval1 = especificMutationManager.mutate(interval.getLowInterval(), this.min, interval.getHighInterval());
        }
        intervalHijo.setLowInterval(NumberUtil.round(interval1, 0));
        intervalHijo.setHighInterval(NumberUtil.round(interval1, 0));
        
        return intervalHijo;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.util.NumberUtil;
import java.util.Random;

/**
 *
 * @author ricardorq85
 */
public class IntervalManager {

    private double min = Double.POSITIVE_INFINITY;
    private double max = Double.NEGATIVE_INFINITY;
    private Random random = new Random();
    private static EspecificMutationManager especificMutationManager = EspecificMutationManager.getInstance();

    public IntervalManager() {
    }

    public Interval generate(double value, double i1, double i2) {
        Interval interval = new Interval();
        double interval1 = 0.0;
        double interval2 = 0.0;
        if (Double.isNaN(value) || (value == 0.0)) {
            if (Double.isInfinite(min) || Double.isInfinite(max)) {
                interval1 = (random.nextBoolean()) ? random.nextDouble() : -random.nextDouble();
            } else {
                interval1 = min + ((random.nextBoolean()) ? random.nextDouble() * max : -random.nextDouble() * max);
            }
            interval2 = (interval1 + random.nextDouble()) + interval1 * ((random.nextBoolean()) ? random.nextDouble() : -random.nextDouble());
        } else {
            if (!Double.isNaN(i1)) {
                interval1 = (value - i1);
            }
            if (!Double.isNaN(i2)) {
                interval2 = (value - i2);
            }
        }
        interval.setLowInterval(NumberUtil.round(Math.min(interval1, interval2)));
        interval.setHighInterval(NumberUtil.round(Math.max(interval1, interval2)));

        min = NumberUtil.round(Math.min(min, interval.getLowInterval()));
        max = NumberUtil.round(Math.max(max, interval.getHighInterval()));

        return interval;
    }

    public boolean operate(Interval interval, double value, double price) {
        double lowInterval = interval.getLowInterval();
        double highInterval = interval.getHighInterval();

        return (evaluate(value - price, lowInterval, highInterval));
    }

    public boolean operate(Interval interval, double value, Point point) {
        return (this.calculateInterval(interval, value, point) != null);
    }

    public Interval calculateInterval(Interval interval, double value, Point point) {
        Interval result = new Interval();
        double lowInterval = interval.getLowInterval();
        double highInterval = interval.getHighInterval();

        double highIntervalPosibble = value - lowInterval;
        double lowIntervalPosibble = value - highInterval;

        result.setLowInterval(NumberUtil.round(lowIntervalPosibble));
        result.setHighInterval(NumberUtil.round(highIntervalPosibble));

        result = IntervalManager.intersect(new Interval(lowIntervalPosibble, highIntervalPosibble), new Interval(point.getLow(), point.getHigh()));

        return result;
    }

    private boolean evaluate(double value, double low, double high) {
        return (value >= low) && (value <= high);
    }

    public Interval crossover(Interval interval1, Interval interval2) {
        Interval interval = new Interval();
        if ((interval1 == null) && (interval2 == null)) {
            interval.setLowInterval(min);
            interval.setHighInterval(max);
        } else if (interval1 == null) {
            interval.setLowInterval(interval2.getLowInterval());
            interval.setHighInterval(interval2.getHighInterval());
        } else if (interval2 == null) {
            interval.setLowInterval(interval1.getLowInterval());
            interval.setHighInterval(interval1.getHighInterval());
        } else {
            if (interval1.getLowInterval() > interval2.getHighInterval()) {
                interval.setLowInterval(interval2.getLowInterval());
                interval.setHighInterval(interval1.getHighInterval());
            } else {
                interval.setLowInterval(interval1.getLowInterval());
                interval.setHighInterval(interval2.getHighInterval());
            }
        }
        return interval;
    }

    public Interval mutate(Interval interval) {
        Interval intervalHijo = new Interval();
        double interval1 = 0.0;
        double interval2 = 0.0;
        if (interval == null) {
            interval1 = min + ((random.nextBoolean()) ? random.nextDouble() * max : -random.nextDouble() * max);
            interval2 = (interval1 + random.nextDouble()) + interval1 * ((random.nextBoolean()) ? random.nextDouble() : -random.nextDouble());
        } else {
            interval1 = especificMutationManager.mutate(interval.getLowInterval(), min, interval.getHighInterval());
            interval2 = especificMutationManager.mutate(interval.getHighInterval(), Math.min(interval1, interval.getLowInterval()), max);
        }
        intervalHijo.setLowInterval(NumberUtil.round(Math.min(interval1, interval2)));
        intervalHijo.setHighInterval(NumberUtil.round(Math.max(interval1, interval2)));
        return intervalHijo;
    }

    public static Interval intersect(Interval i1, Interval i2) {
        Interval intersect = new Interval();
        if ((i1 == null) || (i2 == null)) {
            intersect = null;
        } else {
            intersect.setLowInterval(Math.max(i1.getLowInterval(), i2.getLowInterval()));
            intersect.setHighInterval(Math.min(i1.getHighInterval(), i2.getHighInterval()));
            if (intersect.getLowInterval() > intersect.getHighInterval()) {
                intersect = null;
            }
        }
        return intersect;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.util.Constants;
import forex.genetic.util.NumberUtil;
import java.util.Random;

/**
 *
 * @author ricardorq85
 */
public class IntervalManager {

    private double pairFactor = Constants.getPairFactor(PropertiesManager.getPropertyString(Constants.PAIR));
    private double min = Double.POSITIVE_INFINITY;
    private double max = Double.NEGATIVE_INFINITY;
    private Random random = new Random();
    private static EspecificMutationManager especificMutationManager = EspecificMutationManager.getInstance();
    private String name = null;

    public IntervalManager(String name) {
        this.name = name;
    }

    private double generateDefault() {
        if (Double.isInfinite(min) || Double.isInfinite(max)
                || Double.isNaN(min) || Double.isNaN(max)) {
            return (random.nextBoolean()) ? random.nextDouble() : -random.nextDouble();
        } else {
            return min + random.nextDouble() * (max - min);
        }
    }

    public Interval generate(double value, double i1, double i2) {
        Interval<Double> interval = new DoubleInterval(this.name);
        double interval1 = 0.0;
        double interval2 = 0.0;
        if (Double.isInfinite(value) || Double.isNaN(value) || (value == 0.0)) {
            interval1 = generateDefault();
            interval2 = generateDefault();
        } else {
            if (!Double.isNaN(i1) || !Double.isInfinite(i1)) {
                interval1 = (value - i1);
            } else {
                interval1 = generateDefault();
            }
            if (!Double.isNaN(i2) || !Double.isInfinite(i2)) {
                interval2 = (value - i2);
            } else {
                interval2 = generateDefault();
            }
        }
        interval.setLowInterval(NumberUtil.round(Math.min(interval1, interval2)));
        interval.setHighInterval(NumberUtil.round(Math.max(interval1, interval2)));

        min = NumberUtil.round(Math.min(min, interval.getLowInterval()));
        max = NumberUtil.round(Math.max(max, interval.getHighInterval()));

        return interval;
    }

    public boolean operate(Interval<Double> interval, double value, double price) {
        double lowInterval = interval.getLowInterval();
        double highInterval = interval.getHighInterval();

        return (evaluate(value - price, lowInterval, highInterval));
    }

    public boolean operate(Interval interval, double value, Point point) {
        return (this.calculateInterval(interval, value, point) != null);
    }

    public Interval calculateInterval(Interval<Double> interval, double value, Point point) {
        Interval<Double> result = new DoubleInterval(this.name);
        double lowInterval = interval.getLowInterval();
        double highInterval = interval.getHighInterval();

        double highIntervalPosibble = value - lowInterval;
        double lowIntervalPosibble = value - highInterval;

        /*result.setLowInterval(NumberUtil.round(lowIntervalPosibble));
        result.setHighInterval(NumberUtil.round(highIntervalPosibble));
         */
        result.setLowInterval(lowIntervalPosibble);
        result.setHighInterval(highIntervalPosibble);

        Interval<Double> pointInterval = new DoubleInterval(this.name);
        if (PropertiesManager.getOperationType().equals(Constants.OperationType.Buy)) {
            pointInterval.setLowInterval(point.getLow() +  PropertiesManager.getPropertyDouble(Constants.PIPS_FIXER) / pairFactor);
            pointInterval.setHighInterval(point.getHigh() +  PropertiesManager.getPropertyDouble(Constants.PIPS_FIXER) / pairFactor);
        } else {
            pointInterval.setLowInterval(point.getLow());
            pointInterval.setHighInterval(point.getHigh());
        }

        result = IntervalManager.intersect(result, pointInterval);

        return result;
    }

    private boolean evaluate(double value, double low, double high) {
        return (value >= low) && (value <= high);
    }

    public Interval crossover(Interval<Double> interval1, Interval<Double> interval2) {
        Interval interval = intersect(interval1, interval2);
        if (interval == null) {
            interval = new DoubleInterval(this.name);
            if ((interval1 == null) && (interval2 == null)) {
                if (Double.isInfinite(min) || Double.isInfinite(max)) {
                    interval.setLowInterval(null);
                    interval.setHighInterval(null);
                } else {
                    interval.setLowInterval(min);
                    interval.setHighInterval(max);
                }
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
        }
        return interval;
    }

    public Interval mutate(Interval<Double> interval) {
        Interval<Double> intervalHijo = new DoubleInterval(this.name);
        double interval1 = 0.0;
        double interval2 = 0.0;
        if (interval == null) {
            interval1 = generateDefault();
            interval2 = generateDefault();
        } else {
            interval1 = especificMutationManager.mutate(interval.getLowInterval(), min, interval.getHighInterval());
            interval2 = especificMutationManager.mutate(interval.getHighInterval(), Math.min(interval1, interval.getLowInterval()), Math.max(interval1, interval.getHighInterval()));
        }
        intervalHijo.setLowInterval(NumberUtil.round(Math.min(interval1, interval2)));
        intervalHijo.setHighInterval(NumberUtil.round(Math.max(interval1, interval2)));

        min = NumberUtil.round(Math.min(min, intervalHijo.getLowInterval()));
        max = NumberUtil.round(Math.max(max, intervalHijo.getHighInterval()));

        return intervalHijo;
    }

    public static Interval<Double> intersect(Interval<Double> i1, Interval<Double> i2) {
        Interval<Double> intersect = null;
        if ((i1 == null) || (i2 == null)) {
            intersect = null;
        } else {
            intersect = new DoubleInterval(i1.getName());
            intersect.setLowInterval(Math.max(i1.getLowInterval(), i2.getLowInterval()));
            intersect.setHighInterval(Math.min(i1.getHighInterval(), i2.getHighInterval()));
            if (intersect.getLowInterval() > intersect.getHighInterval()) {
                intersect = null;
            }
        }
        return intersect;
    }
}

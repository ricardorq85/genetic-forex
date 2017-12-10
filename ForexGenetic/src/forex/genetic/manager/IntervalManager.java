/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import java.util.Random;

import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.util.NumberUtil;
import forex.genetic.util.RandomUtil;

/**
 *
 * @author ricardorq85
 */
public class IntervalManager {

	protected double min = Double.POSITIVE_INFINITY;

	protected double max = Double.NEGATIVE_INFINITY;

	protected static final Random RANDOM = new Random();

	protected static final EspecificMutationManager ESPECIFIC_MUTATION_MANAGER = EspecificMutationManager.getInstance();

	protected String name = null;

	/**
	 *
	 * @param name
	 */
	public IntervalManager(String name) {
		this.name = name;
	}

	/**
	 *
	 * @return
	 */
	public double getMin() {
		return min;
	}

	/**
	 *
	 * @param min
	 */
	public void setMin(double min) {
		this.min = min;
	}

	/**
	 *
	 * @return
	 */
	public double getMax() {
		return max;
	}

	/**
	 *
	 * @param max
	 */
	public void setMax(double max) {
		this.max = max;
	}

	/**
	 *
	 * @return
	 */
	protected double generateDefault() {
		if (Double.isInfinite(min) || Double.isInfinite(max) || Double.isNaN(min) || Double.isNaN(max)) {
			return (RANDOM.nextBoolean()) ? RANDOM.nextDouble() : -RANDOM.nextDouble();
		} else {
			return min + RANDOM.nextDouble() * (max - min);
		}
	}

	/**
	 *
	 * @param value
	 * @param i1
	 * @param i2
	 * @return
	 */
	public Interval generate(double value, double i1, double i2) {
		Interval<Double> interval = new DoubleInterval(this.name);
		double interval1;
		double interval2;
		if (Double.isInfinite(value) || Double.isNaN(value) || (value == 0.0)) {
			interval1 = generateDefault();
			interval2 = generateDefault();
		} else {
			if (!Double.isNaN(i1) && !Double.isInfinite(i1)) {
				// interval1 = (value - (i1 * (1.0 + ((random.nextBoolean()) ?
				// random.nextDouble() : -random.nextDouble()))));
				interval1 = (value - i1);
			} else {
				interval1 = generateDefault();
			}
			if (!Double.isNaN(i2) && !Double.isInfinite(i2)) {
				// interval2 = (value - (i2 * (1.0 + ((random.nextBoolean()) ?
				// random.nextDouble() : -random.nextDouble()))));
				interval2 = (value - i2);
			} else {
				interval2 = generateDefault();
			}
		}
		interval.setLowInterval(NumberUtil.round(Math.min(interval1, interval2)));
		interval.setHighInterval(NumberUtil.round(Math.max(interval1, interval2)));

		min = Math.min(min, interval.getLowInterval());
		max = Math.max(max, interval.getHighInterval());

		return interval;
	}

	/**
	 *
	 * @param interval
	 * @param value
	 * @param price
	 * @return
	 */
	public boolean operate(Interval<Double> interval, double value, double price) {
		double lowInterval = interval.getLowInterval();
		double highInterval = interval.getHighInterval();

		return (evaluate(NumberUtil.round(value - price), lowInterval, highInterval));
	}

	/**
	 *
	 * @param interval
	 * @param value
	 * @param point
	 * @return
	 */
	public boolean operate(Interval interval, double value, Point point) {
		return (this.calculateInterval(interval, value, point) != null);
	}

	/**
	 *
	 * @param interval
	 * @param value
	 * @param point
	 * @return
	 */
	public Interval calculateInterval(Interval<Double> interval, double value, Point point) {
		Interval<Double> result = new DoubleInterval(this.name);
		double lowInterval = interval.getLowInterval();
		double highInterval = interval.getHighInterval();

		double highIntervalPosibble = value - lowInterval;
		double lowIntervalPosibble = value - highInterval;

		result.setLowInterval(lowIntervalPosibble);
		result.setHighInterval(highIntervalPosibble);

		Interval<Double> pointInterval = new DoubleInterval(this.name);
		pointInterval.setLowInterval(point.getLow());
		pointInterval.setHighInterval(point.getHigh());

		result = IntervalManager.intersect(result, pointInterval);

		return result;
	}

	private boolean evaluate(double value, double low, double high) {
		return (value >= low) && (value <= high);
	}

	/**
	 *
	 * @param interval1
	 * @param interval2
	 * @return
	 */
	public Interval crossover(Interval<Double> interval1, Interval<Double> interval2) {
		Interval interval = (RandomUtil.nextBoolean() ? intersect(interval1, interval2) : union(interval1, interval2));
		if (interval == null) {
			interval = new DoubleInterval(this.name);
			if ((interval1 == null) && (interval2 == null)) {
				interval.setLowInterval(null);
				interval.setHighInterval(null);
				/*
				 * if (Double.isInfinite(min) || Double.isInfinite(max)) {
				 * interval.setLowInterval(null);
				 * interval.setHighInterval(null); } else { Interval
				 * intervalTemp = new DoubleInterval("Temp");
				 * intervalTemp.setLowInterval(min);
				 * intervalTemp.setHighInterval(max);
				 * 
				 * Interval intervalMutated = this.mutate(intervalTemp);
				 * interval.setLowInterval(intervalMutated.getLowInterval());
				 * interval.setHighInterval(intervalMutated.getHighInterval());
				 * }
				 */
			} else if (interval1 == null) {
				Interval intervalMutated = interval2; // this.mutate(interval2);
				interval.setLowInterval(intervalMutated.getLowInterval());
				interval.setHighInterval(intervalMutated.getHighInterval());
			} else if (interval2 == null) {
				Interval intervalMutated = interval1; // this.mutate(interval1);
				interval.setLowInterval(intervalMutated.getLowInterval());
				interval.setHighInterval(intervalMutated.getHighInterval());
			} else {
				if (interval1.getLowInterval() > interval2.getHighInterval()) {
					Interval intervalTemp = new DoubleInterval("Temp");
					intervalTemp.setLowInterval(interval2.getLowInterval());
					intervalTemp.setHighInterval(interval1.getHighInterval());

					// Interval intervalMutated = this.mutate(intervalTemp);
					// interval.setLowInterval(intervalMutated.getLowInterval());
					// interval.setHighInterval(intervalMutated.getHighInterval());

					interval.setLowInterval(intervalTemp.getLowInterval());
					interval.setHighInterval(intervalTemp.getHighInterval());
				} else {
					Interval intervalTemp = new DoubleInterval("Temp");
					intervalTemp.setLowInterval(interval1.getLowInterval());
					intervalTemp.setHighInterval(interval2.getHighInterval());

					// Interval intervalMutated = this.mutate(intervalTemp);
					// interval.setLowInterval(intervalMutated.getLowInterval());
					// interval.setHighInterval(intervalMutated.getHighInterval());

					interval.setLowInterval(intervalTemp.getLowInterval());
					interval.setHighInterval(intervalTemp.getHighInterval());
				}
			}
		}
		return interval;
	}

	private Interval<Double> union(Interval<Double> interval1, Interval<Double> interval2) {
		Interval<Double> union = null;
		if ((interval1 != null) && (interval2 != null)) {
			union = new DoubleInterval(interval1.getName());
			union.setLowInterval(Math.min(interval1.getLowInterval(), interval2.getLowInterval()));
			union.setHighInterval(Math.max(interval1.getHighInterval(), interval2.getHighInterval()));
		}
		return union;
	}

	/**
	 *
	 * @param interval
	 * @return
	 */
	public Interval mutate(Interval<Double> interval) {
		Interval<Double> intervalHijo = new DoubleInterval(this.name);
		double interval1 = 0.0;
		double interval2 = 0.0;
		if (interval == null) {
			interval1 = generateDefault();
			interval2 = generateDefault();
		} else {
			interval1 = ESPECIFIC_MUTATION_MANAGER.mutate(interval.getLowInterval(), min, max);
			interval2 = ESPECIFIC_MUTATION_MANAGER.mutate(interval.getHighInterval(), min, max);
			// interval1 =
			// ESPECIFIC_MUTATION_MANAGER.mutate(interval.getLowInterval(), min,
			// interval.getHighInterval());
			// interval2 =
			// ESPECIFIC_MUTATION_MANAGER.mutate(interval.getHighInterval(),
			// Math.min(interval1, interval.getLowInterval()),
			// Math.max(interval1, interval.getHighInterval()));
		}
		intervalHijo.setLowInterval(NumberUtil.round(Math.min(interval1, interval2)));
		intervalHijo.setHighInterval(NumberUtil.round(Math.max(interval1, interval2)));

		min = Math.min(min, intervalHijo.getLowInterval());
		max = Math.max(max, intervalHijo.getHighInterval());

		return intervalHijo;
	}

	/**
	 *
	 * @param optimized
	 * @param generated
	 * @return
	 */
	public Interval optimize(Interval<Double> optimized, Interval<Double> generated) {
		Interval<Double> optimizedResult = new DoubleInterval(this.name);

		double val1 = 0.0D;
		double val2 = 0.0D;
		if (generated != null) {
			if ((optimized == null) || (optimized.getLowInterval() == null)) {
				val1 = generated.getLowInterval();
			} else {
				val1 = Math.min(optimized.getLowInterval(), generated.getLowInterval());
			}
			if ((optimized == null) || (optimized.getHighInterval() == null)) {
				val2 = generated.getHighInterval();
			} else {
				val2 = Math.max(optimized.getHighInterval(), generated.getHighInterval());
			}
		} else {
			if ((optimized == null) || (optimized.getLowInterval() == null) || (optimized.getHighInterval() == null)) {
				optimizedResult = null;
			} else {
				val1 = optimized.getLowInterval();
				val2 = optimized.getHighInterval();
			}
		}
		if (optimizedResult != null) {
			optimizedResult.setLowInterval(NumberUtil.round(val1));
			optimizedResult.setHighInterval(NumberUtil.round(val2));
		}

		return optimizedResult;
	}

	/**
	 *
	 * @param i1
	 * @param i2
	 * @return
	 */
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

	/**
	 *
	 * @param i1
	 * @param i2
	 * @return
	 */
	public static Interval<Double> difference(Interval<Double> i1, Interval<Double> i2) {
		Interval<Double> difference = null;
		if ((i1 == null) || (i1.getLowInterval() == null) || (i1.getHighInterval() == null)) {
			difference = null;
		} else if ((i2 == null) || (i2.getLowInterval() == null) || (i2.getHighInterval() == null)) {
			difference = i1;
		} else {
			difference = new DoubleInterval(i1.getName());
			Interval<Double> intersect = intersect(i1, i2);
			if (intersect == null) {
				difference = i1;
			} else if (intersect.equals(i1)) {
				difference = null;
			} else if (intersect.equals(i2)) {
				boolean rb = RANDOM.nextBoolean();
				difference.setLowInterval(rb ? i1.getLowInterval() : i2.getHighInterval());
				difference.setHighInterval(rb ? i2.getLowInterval() : i1.getHighInterval());
			} else if (intersect.getLowInterval() == intersect.getHighInterval()) {
				if (intersect.getLowInterval() == i1.getLowInterval()) {
					difference.setLowInterval(i1.getLowInterval() + 0.00001);
					difference.setHighInterval(i1.getHighInterval());
				} else if (intersect.getHighInterval() == i1.getHighInterval()) {
					difference.setLowInterval(i1.getLowInterval());
					difference.setHighInterval(i1.getHighInterval() - 0.00001);
				}
			} else {
				if (i1.getLowInterval() < i2.getLowInterval()) {
					difference.setLowInterval(i1.getLowInterval());
				} else if (i2.getLowInterval() <= i1.getLowInterval()) {
					difference.setLowInterval(intersect.getHighInterval() + 0.00001);
				}
				if (i1.getHighInterval() > i2.getHighInterval()) {
					difference.setHighInterval(i1.getHighInterval());
				} else if (i2.getHighInterval() >= i1.getHighInterval()) {
					difference.setHighInterval(intersect.getLowInterval() - 0.00001);
				}
			}
		}
		return difference;
	}

	/**
	 *
	 * @param interval
	 */
	public void round(Interval<Double> interval) {
		/*
		 * double val1 = interval.getLowInterval(); double val2 =
		 * interval.getHighInterval(); /*if (val1 >= 0) { val1 *= 0.999999999; }
		 * else { val1 *= 1.000000001; } if (val2 >= 0) { val2 *= 1.000000001; }
		 * else { val2 *= 0.999999999; }
		 * interval.setLowInterval(NumberUtil.round(val1));
		 * interval.setHighInterval(NumberUtil.round(val2));
		 */
	}
}

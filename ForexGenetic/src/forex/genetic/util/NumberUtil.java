/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.util;

import java.math.BigDecimal;

import forex.genetic.manager.PropertiesManager;

/**
 *
 * @author ricardorq85
 */
public class NumberUtil {

	/**
	 *
	 * @param d
	 * @return
	 */
	public static double round(double d) {
		return NumberUtil.round(d, PropertiesManager.getDefaultScaleRounding());
	}

	public static double round(double d, boolean positive) {
		double rounded = NumberUtil.round(d, PropertiesManager.getDefaultScaleRounding());
		if (positive && rounded <= 0.0D) {
			rounded = Double.POSITIVE_INFINITY;
		}
		return rounded;
	}

	/**
	 *
	 * @param d
	 * @param scale
	 * @return
	 */
	public static double round(double d, int scale) {
		if (Double.isInfinite(d) || Double.isNaN(d)) {
			return d;
		} else {
			BigDecimal bd = new BigDecimal(d);
			bd = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
			double value = bd.doubleValue();
			return value;
		}
	}
	
	public static boolean isInfiniteOrNan(double d) {
		return (Double.isInfinite(d) || Double.isNaN(d));
	}
	
	public static boolean isAnyInfiniteOrNan(double... ds) {
		boolean val = false;
		for (int i = 0; i < ds.length && !val; i++) {
			val = isInfiniteOrNan(ds[i]);
		}
		return val;
	}

	public static double zeroToOne(double d) {
		return (d == 0.0D) ? 1.0D : d;
	}

	public static double zeroToOne(int d) {
		return (d == 0) ? 1.0D : d;
	}
}

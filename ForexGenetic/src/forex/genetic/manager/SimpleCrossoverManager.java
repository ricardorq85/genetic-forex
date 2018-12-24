/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.util.RandomUtil;

/**
 *
 * @author ricardorq85
 */
public class SimpleCrossoverManager extends EspecificCrossoverManager {

	/**
	 *
	 * @param d1
	 * @param d2
	 * @param min
	 * @param max
	 * @return
	 */
	@Override
	public double crossover(double d1, double d2, double min, double max) {
		double d = (d1 + d2) / (1 + RandomUtil.nextDouble() * 2);
		if (d < min) {
			d = min;
		}
		if (d > max) {
			d = max;
		}
		return d;
	}

	/**
	 *
	 * @param d1
	 * @param d2
	 * @param min
	 * @param max
	 * @return
	 */
	@Override
	public int crossover(int d1, int d2, int min, int max) {
		int d = (d1 + d2) / (1 + RandomUtil.nextInt(2));
		if (d < min) {
			d = min;
		}
		if (d > max) {
			d = max;
		}
		return d;
	}
}

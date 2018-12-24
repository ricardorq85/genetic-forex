/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.util.NumberUtil;
import forex.genetic.util.RandomUtil;

/**
 *
 * @author ricardorq85
 */
public class SimpleMutationManager extends EspecificMutationManager {

	private double mutate(double base, double factor) {
		double r = (RandomUtil.nextDouble() * factor);
		int criterio = (RandomUtil.nextInt(2) + 1);
		double mutated = (base + r) / criterio;
		return mutated;
	}

	private int mutate(int base, int factor) {
		int r = RandomUtil.nextInt(Math.abs(factor));
		int criterio = (RandomUtil.nextInt(2) + 1);
		int mutated = (int) ((base + r) / criterio);
		return mutated;
	}

	/**
	 *
	 * @param d1
	 * @param min
	 * @param max
	 * @return
	 */
	@Override
	public double mutate(double d1, double min, double max) {
		boolean infiniteOrNan = NumberUtil.isAnyInfiniteOrNan(min, max);
		double base = (infiniteOrNan) ? d1 : ((d1 + min + max) / 3);
		double factor = (infiniteOrNan) ? d1 : (max - min);
		double mutado = mutate(base, factor);
		if (mutado < min) {
			mutado = min;
		}
		if (mutado > max) {
			mutado = max;
		}
		return (mutado);
	}

	/**
	 *
	 * @param d1
	 * @param min
	 * @param max
	 * @return
	 */
	@Override
	public int mutate(int d1, int min, int max) {
		boolean infiniteOrNan = NumberUtil.isAnyInfiniteOrNan(min, max);
		int base = (infiniteOrNan) ? d1 : ((d1 + min + max) / 3);
		int factor = (infiniteOrNan) ? d1 : (max - min);
		int mutado = mutate(base, factor);
		if (mutado < min) {
			mutado = min;
		}
		if (mutado > max) {
			mutado = max;
		}
		return (mutado);
	}

}

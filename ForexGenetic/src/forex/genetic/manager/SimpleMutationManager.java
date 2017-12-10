/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import java.util.Random;

/**
 *
 * @author ricardorq85
 */
public class SimpleMutationManager extends EspecificMutationManager {

	private final Random random = new Random();

	private double mutate(double base, double factor) {
		double r = (random.nextDouble() * factor);// * (random.nextBoolean() ? 1
													// : -1);
		int criterio = (2);

		double mutated = (base + r) / criterio;

		return mutated;
	}

	private int mutate(int base, int factor) {
		int r = random.nextInt(Math.abs(factor));// * (random.nextBoolean() ? 1
													// : -1);
		int criterio = (2);

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
		double base = (Double.isInfinite(min) || Double.isInfinite(max)) ? d1 : ((d1 + min + max) / 3);
		double factor = (Double.isInfinite(min) || Double.isInfinite(max)) ? d1 : (max - min);
		double mutado = mutate(base, factor);
		return (mutado);
		// return ((Double.isInfinite(min) || Double.isInfinite(max)) ?
		// ((d1 + random.nextDouble() * d1) / 2)
		// : (d1 + min + random.nextDouble() * (max - min)) / 2);
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
		int base = (Double.isInfinite(min) || Double.isInfinite(max)) ? d1 : ((d1 + min + max) / 3);
		int factor = (Double.isInfinite(min) || Double.isInfinite(max)) ? d1 : (max - min);
		int mutado = mutate(base, factor);
		return (mutado);
		// return (((min == Integer.MIN_VALUE) || (max == Integer.MAX_VALUE))
		// ? (d1 + (random.nextInt(d1)) / 2) : (d1 + (min + random.nextInt(max -
		// min))) / 2);

	}

}

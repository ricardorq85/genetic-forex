package forex.genetic.util;

import java.util.Random;

public class RandomUtil {
	private static final Random random = new Random();

	public static int generateNegativePositive(int min, int max) {
		if (min > max) {
			throw new IllegalArgumentException("min debe ser menor que max");
		}
		int value;
		int minToProcess = min;
		int maxToProcess = max + 1;
		int flag = 1;
		if (max <= 0) {
			minToProcess = -max;
			maxToProcess = -min + 1;
			flag = -1;
		}
		value = flag * (random.nextInt(maxToProcess - minToProcess) + minToProcess);

		return value;
	}

	public static double generateNegativePositive(double min, double max) {
		if (min > max) {
			throw new IllegalArgumentException("min debe ser menor que max");
		}
		double value;
		double minToProcess = min;
		double maxToProcess = max;
		int flag = 1;
		if (max <= 0) {
			minToProcess = -max;
			maxToProcess = -min;
			flag = -1;
		}
		double r = random.nextDouble();
		value = flag * (r * (maxToProcess - minToProcess) + minToProcess);

		return value;
	}

	public static double nextDouble() {
		return random.nextDouble();
	}

	public static int nextInt(int bound) {
		return random.nextInt(bound);
	}
	
	public static boolean nextBoolean() {
		return random.nextBoolean();
	}
}

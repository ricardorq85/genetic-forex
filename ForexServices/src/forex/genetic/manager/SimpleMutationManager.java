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

    /**
     *
     * @param d1
     * @param min
     * @param max
     * @return
     */
    @Override
    public double mutate(double d1, double min, double max) {
        return ((Double.isInfinite(min) || Double.isInfinite(max))
                ? ((d1 + random.nextDouble() * d1) / 2) : (d1 + (min + random.nextDouble() * (max - min))) / 2);
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
        return (((min == Integer.MIN_VALUE) || (max == Integer.MAX_VALUE))
                ? (d1 + (random.nextInt(d1)) / 2) : (d1 + (min + random.nextInt(max - min))) / 2);
    }
}

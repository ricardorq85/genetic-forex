/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.util.NumberUtil;
import java.util.Random;

/**
 *
 * @author ricardorq85
 */
public class SimpleMutationManager extends EspecificMutationManager {

    private Random random = new Random();

    public double mutate(double d1, double min, double max) {
        return NumberUtil.round((Double.isInfinite(min) || Double.isInfinite(max)) ? ((d1 + random.nextDouble() * d1) / 2) : ((d1 + (min + random.nextDouble() * max))) / 2);
    }

    public int mutate(int d1, int min, int max) {
        return (((min == Integer.MIN_VALUE) || (max == Integer.MAX_VALUE))
                ? (d1 + (random.nextInt(d1)) / 2) : ((d1 + (min + random.nextInt(max)))) / 2);
    }
}

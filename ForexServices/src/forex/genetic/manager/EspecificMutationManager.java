/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

/**
 *
 * @author ricardorq85
 */
public abstract class EspecificMutationManager {

    private static final EspecificMutationManager instance = new SimpleMutationManager();

    /**
     *
     * @return
     */
    public static EspecificMutationManager getInstance() {
        return instance;
    }

    /**
     *
     * @param d1
     * @return
     */
    public int mutate(int d1) {
        return this.mutate(d1, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     *
     * @param d1
     * @return
     */
    public double mutate(double d1) {
        return this.mutate(d1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     *
     * @param d1
     * @param min
     * @param max
     * @return
     */
    public abstract double mutate(double d1, double min, double max);

    /**
     *
     * @param d1
     * @param min
     * @param max
     * @return
     */
    public abstract int mutate(int d1, int min, int max);
}

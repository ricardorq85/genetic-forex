/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

/**
 *
 * @author ricardorq85
 */
public abstract class EspecificCrossoverManager {

    private static final EspecificCrossoverManager instance = new SimpleCrossoverManager();

    /**
     *
     * @return
     */
    public static EspecificCrossoverManager getInstance() {
        return instance;
    }

    /**
     *
     * @param d1
     * @param d2
     * @return
     */
    public int crossover(int d1, int d2) {
        return this.crossover(d1, d2, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     *
     * @param d1
     * @param d2
     * @return
     */
    public double crossover(double d1, double d2) {
        return this.crossover(d1, d2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     *
     * @param d1
     * @param d2
     * @param min
     * @param max
     * @return
     */
    public abstract double crossover(double d1, double d2, double min, double max);

    /**
     *
     * @param d1
     * @param d2
     * @param min
     * @param max
     * @return
     */
    public abstract int crossover(int d1, int d2, int min, int max);
    
}

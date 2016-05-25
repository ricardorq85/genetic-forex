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

    private static EspecificCrossoverManager instance = new SimpleCrossoverManager();

    public static EspecificCrossoverManager getInstance() {
        return instance;
    }

    public int crossover(int d1, int d2) {
        return this.crossover(d1, d2, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public double crossover(double d1, double d2) {
        return this.crossover(d1, d2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public abstract double crossover(double d1, double d2, double min, double max);
    public abstract int crossover(int d1, int d2, int min, int max);
    
}

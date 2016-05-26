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

    private static EspecificMutationManager instance = new SimpleMutationManager();

    public static EspecificMutationManager getInstance() {
        return instance;
    }

    public int mutate(int d1) {
        return this.mutate(d1, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public double mutate(double d1) {
        return this.mutate(d1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public abstract double mutate(double d1, double min, double max);
    public abstract int mutate(int d1, int min, int max);
}

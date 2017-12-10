/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

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
        return (d1 + d2) / 2;
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
        return ((d1 + d2) / 2);
    }
}

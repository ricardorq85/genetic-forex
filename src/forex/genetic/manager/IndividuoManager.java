/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forex.genetic.manager;

import forex.genetic.delegate.GeneticDelegate;

/**
 *
 * @author ricardorq85
 */
public class IndividuoManager {

    private static int individuoCounter = 0;

    public synchronized static String nextId() {
        return (GeneticDelegate.id + "." + (++individuoCounter));
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forex.genetic.manager;

import forex.genetic.delegate.GeneticDelegate;
import forex.genetic.entities.Individuo;
import forex.genetic.manager.statistic.EstadisticaManager;

/**
 *
 * @author ricardorq85
 */
public class IndividuoManager {

    private static int individuoCounter = 0;

    /**
     *
     * @return
     */
    public synchronized static String nextId() {
        EstadisticaManager.addIndividuoGenerado(1);
        return (GeneticDelegate.getId() + "." + (++individuoCounter));
    }
    
    /**
     *
     * @param individuo
     */
    public void smartDelete(Individuo individuo) {
        
    }

}

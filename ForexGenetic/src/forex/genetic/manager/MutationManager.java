/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.Poblacion;
import forex.genetic.manager.controller.IndicadorController;

/**
 *
 * @author ricardorq85
 */
public abstract class MutationManager extends GeneticManager {

    /**
     *
     * @param indicadorController
     */
    public MutationManager(IndicadorController indicadorController) {
        super(indicadorController);
    }

    /**
     *
     * @param generacion
     * @param poblacion
     * @param percentValue
     * @return
     */
    public abstract Poblacion[] mutate(int generacion, Poblacion poblacion, int percentValue);
}

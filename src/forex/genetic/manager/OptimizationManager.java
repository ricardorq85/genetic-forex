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
public abstract class OptimizationManager extends GeneticManager {

    public OptimizationManager(IndicadorController indicadorController) {
        super(indicadorController);
    }

    public abstract Poblacion[] optimize(int generacion, Poblacion poblacion, int percentValue);
}

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
public abstract class CrossoverManager extends GeneticManager {

	/**
	 *
	 * @param indicadorController
	 */
	public CrossoverManager(IndicadorController indicadorController) {
		super(indicadorController);
	}

	public abstract Poblacion[] crossover(int generacion, Poblacion poblacionBase, Poblacion poblacionParaCruzar,
			int percentValue);

	/**
	 *
	 * @param generacion
	 * @param poblacion
	 * @param percentValue
	 * @return
	 */
	public Poblacion[] crossover(int generacion, Poblacion poblacion, int percentValue) {
		return (crossover(generacion, poblacion, poblacion, percentValue));
	}

}

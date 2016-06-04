/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.controller;

import forex.genetic.manager.CrossoverIndividuoManager;
import forex.genetic.manager.CrossoverManager;
import forex.genetic.manager.MutationIndividuoManager;
import forex.genetic.manager.MutationManager;
import forex.genetic.manager.OptimizationIndividuoManager;
import forex.genetic.manager.OptimizationManager;

/**
 *
 * @author ricardorq85
 */
public class GeneticIndividuoController extends GeneticController {

    /**
     *
     * @return
     */
    @Override
    public CrossoverManager getCrossoverManager() {
        return new CrossoverIndividuoManager();
    }

    /**
     *
     * @return
     */
    @Override
    public MutationManager getMutationManager() {
        return new MutationIndividuoManager();
    }

    /**
     *
     * @return
     */
    @Override
    public OptimizationManager getOptimizationManager() {
        return new OptimizationIndividuoManager();
    }
}

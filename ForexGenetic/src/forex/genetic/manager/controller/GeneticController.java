/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.controller;

import forex.genetic.manager.CrossoverManager;
import forex.genetic.manager.MutationManager;
import forex.genetic.manager.OptimizationManager;

/**
 *
 * @author ricardorq85
 */
public abstract class GeneticController {

    /**
     *
     * @return
     */
    public abstract CrossoverManager getCrossoverManager();

    /**
     *
     * @return
     */
    public abstract MutationManager getMutationManager();

    /**
     *
     * @return
     */
    public abstract OptimizationManager getOptimizationManager();
}

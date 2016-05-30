/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.controller;

import forex.genetic.manager.CrossoverIndividuoTendenciaManager;
import forex.genetic.manager.CrossoverManager;
import forex.genetic.manager.MutationIndividuoTendenciaManager;
import forex.genetic.manager.MutationManager;
import forex.genetic.manager.OptimizationManager;

/**
 *
 * @author USER
 */
public class GeneticIndividuoTendenciaController extends GeneticController {

    @Override
    public CrossoverManager getCrossoverManager() {
        return new CrossoverIndividuoTendenciaManager();
    }

    @Override
    public MutationManager getMutationManager() {
        return new MutationIndividuoTendenciaManager();
    }

    @Override
    public OptimizationManager getOptimizationManager() {
        return null;
    }
}

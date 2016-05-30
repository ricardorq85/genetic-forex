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
 * @author USER
 */
public abstract class GeneticController {

    public abstract CrossoverManager getCrossoverManager();

    public abstract MutationManager getMutationManager();

    public abstract OptimizationManager getOptimizationManager();
}

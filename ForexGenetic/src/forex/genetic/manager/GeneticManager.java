/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.manager.controller.IndicadorController;

/**
 *
 * @author ricardorq85
 */
public abstract class GeneticManager {

    /**
     *
     */
    protected boolean endProcess = false;

    /**
     *
     */
    protected final IndicadorController indicadorController;

    /**
     *
     * @param indicadorController
     */
    public GeneticManager(IndicadorController indicadorController) {
        this.indicadorController = indicadorController;
    }

    /**
     *
     */
    public synchronized void endProcess() {
        this.endProcess = true;
    }
}

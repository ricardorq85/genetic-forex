/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread;

import forex.genetic.entities.Poblacion;
import forex.genetic.manager.CrossoverManager;

/**
 *
 * @author ricardorq85
 */
public class CrossoverThread extends Thread {

    private final int generacion;
    private final Poblacion poblacion;
    private final Poblacion newPoblacion;
    private final int percentValue;
    private final CrossoverManager crossoverManager;

    /**
     *
     * @param name
     * @param generacion
     * @param poblacion
     * @param percentValue
     * @param crossoverManager
     */
    public CrossoverThread(String name, int generacion, Poblacion poblacion, int percentValue, CrossoverManager crossoverManager) {
        super(name);
        this.newPoblacion = new Poblacion();
        this.generacion = generacion;
        this.poblacion = poblacion;
        this.percentValue = percentValue;
        this.crossoverManager = crossoverManager;
    }

    @Override
    public void run() {
        Poblacion[] crossoverPoblacion = crossoverManager.crossover(generacion, poblacion, percentValue);
        newPoblacion.addAll(crossoverPoblacion[1]);
    }

    /**
     *
     */
    public void endProcess() {
        crossoverManager.endProcess();
    }

    /**
     *
     * @return
     */
    public Poblacion getNewPoblacion() {
        return newPoblacion;
    }
}

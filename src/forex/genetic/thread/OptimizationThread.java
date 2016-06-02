/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread;

import forex.genetic.entities.Poblacion;
import forex.genetic.manager.OptimizationManager;

/**
 *
 * @author ricardorq85
 */
public class OptimizationThread extends Thread {

    private int generacion;
    private Poblacion poblacion;
    private Poblacion newPoblacion = new Poblacion();
    private int percentValue;
    private OptimizationManager optimizationManager;
    private Poblacion poblacionHija = new Poblacion();
    private Poblacion poblacionPadre = new Poblacion();

    /**
     *
     * @param name
     * @param generacion
     * @param poblacion
     * @param percentValue
     * @param optimizationManager
     */
    public OptimizationThread(String name, int generacion, Poblacion poblacion, int percentValue, OptimizationManager optimizationManager) {
        super(name);
        this.generacion = generacion;
        this.poblacion = poblacion;
        this.percentValue = percentValue;
        this.optimizationManager = optimizationManager;
    }

    @Override
    public void run() {
        Poblacion[] optimizationPoblacion = optimizationManager.optimize(generacion, poblacion, percentValue);
        newPoblacion.addAll(optimizationPoblacion[1]);
        poblacionPadre.addAll(optimizationPoblacion[0]);
        poblacionHija.addAll(optimizationPoblacion[1]);
    }

    /**
     *
     */
    public void endProcess() {
        optimizationManager.endProcess();
    }

    /**
     *
     * @return
     */
    public Poblacion getNewPoblacion() {
        return newPoblacion;
    }

    /**
     *
     * @return
     */
    public Poblacion getPoblacionHija() {
        return poblacionHija;
    }

    /**
     *
     * @return
     */
    public Poblacion getPoblacionPadre() {
        return poblacionPadre;
    }
}

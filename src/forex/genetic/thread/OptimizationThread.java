/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread;

import forex.genetic.entities.Poblacion;
import forex.genetic.manager.OptimizationIndividuoManager;
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

    public OptimizationThread(String name, int generacion, Poblacion poblacion, int percentValue, OptimizationManager optimizationManager) {
        super(name);
        this.generacion = generacion;
        this.poblacion = poblacion;
        this.percentValue = percentValue;
        this.optimizationManager = optimizationManager;
    }

    public void run() {
        Poblacion[] optimizationPoblacion = optimizationManager.optimize(generacion, poblacion, percentValue);
        newPoblacion.addAll(optimizationPoblacion[1]);
        poblacionPadre.addAll(optimizationPoblacion[0]);
        poblacionHija.addAll(optimizationPoblacion[1]);
    }

    public void endProcess() {
        optimizationManager.endProcess();
    }

    public Poblacion getNewPoblacion() {
        return newPoblacion;
    }

    public Poblacion getPoblacionHija() {
        return poblacionHija;
    }

    public Poblacion getPoblacionPadre() {
        return poblacionPadre;
    }
}

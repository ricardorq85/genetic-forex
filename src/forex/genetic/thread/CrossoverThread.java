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

    private int generacion;
    private Poblacion poblacion;
    private Poblacion newPoblacion = new Poblacion();
    private int percentValue;
    private CrossoverManager crossoverManager;

    public CrossoverThread(String name, int generacion, Poblacion poblacion, int percentValue, CrossoverManager crossoverManager) {
        super(name);
        this.generacion = generacion;
        this.poblacion = poblacion;
        this.percentValue = percentValue;
        this.crossoverManager = crossoverManager;
    }

    public void run() {
        Poblacion[] crossoverPoblacion = crossoverManager.crossover(generacion, poblacion, percentValue);
        newPoblacion.addAll(crossoverPoblacion[1]);
    }

    public Poblacion getNewPoblacion() {
        return newPoblacion;
    }
}

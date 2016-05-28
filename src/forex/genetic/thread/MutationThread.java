/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread;

import forex.genetic.entities.Poblacion;
import forex.genetic.manager.MutationManager;

/**
 *
 * @author ricardorq85
 */
public class MutationThread extends Thread {

    private int generacion;
    private Poblacion poblacion;
    private Poblacion newPoblacion = new Poblacion();
    private int percentValue;
    private MutationManager mutationManager;

    public MutationThread(String name, int generacion, Poblacion poblacion, int percentValue, MutationManager mutationManager) {
        super(name);
        this.generacion = generacion;
        this.poblacion = poblacion;
        this.percentValue = percentValue;
        this.mutationManager = mutationManager;
    }

    public void run() {
        Poblacion[] mutationPoblacion = mutationManager.mutate(generacion, poblacion, percentValue);
        newPoblacion.addAll(mutationPoblacion[1]);
    }

    public Poblacion getNewPoblacion() {
        return newPoblacion;
    }
}

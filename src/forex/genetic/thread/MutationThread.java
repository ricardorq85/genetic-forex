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

    private final int generacion;
    private final Poblacion poblacion;
    private final Poblacion newPoblacion;
    private final int percentValue;
    private final MutationManager mutationManager;
    private final Poblacion poblacionHija;
    private final Poblacion poblacionPadre;

    public MutationThread(String name, int generacion, Poblacion poblacion, int percentValue, MutationManager mutationManager) {
        super(name);
        this.poblacionPadre = new Poblacion();
        this.poblacionHija = new Poblacion();
        this.newPoblacion = new Poblacion();
        this.generacion = generacion;
        this.poblacion = poblacion;
        this.percentValue = percentValue;
        this.mutationManager = mutationManager;
    }

    public void run() {
        Poblacion[] mutationPoblacion = mutationManager.mutate(generacion, poblacion, percentValue);
        newPoblacion.addAll(mutationPoblacion[1]);
        poblacionPadre.addAll(mutationPoblacion[0]);
        poblacionHija.addAll(mutationPoblacion[1]);
    }

    public void endProcess() {
        mutationManager.endProcess();
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

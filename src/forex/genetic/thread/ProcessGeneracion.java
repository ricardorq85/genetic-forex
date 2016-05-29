/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread;

import forex.genetic.entities.Poblacion;
import forex.genetic.manager.CrossoverManager;
import forex.genetic.manager.MutationManager;
import forex.genetic.manager.OptimizationManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.statistic.EstadisticaManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import forex.genetic.util.ThreadUtil;

/**
 *
 * @author ricardorq85
 */
public class ProcessGeneracion extends ProcessPoblacionThread {

    private CrossoverManager crossoverManager = new CrossoverManager();
    private MutationManager mutationManager = new MutationManager();
    private OptimizationManager optimizationManager = new OptimizationManager();
    private int generacion;
    private CrossoverThread crossoverThread = null;
    private MutationThread mutationThread = null;
    private OptimizationThread optimizationThread = null;

    public ProcessGeneracion(String name, Poblacion poblacion, int generacion) {
        super(name);
        super.poblacion = poblacion;
        this.generacion = generacion;
    }

    public void run() {
        LogUtil.logTime("Procesar Generacion " + this.getName() + " Individuos=" + poblacion.getIndividuos().size(), 2);
        this.processGeneracion(poblacion);
        this.joinProcessGeneracion();
        LogUtil.logTime("Procesar Generacion " + this.getName() + " Individuos Nuevos=" + newPoblacion.getIndividuos().size(), 2);
    }

    private void processGeneracion(Poblacion poblacion) {
        /** Se mezclan los individuos */
        crossoverThread = new CrossoverThread("crossoverThread " + generacion,
                generacion, poblacion, PropertiesManager.getPropertyInt(Constants.CROSSOVER), crossoverManager);
        if (PropertiesManager.isThread()) {
            crossoverThread.start();
        } else {
            crossoverThread.run();
        }

        /** Se mutan los individuos */
        mutationThread = new MutationThread("mutationThread " + generacion,
                generacion, poblacion, PropertiesManager.getPropertyInt(Constants.MUTATION), mutationManager);
        if (PropertiesManager.isThread()) {
            mutationThread.start();
        } else {
            mutationThread.run();
        }

        /** Optimizacion */
        optimizationThread = new OptimizationThread("optimization " + generacion,
                generacion, poblacion, PropertiesManager.getPropertyInt(Constants.OPTIMIZATION), optimizationManager);
        if (PropertiesManager.isThread()) {
            optimizationThread.start();
        } else {
            optimizationThread.run();
        }
    }

    private void joinProcessGeneracion() {
        if (PropertiesManager.isThread()) {
            ThreadUtil.joinThread(crossoverThread);
            Poblacion p = crossoverThread.getNewPoblacion();
            if (p.getIndividuos() != null) {
                super.getNewPoblacion().addAll(p);
                EstadisticaManager.addIndividuoCruzado(p.getIndividuos().size());
            }
            ThreadUtil.joinThread(mutationThread);
            p = mutationThread.getNewPoblacion();
            if (p.getIndividuos() != null) {
                super.getNewPoblacion().addAll(p);
                EstadisticaManager.addIndividuoMutado(p.getIndividuos().size());
                super.getPoblacionHija().addAll(mutationThread.getPoblacionHija());
                super.getPoblacionPadre().addAll(mutationThread.getPoblacionPadre());
            }
            ThreadUtil.joinThread(optimizationThread);
            p = optimizationThread.getNewPoblacion();
            if (p.getIndividuos() != null) {
                super.getNewPoblacion().addAll(p);
                EstadisticaManager.addIndividuoOptimizado(p.getIndividuos().size());
                super.poblacionHija.addAll(optimizationThread.getPoblacionHija());
                super.poblacionPadre.addAll(optimizationThread.getPoblacionPadre());
            }
        }
    }

    public synchronized void endProcess() {
        if (crossoverThread != null) {
            crossoverThread.endProcess();
        }
        if (mutationThread != null) {
            mutationThread.endProcess();
        }
        if (optimizationThread != null) {
            optimizationThread.endProcess();
        }
    }
}

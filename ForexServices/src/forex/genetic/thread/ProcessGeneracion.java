/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread;

import forex.genetic.entities.Poblacion;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.CrossoverManager;
import forex.genetic.manager.MutationManager;
import forex.genetic.manager.OptimizationManager;
import forex.genetic.util.io.PropertiesManager;
import forex.genetic.manager.controller.GeneticController;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.statistic.EstadisticaManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import forex.genetic.util.ThreadUtil;

/**
 *
 * @author ricardorq85
 */
public class ProcessGeneracion extends ProcessPoblacionThread {

    private final IndicadorController indicadorController;
    private final GeneticController geneticController;
    private final CrossoverManager crossoverManager;
    private final MutationManager mutationManager;
    private final OptimizationManager optimizationManager;
    private final int generacion;
    private CrossoverThread crossoverThread;
    private MutationThread mutationThread;
    private OptimizationThread optimizationThread;

    /**
     *
     * @param name
     * @param poblacion
     * @param generacion
     * @param controllerType
     */
    public ProcessGeneracion(String name, Poblacion poblacion, int generacion,
            ControllerFactory.ControllerType controllerType) {
        super(name);
        this.indicadorController = ControllerFactory.createIndicadorController(ControllerFactory.ControllerType.Individuo);
        super.poblacion = poblacion;
        this.generacion = generacion;
        this.geneticController = ControllerFactory.createGeneticController(controllerType);
        this.crossoverManager = this.geneticController.getCrossoverManager();
        this.mutationManager = this.geneticController.getMutationManager();
        this.optimizationManager = this.geneticController.getOptimizationManager();
    }

    @Override
    public void run() {
        LogUtil.logTime("Procesar Generacion " + this.getName() + " Individuos=" + poblacion.getIndividuos().size(), 2);
        this.processGeneracion(poblacion);
        this.joinProcessGeneracion();
        LogUtil.logTime("Procesar Generacion " + this.getName() + " Individuos Nuevos=" + newPoblacion.getIndividuos().size(), 2);
    }

    private void processGeneracion(Poblacion poblacion) {
        /**
         * Se mezclan los individuos
         */
        crossoverThread = new CrossoverThread("crossoverThread " + generacion,
                generacion, poblacion, PropertiesManager.getPropertyInt(Constants.CROSSOVER), crossoverManager);
        if (PropertiesManager.isThread()) {
            crossoverThread.start();
        } else {
            crossoverThread.run();
        }

        /**
         * Se mutan los individuos
         */
        mutationThread = new MutationThread("mutationThread " + generacion,
                generacion, poblacion, PropertiesManager.getPropertyInt(Constants.MUTATION), mutationManager);
        if (PropertiesManager.isThread()) {
            mutationThread.start();
        } else {
            mutationThread.run();
        }

        /**
         * Optimizacion
         */
        if (optimizationManager != null) {
            optimizationThread = new OptimizationThread("optimization " + generacion,
                    generacion, poblacion, PropertiesManager.getPropertyInt(Constants.OPTIMIZATION), optimizationManager);
            if (PropertiesManager.isThread()) {
                optimizationThread.start();
            } else {
                optimizationThread.run();
            }
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
            if (optimizationManager != null) {
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
    }

    /**
     *
     */
    @Override
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread;

import forex.genetic.entities.Poblacion;
import forex.genetic.entities.Point;
import forex.genetic.entities.statistic.Estadistica;
import forex.genetic.manager.CrossoverManager;
import forex.genetic.manager.FuncionFortalezaManager;
import forex.genetic.manager.MutationManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.statistic.EstadisticaManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import forex.genetic.util.ThreadUtil;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class ProcessPoblacionThread extends Thread {

    private CrossoverManager crossoverManager = new CrossoverManager();
    private MutationManager mutationManager = new MutationManager();
    private List<Point> points;
    private Poblacion poblacion;
    private Poblacion newPoblacion = new Poblacion();
    private boolean recalculate;
    private int poblacionIndex;
    private FuncionFortalezaManager ffm;
    private CrossoverThread crossoverThread = null;
    private MutationThread mutationThread = null;

    public ProcessPoblacionThread(String name, List<Point> points, Poblacion poblacion, boolean recalculate, int poblacionIndex, FuncionFortalezaManager ffm) {
        super(name);
        this.points = points;
        LogUtil.logTime(this.getName() + " : Points=" + this.points.size(), 5);
        this.poblacion = poblacion;
        this.recalculate = recalculate;
        this.poblacionIndex = poblacionIndex;
        this.ffm = ffm;
    }

    public void run() {
        LogUtil.logTime("Procesar Generacion " + this.getName() + " Individuos=" + poblacion.getIndividuos().size(), 3);
        this.processGeneracion(poblacion, poblacionIndex);
        LogUtil.logTime("Calcular fortaleza " + this.getName() + " Individuos=" + poblacion.getIndividuos().size() + " Points=" + points.size(), 2);
        ffm.calculateFortaleza(points, poblacion, recalculate, poblacionIndex);
        LogUtil.logTime(this.getName() + " : Points=" + this.points.size(), 3);
        LogUtil.logTime("Procesar invalidos " + this.getName() + " Individuos=" + poblacion.getIndividuos().size(), 3);
        ffm.processInvalids(poblacion);
        this.joinProcessGeneracion();
        LogUtil.logTime(this.getName() + " Individuos=" + poblacion.getIndividuos().size() + " Points=" + this.points.size(), 5);
    }

    private void processGeneracion(Poblacion poblacion, int generacion) {
        int size = poblacion.getIndividuos().size();

        /** Se mezclan los individuos */
        crossoverThread = new CrossoverThread("crossoverThread " + generacion,
                generacion, poblacion, (new Double(size * PropertiesManager.getPropertyDouble(Constants.CROSSOVER_PERCENT))).intValue(), crossoverManager);
        if (PropertiesManager.getPropertyBoolean(Constants.THREAD)) {
            crossoverThread.start();
        } else {
            crossoverThread.run();
        }

        /** Se mutan los individuos */
        mutationThread = new MutationThread("mutationThread " + generacion,
                generacion, poblacion, (new Double(size * PropertiesManager.getPropertyDouble(Constants.MUTATION_PERCENT))).intValue(), mutationManager);
        if (PropertiesManager.getPropertyBoolean(Constants.THREAD)) {
            mutationThread.start();
        } else {
            mutationThread.run();
        }
    }

    private void joinProcessGeneracion() {
        if (PropertiesManager.getPropertyBoolean(Constants.THREAD)) {
            ThreadUtil.joinThread(crossoverThread);
            Poblacion p = crossoverThread.getNewPoblacion();
            if (p.getIndividuos() != null) {
                newPoblacion.addAll(p);
                EstadisticaManager.addIndividuoCruzado(p.getIndividuos().size());
            }
            ThreadUtil.joinThread(mutationThread);
            p = mutationThread.getNewPoblacion();
            if (p.getIndividuos() != null) {
                newPoblacion.addAll(p);
                EstadisticaManager.addIndividuoMutado(p.getIndividuos().size());
            }
        }
    }

    public Poblacion getPoblacion() {
        return poblacion;
    }

    public Poblacion getNewPoblacion() {
        return newPoblacion;
    }

    public int getPoblacionIndex() {
        return poblacionIndex;
    }
}

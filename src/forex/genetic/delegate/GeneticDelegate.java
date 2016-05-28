/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.delegate;

import forex.genetic.util.Constants;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.manager.FuncionFortalezaManager;
import forex.genetic.manager.PoblacionManager;
import forex.genetic.manager.io.FileOutManager;
import forex.genetic.manager.io.SerializationManager;
import forex.genetic.manager.statistic.EstadisticaManager;
import forex.genetic.thread.ProcessPoblacionThread;
import forex.genetic.thread.PoblacionLoadThread;
import forex.genetic.thread.SerializationReadAllthread;
import forex.genetic.util.LogUtil;
import forex.genetic.util.ThreadUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class GeneticDelegate {

    public static String id = "0";
    private SerializationManager serializationManager = new SerializationManager();
    private FileOutManager fileOutManager = new FileOutManager();
    private FuncionFortalezaManager funcionFortalezaManager = new FuncionFortalezaManager();

    public GeneticDelegate() {
    }

    public Poblacion process() {
        Poblacion poblacion = new Poblacion();
        Poblacion newPoblacion = new Poblacion();
        ProcessPoblacionThread threadProcessPoblacionAcumulada = null;
        ProcessPoblacionThread threadProcessPoblacionNueva = null;
        List<ProcessPoblacionThread> threads = new ArrayList<ProcessPoblacionThread>(PropertiesManager.getPropertyInt(Constants.GENERATIONS));
        List<ProcessPoblacionThread> threadsNew = new ArrayList<ProcessPoblacionThread>(PropertiesManager.getPropertyInt(Constants.GENERATIONS));
        PoblacionLoadThread[] threadPoblacionesLoad = new PoblacionLoadThread[PropertiesManager.getPropertyInt(Constants.END_POBLACION)];
        threadPoblacionesLoad[PropertiesManager.getPropertyInt(Constants.ROOT_POBLACION) - 1] =
                launchLoad(PropertiesManager.getPropertyInt(Constants.ROOT_POBLACION),
                PropertiesManager.getPropertyInt(Constants.ROOT_POBLACION), 0);

        PoblacionLoadThread currentThreadPoblacionLoad = threadPoblacionesLoad[PropertiesManager.getPropertyInt(Constants.ROOT_POBLACION) - 1];
        PoblacionManager currentPoblacionManager = currentThreadPoblacionLoad.getPoblacionManager();
        List<SerializationReadAllthread> readSerialize = new ArrayList<SerializationReadAllthread>();
        SerializationReadAllthread threadSerReadAll = null;
        for (int poblacionIndex = PropertiesManager.getPropertyInt(Constants.INITIAL_POBLACION);
                poblacionIndex <= PropertiesManager.getPropertyInt(Constants.END_POBLACION) && !PropertiesManager.getPropertyBoolean(Constants.TERMINAR);
                poblacionIndex++) {
            System.gc();
            int generacionIndex = 0;
            for (generacionIndex = 1; generacionIndex <= PropertiesManager.getPropertyInt(Constants.GENERATIONS)
                    && !PropertiesManager.getPropertyBoolean(Constants.TERMINAR); generacionIndex++) {
                EstadisticaManager.addGeneracion(1);
                PropertiesManager.load();
                if (threadPoblacionesLoad.length < PropertiesManager.getPropertyInt(Constants.END_POBLACION)) {
                    threadPoblacionesLoad = Arrays.copyOf(threadPoblacionesLoad, PropertiesManager.getPropertyInt(Constants.END_POBLACION));
                }
                ThreadUtil.joinThread(threadPoblacionesLoad[PropertiesManager.getPropertyInt(Constants.ROOT_POBLACION) - 1]);
                newPoblacion = new Poblacion();
                newPoblacion.addAll(threadPoblacionesLoad[PropertiesManager.getPropertyInt(Constants.ROOT_POBLACION) - 1].getPoblacionManager().getPoblacion(), poblacion);
                unirPoblaciones(threads, threadsNew, poblacion, newPoblacion);
                processSerialized(newPoblacion, poblacion, readSerialize);
                if ((currentPoblacionManager != null) && (!currentThreadPoblacionLoad.isAlive())
                        && (generacionIndex == PropertiesManager.getPropertyInt(Constants.GENERATIONS))) {
                    newPoblacion.addAll(currentPoblacionManager.getPoblacion(), poblacion);
                }

                /*                if (threadProcessPoblacionAcumulada != null) {
                outPoblacion(threadProcessPoblacionAcumulada.getPoblacion().getFirst(PropertiesManager.getPropertyInt(Constants.SHOW_HARDEST)));
                try {
                fileOutManager.write(threadProcessPoblacionAcumulada.getPoblacion().getFirst(PropertiesManager.getPropertyInt(Constants.SHOW_HARDEST)), currentPoblacionManager.getDateInterval(), false);
                } catch (IOException ex) {
                ex.printStackTrace();
                }
                }
                 */

                LogUtil.logTime("Generacion = " + (generacionIndex - 1) + " Poblacion=" + poblacionIndex, 1);
                for (int poblacionManagerIndex = PropertiesManager.getPropertyInt(Constants.ROOT_POBLACION);
                        poblacionManagerIndex <= poblacionIndex; poblacionManagerIndex++) {
                    LogUtil.logTime("Individuos=" + poblacion.getIndividuos().size(), 1);
                    LogUtil.logTime("Individuos Nuevos=" + newPoblacion.getIndividuos().size(), 1);
                    if ((poblacionManagerIndex < poblacionIndex) && (threadPoblacionesLoad[poblacionManagerIndex] == null)) {
                        threadPoblacionesLoad[poblacionManagerIndex] = launchLoad(poblacionManagerIndex, poblacionIndex, 1);
                    }
                    if ((poblacionManagerIndex < poblacionIndex - 1) && (threadPoblacionesLoad[poblacionManagerIndex + 1] == null)) {
                        threadPoblacionesLoad[poblacionManagerIndex + 1] = launchLoad(poblacionManagerIndex + 1, poblacionIndex, 1);
                    }

                    currentThreadPoblacionLoad = threadPoblacionesLoad[poblacionManagerIndex - 1];
                    if ((poblacionManagerIndex > PropertiesManager.getPropertyInt(Constants.ROOT_POBLACION)) && (poblacionManagerIndex < poblacionIndex)) {
                        threadPoblacionesLoad[poblacionManagerIndex - 1] = null;
                    }

                    ThreadUtil.joinThread(currentThreadPoblacionLoad);
                    currentPoblacionManager = currentThreadPoblacionLoad.getPoblacionManager();
                    if (poblacionManagerIndex == PropertiesManager.getPropertyInt(Constants.ROOT_POBLACION)) {
                        if (generacionIndex == 1) {
                            LogUtil.logTime("Inicio poblacion " + poblacionIndex + " "
                                    + threadPoblacionesLoad[PropertiesManager.getPropertyInt(Constants.ROOT_POBLACION) - 1].getPoblacionManager().getDateInterval(), 1);
                        }
                        LogUtil.logTime("Cargar poblacion serializada " + (poblacionIndex) + "." + generacionIndex, 1);
                        threadSerReadAll = new SerializationReadAllthread("threadSerReadAll " + (poblacionIndex) + "." + generacionIndex,
                                PropertiesManager.getPropertyString(Constants.SERIALICE_PATH),
                                PropertiesManager.getPropertyInt(Constants.READ_HARDEST),
                                poblacionIndex, serializationManager);
                        if (PropertiesManager.getPropertyBoolean(Constants.THREAD)) {
                            threadSerReadAll.start();
                        } else {
                            threadSerReadAll.run();
                        }
                        readSerialize.add(threadSerReadAll);
                    }

                    // Nueva Poblacion
                    if (threadProcessPoblacionNueva != null) {
                        ThreadUtil.joinThread(threadProcessPoblacionNueva);
                        //LogUtil.logTime("Before ", 1);
                        //outPoblacion(newPoblacion.getFirst(PropertiesManager.getPropertyInt(Constants.SHOW_HARDEST)));
                        LogUtil.logTime("Procesar mas debiles newPoblacion. Individuos=" + newPoblacion.getIndividuos().size(), 1);
                        funcionFortalezaManager.processWeakestPoblacion(newPoblacion, PropertiesManager.getPropertyInt(Constants.INDIVIDUOS));
                    }
                    if (!newPoblacion.getIndividuos().isEmpty()) {
                        threadProcessPoblacionNueva =
                                new ProcessPoblacionThread("threadProcessPoblacionNueva "
                                + poblacionIndex + "." + generacionIndex + "." + poblacionManagerIndex,
                                currentPoblacionManager.getPoints(), newPoblacion,
                                recalculate(poblacionManagerIndex, generacionIndex),
                                poblacionManagerIndex, funcionFortalezaManager);
                        if (PropertiesManager.getPropertyBoolean(Constants.THREAD)) {
                            LogUtil.logTime("Starting Thread " + threadProcessPoblacionNueva.getName(), 4);
                            threadProcessPoblacionNueva.start();
                        } else {
                            threadProcessPoblacionNueva.run();
                        }
                        if (poblacionManagerIndex == poblacionIndex) {
                            threadsNew.add(threadProcessPoblacionNueva);
                        }
                    }
                    if (!poblacion.getIndividuos().isEmpty()) {
                        if ((generacionIndex == 1) && (poblacionManagerIndex == poblacionIndex)) {
                            threadProcessPoblacionAcumulada =
                                    new ProcessPoblacionThread("threadProcessPoblacionAcumulada "
                                    + poblacionIndex + "." + generacionIndex + "." + poblacionManagerIndex,
                                    currentPoblacionManager.getPoints(), poblacion,
                                    recalculate(poblacionManagerIndex, generacionIndex),
                                    poblacionManagerIndex, funcionFortalezaManager);
                            if (PropertiesManager.getPropertyBoolean(Constants.THREAD)) {
                                threadProcessPoblacionAcumulada.start();
                            } else {
                                threadProcessPoblacionAcumulada.run();
                            }
                            threads.add(threadProcessPoblacionAcumulada);
                        }
                    }
                }
            }
            if (threadProcessPoblacionAcumulada != null) {
                ThreadUtil.joinThread(threadProcessPoblacionAcumulada);
            }
            if (threadProcessPoblacionNueva != null) {
                ThreadUtil.joinThread(threadProcessPoblacionNueva);
            }
            LogUtil.logTime("Procesar mas debiles poblacion. Individuos=" + poblacion.getIndividuos().size(), 1);
            funcionFortalezaManager.processWeakestPoblacion(poblacion, PropertiesManager.getPropertyInt(Constants.INDIVIDUOS));
            LogUtil.logTime("Procesar mas debiles newPoblacion. Individuos=" + newPoblacion.getIndividuos().size(), 1);
            funcionFortalezaManager.processWeakestPoblacion(newPoblacion, PropertiesManager.getPropertyInt(Constants.INDIVIDUOS));

            LogUtil.logTime("Generaciones=" + (generacionIndex - 1), 1);
            outPoblacion(poblacion.getFirst(PropertiesManager.getPropertyInt(Constants.SHOW_HARDEST)));
            try {
                fileOutManager.write(poblacion.getFirst(PropertiesManager.getPropertyInt(Constants.SHOW_HARDEST)), currentPoblacionManager.getDateInterval(), true);
                if ((currentPoblacionManager.getPoblacion() != null) && (!currentPoblacionManager.getPoblacion().getIndividuos().isEmpty())) {
                    serializationManager.writeObject(id, poblacion, currentPoblacionManager.getDateInterval(), poblacionIndex);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            EstadisticaManager.showEstadisticas();
        }

        return poblacion;
    }

    private PoblacionLoadThread launchLoad(int poblacionManagerIndex, int poblacionIndex, int plus) {
        LogUtil.logTime("Launching Thread threadPoblacionLoad " + (poblacionManagerIndex + plus), 3);
        PoblacionLoadThread threadPoblacionesLoad = new PoblacionLoadThread("threadPoblacionLoad " + (poblacionManagerIndex + plus),
                new PoblacionManager(), poblacionManagerIndex + plus, ((poblacionManagerIndex + plus) == poblacionIndex) ? true : false);
        if (PropertiesManager.getPropertyBoolean(Constants.THREAD)) {
            threadPoblacionesLoad.start();
        } else {
            threadPoblacionesLoad.run();
        }
        return threadPoblacionesLoad;
    }

    private void processSerialized(Poblacion newPoblacion, Poblacion poblacion, List<SerializationReadAllthread> readSerialize) {
        for (Iterator<SerializationReadAllthread> it = readSerialize.iterator(); it.hasNext();) {
            SerializationReadAllthread threadSerReadAll = it.next();
            if (!threadSerReadAll.isAlive()) {
                Poblacion p = threadSerReadAll.getPoblacion();
                if (p.getIndividuos() != null) {
                    newPoblacion.addAll(p, poblacion);
                    EstadisticaManager.addIndividuoLeido(p.getIndividuos().size());
                }
                it.remove();
            }
        }
    }

    private void unirPoblaciones(List<ProcessPoblacionThread> threads, List<ProcessPoblacionThread> threadsNew,
            Poblacion poblacion, Poblacion newPoblacion) {
        for (Iterator<ProcessPoblacionThread> it = threads.iterator(); it.hasNext();) {
            ProcessPoblacionThread processPoblacionThread = it.next();
            ThreadUtil.joinThread(processPoblacionThread);
            newPoblacion.addAll(processPoblacionThread.getNewPoblacion(), poblacion);
            it.remove();
        }
        for (Iterator<ProcessPoblacionThread> it = threadsNew.iterator(); it.hasNext();) {
            ProcessPoblacionThread processPoblacionThread = it.next();
            ThreadUtil.joinThread(processPoblacionThread);
            poblacion.addAll(processPoblacionThread.getPoblacion());
            it.remove();
        }
    }

    private boolean recalculate(int poblacionIndex, int generacionIndex) {
        return ((poblacionIndex == PropertiesManager.getPropertyInt(Constants.ROOT_POBLACION)) && (generacionIndex == 1));
    }

    public void outPoblacion(Poblacion poblacion) {
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        for (int i = individuos.size() - 1; i >= 0; i--) {
            IndividuoEstrategia individuo = (IndividuoEstrategia) individuos.get(i);
            System.out.println("i=" + i + "; ");
            outIndividuo(individuo);
        }
    }

    public void outIndividuo(IndividuoEstrategia individuo) {
        System.out.println(individuo.toString());
    }
}

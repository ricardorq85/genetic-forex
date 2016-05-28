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
import forex.genetic.thread.ProcessGeneracion;
import forex.genetic.thread.SerializationReadAllthread;
import forex.genetic.util.LogUtil;
import forex.genetic.util.ThreadUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricardorq85
 */
public class GeneticDelegate {

    //IndividuoEstrategia ie = new IndividuoEstrategia();
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
        ProcessGeneracion threadProcessGeneracion = null;
        List<ProcessPoblacionThread> threads = new ArrayList<ProcessPoblacionThread>(PropertiesManager.getPropertyInt(Constants.GENERATIONS));
        List<ProcessPoblacionThread> threadsNew = new ArrayList<ProcessPoblacionThread>(PropertiesManager.getPropertyInt(Constants.GENERATIONS));
        PoblacionLoadThread[] threadPoblacionesLoad = new PoblacionLoadThread[PropertiesManager.getPropertyInt(Constants.END_POBLACION)];

        List<SerializationReadAllthread> readSerialize = new ArrayList<SerializationReadAllthread>();
        SerializationReadAllthread threadSerReadAll = null;
        PoblacionManager currentPoblacionManager = null;
        for (int poblacionIndex = PropertiesManager.getPropertyInt(Constants.INITIAL_POBLACION);
                poblacionIndex <= PropertiesManager.getPropertyInt(Constants.END_POBLACION) && !PropertiesManager.getPropertyBoolean(Constants.TERMINAR);
                poblacionIndex++) {
            System.gc();
            int generacionIndex = 0;
            int poblacionFromIndex = 0;
            int root1 = PropertiesManager.getPropertyInt(Constants.ROOT_POBLACION);
            int back = PropertiesManager.getPropertyInt(Constants.NUMBER_BACK_ROOT_POBLACION);
            if (back < 0) {
                back = (PropertiesManager.getPropertyInt(Constants.END_POBLACION));
            }
            int root2 = poblacionIndex - back + 1;
            poblacionFromIndex = Math.max(root1, root2);

            if (poblacionFromIndex > root1) {
                threadPoblacionesLoad[poblacionFromIndex - 2] = null;
            }
            if (threadPoblacionesLoad[poblacionIndex - 1] == null) {
                threadPoblacionesLoad[poblacionIndex - 1] = launchLoad(poblacionIndex, poblacionIndex, 0, true);
            }

            for (generacionIndex = 1; generacionIndex <= PropertiesManager.getPropertyInt(Constants.GENERATIONS)
                    && !PropertiesManager.getPropertyBoolean(Constants.TERMINAR); generacionIndex++) {
                EstadisticaManager.addGeneracion(1);
                if (threadPoblacionesLoad[poblacionFromIndex - 1] == null) {
                    threadPoblacionesLoad[poblacionFromIndex - 1] = launchLoad(poblacionFromIndex, poblacionFromIndex, 0, false);
                }
                PoblacionLoadThread currentThreadPoblacionLoad = threadPoblacionesLoad[poblacionIndex - 1];
                currentPoblacionManager = currentThreadPoblacionLoad.getPoblacionManager();

                if (threadPoblacionesLoad.length < PropertiesManager.getPropertyInt(Constants.END_POBLACION)) {
                    threadPoblacionesLoad = Arrays.copyOf(threadPoblacionesLoad, PropertiesManager.getPropertyInt(Constants.END_POBLACION));
                }
                ThreadUtil.joinThread(threadPoblacionesLoad[poblacionFromIndex - 1]);
                newPoblacion = new Poblacion();
                //newPoblacion.addAll(threadPoblacionesLoad[poblacionFromIndex - 1].getPoblacionManager().getPoblacion(), poblacion);
                unirPoblaciones(threads, threadsNew, poblacion, newPoblacion);
                processSerialized(newPoblacion, poblacion, readSerialize, poblacionIndex, (generacionIndex == PropertiesManager.getPropertyInt(Constants.GENERATIONS) - 1));
                if ((currentPoblacionManager != null) && (!currentThreadPoblacionLoad.isAlive())
                        && (generacionIndex == PropertiesManager.getPropertyInt(Constants.GENERATIONS) - 1)) {
                    newPoblacion.addAll(currentPoblacionManager.getPoblacion(), poblacion);
                }

                boolean processedWeakestPoblacion = false;
                LogUtil.logTime("Generacion = " + (generacionIndex - 1) + " Poblacion=" + poblacionIndex, 1);
                for (int poblacionManagerIndex = poblacionFromIndex;
                        poblacionManagerIndex <= poblacionIndex; poblacionManagerIndex++) {
                    PropertiesManager.load();
                    LogUtil.logTime("Individuos=" + poblacion.getIndividuos().size(), 1);
                    LogUtil.logTime("Individuos Nuevos=" + newPoblacion.getIndividuos().size(), 1);
                    if ((poblacionManagerIndex < poblacionIndex) && (threadPoblacionesLoad[poblacionManagerIndex] == null)) {
                        threadPoblacionesLoad[poblacionManagerIndex] = launchLoad(poblacionManagerIndex, poblacionIndex, 1, false);
                    }
                    if ((poblacionManagerIndex < poblacionIndex - 1) && (threadPoblacionesLoad[poblacionManagerIndex + 1] == null)) {
                        threadPoblacionesLoad[poblacionManagerIndex + 1] = launchLoad(poblacionManagerIndex + 1, poblacionIndex, 1, false);
                    }

                    currentThreadPoblacionLoad = threadPoblacionesLoad[poblacionManagerIndex - 1];
                    if (((poblacionManagerIndex > poblacionFromIndex)) && (poblacionManagerIndex < poblacionIndex)) {
                        threadPoblacionesLoad[poblacionManagerIndex - 1] = null;
                    }

                    ThreadUtil.joinThread(currentThreadPoblacionLoad);
                    currentPoblacionManager = currentThreadPoblacionLoad.getPoblacionManager();
                    //if (poblacionManagerIndex == poblacionFromIndex) {
                    if ((threadSerReadAll == null) || (!threadSerReadAll.isAlive())) {
                        //if ((PropertiesManager.getPropertyInt(Constants.GENERATIONS) == 1) || (generacionIndex <= PropertiesManager.getPropertyInt(Constants.GENERATIONS) * 2 / 3)) {
                        LogUtil.logTime("Cargar poblacion serializada " + (poblacionIndex) + "." + generacionIndex, 1);
                        threadSerReadAll = new SerializationReadAllthread("threadSerReadAll " + (poblacionIndex) + "." + generacionIndex,
                                PropertiesManager.getPropertyString(Constants.SERIALICE_PATH),
                                PropertiesManager.getPropertyInt(Constants.READ_HARDEST),
                                poblacionIndex, poblacionFromIndex, serializationManager);
                        if (PropertiesManager.getPropertyBoolean(Constants.THREAD)) {
                            threadSerReadAll.start();
                        } else {
                            threadSerReadAll.run();
                        }
                        readSerialize.add(threadSerReadAll);
                        //}
                    }
                    if (!newPoblacion.getIndividuos().isEmpty()) {
                        // Nueva Poblacion
                        if (threadProcessPoblacionNueva != null) {
                            ThreadUtil.joinThread(threadProcessPoblacionNueva);
                            //LogUtil.logTime("Procesar mas debiles newPoblacion. Individuos=" + newPoblacion.getIndividuos().size(), 1);
                            //funcionFortalezaManager.processWeakestPoblacion(newPoblacion, PropertiesManager.getPropertyInt(Constants.INDIVIDUOS));
                        }
                        threadProcessPoblacionNueva =
                                new ProcessPoblacionThread("threadProcessPoblacionNueva "
                                + poblacionIndex + "." + generacionIndex + "." + poblacionManagerIndex,
                                currentPoblacionManager.getPoints(), newPoblacion,
                                recalculate(poblacionIndex, generacionIndex, poblacionManagerIndex, poblacionFromIndex),
                                poblacionManagerIndex, poblacionFromIndex, funcionFortalezaManager);
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
                    if ((threadProcessPoblacionAcumulada != null) && (!threadProcessPoblacionAcumulada.isAlive()) && (!processedWeakestPoblacion)) {
                        LogUtil.logTime("Procesar mas debiles Poblacion. Individuos=" + poblacion.getIndividuos().size(), 1);
                        funcionFortalezaManager.processWeakestPoblacion(poblacion, PropertiesManager.getPropertyInt(Constants.INDIVIDUOS));
                        processedWeakestPoblacion = true;
                        if (poblacion.getIndividuos().size() > 0) {
                            threadProcessGeneracion = new ProcessGeneracion("threadProcessGeneracion " + poblacionIndex + "." + generacionIndex, poblacion, generacionIndex);
                            ThreadUtil.launchThread(threadProcessGeneracion);
                            threads.add(threadProcessGeneracion);
                        }
                    }
                    if (generacionIndex == 1) {
                        if (threadProcessPoblacionAcumulada != null) {
                            ThreadUtil.joinThread(threadProcessPoblacionAcumulada);
                        }
                        if (!poblacion.getIndividuos().isEmpty()) {
                            threadProcessPoblacionAcumulada =
                                    new ProcessPoblacionThread("threadProcessPoblacionAcumulada "
                                    + poblacionIndex + "." + generacionIndex + "." + poblacionManagerIndex,
                                    currentPoblacionManager.getPoints(), poblacion,
                                    recalculate(poblacionIndex, generacionIndex, poblacionManagerIndex, poblacionFromIndex),
                                    poblacionManagerIndex, poblacionFromIndex,
                                    funcionFortalezaManager);
                            ThreadUtil.launchThread(threadProcessPoblacionAcumulada);
                            //if (poblacionManagerIndex == poblacionIndex) {
                            //threads.add(threadProcessPoblacionAcumulada);
                            //}
                        }
                    }
                }
            }
            if (threadProcessPoblacionAcumulada != null) {
                ThreadUtil.joinThread(threadProcessPoblacionAcumulada);
            }
            LogUtil.logTime("Procesar mas debiles poblacion. Individuos=" + poblacion.getIndividuos().size(), 1);
            funcionFortalezaManager.processWeakestPoblacion(poblacion, PropertiesManager.getPropertyInt(Constants.INDIVIDUOS));

            if (threadProcessPoblacionNueva != null) {
                if (poblacionIndex < PropertiesManager.getPropertyInt(Constants.END_POBLACION) && !PropertiesManager.getPropertyBoolean(Constants.TERMINAR)) {
                    ThreadUtil.joinThread(threadProcessPoblacionNueva);
                    LogUtil.logTime("Procesar mas debiles newPoblacion. Individuos=" + newPoblacion.getIndividuos().size(), 1);
                    funcionFortalezaManager.processWeakestPoblacion(newPoblacion, PropertiesManager.getPropertyInt(Constants.INDIVIDUOS));
                }
            }

            LogUtil.logTime("Generaciones=" + (generacionIndex - 1), 1);
            outPoblacion(poblacion.getFirst(PropertiesManager.getPropertyInt(Constants.SHOW_HARDEST)));
            try {
                fileOutManager.write(poblacion.getFirst(PropertiesManager.getPropertyInt(Constants.SHOW_HARDEST)), currentPoblacionManager.getDateInterval(), true);
                if ((currentPoblacionManager.getPoblacion() != null) && (!currentPoblacionManager.getPoblacion().getIndividuos().isEmpty())) {
                    serializationManager.writeObject(id, poblacion,
                            currentPoblacionManager.getDateInterval(),
                            poblacionIndex, poblacionFromIndex);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            EstadisticaManager.showEstadisticas();
        }

        return poblacion;
    }

    private PoblacionLoadThread launchLoad(int poblacionManagerIndex, int poblacionIndex, int plus, boolean poblar) {
        LogUtil.logTime("Launching Thread threadPoblacionLoad " + (poblacionManagerIndex + plus), 3);
        PoblacionLoadThread threadPoblacionesLoad = new PoblacionLoadThread("threadPoblacionLoad " + (poblacionManagerIndex + plus),
                new PoblacionManager(), poblacionManagerIndex + plus,
                poblar);
        if (PropertiesManager.getPropertyBoolean(Constants.THREAD)) {
            threadPoblacionesLoad.start();
        } else {
            threadPoblacionesLoad.run();
        }
        return threadPoblacionesLoad;
    }

    private void processSerialized(Poblacion newPoblacion, Poblacion poblacion, List<SerializationReadAllthread> readSerialize, int waitIndex, boolean wait) {
        for (ListIterator<SerializationReadAllthread> it = readSerialize.listIterator(); it.hasNext();) {
            SerializationReadAllthread threadSerReadAll = it.next();
            if ((wait) && (threadSerReadAll.getProcessedUntil() == waitIndex)) {
                //threadSerReadAll.endProcess();
                ThreadUtil.joinThread(threadSerReadAll);
            }
            if (!threadSerReadAll.isAlive()) {
                Poblacion p = threadSerReadAll.getPoblacion();
                if ((p != null) && (p.getIndividuos() != null)) {
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
            //processPoblacionThread.endProcess();
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

    private boolean recalculate(int poblacionIndex, int generacionIndex, int poblacionManagerIndex, int poblacionFromIndex) {
        return (((poblacionManagerIndex == PropertiesManager.getPropertyInt(Constants.ROOT_POBLACION))
                || (poblacionManagerIndex == poblacionFromIndex))
                && (generacionIndex == 1));
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

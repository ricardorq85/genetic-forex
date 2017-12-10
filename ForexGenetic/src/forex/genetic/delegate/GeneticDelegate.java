/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.delegate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Learning;
import forex.genetic.entities.Poblacion;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.FuncionFortalezaManager;
import forex.genetic.manager.LearningManager;
import forex.genetic.manager.PatternManager;
import forex.genetic.manager.PoblacionManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.io.FileOutManager;
import forex.genetic.manager.io.SerializationLearningManager;
import forex.genetic.manager.io.SerializationPoblacionManager;
import forex.genetic.manager.statistic.EstadisticaManager;
import forex.genetic.thread.PoblacionLoadThread;
import forex.genetic.thread.ProcessGeneracion;
import forex.genetic.thread.ProcessPoblacionThread;
import forex.genetic.thread.SerializationReadAllThread;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import forex.genetic.util.ThreadUtil;

/**
 *
 * @author ricardorq85
 */
public class GeneticDelegate {

    //IndividuoEstrategia ie = new IndividuoEstrategia();
    private static String id = "0";

    private SerializationPoblacionManager serializationPoblacionManager = new SerializationPoblacionManager();

    /**
     *
     */
    protected FileOutManager fileOutManager = null;
    private FuncionFortalezaManager funcionFortalezaManager = new FuncionFortalezaManager();
    private SerializationLearningManager serializationLearningManager = new SerializationLearningManager();
    private LearningManager learningManager = new LearningManager();
    private PatternManager patternManager = new PatternManager();
    private int rootPoblacion = -1;
    private int backPoblacion = -1;
    private int initialPoblacion = -1;
    private int generations = -1;
    private Learning learning = null;
    

    /**
     * @return the id
     */
    public static String getId() {
        return id;
    }

    /**
     * @param aId the id to set
     */
    public static void setId(String aId) {
        id = aId;
    }

    /**
     *
     * @throws FileNotFoundException
     */
    public GeneticDelegate() throws FileNotFoundException {
        this(true);
    }

    /**
     *
     * @param createFile
     * @throws FileNotFoundException
     */
    public GeneticDelegate(boolean createFile) throws FileNotFoundException {
        rootPoblacion = PropertiesManager.getPropertyInt(Constants.ROOT_POBLACION);
        backPoblacion = PropertiesManager.getPropertyInt(Constants.NUMBER_BACK_ROOT_POBLACION);
        initialPoblacion = PropertiesManager.getPropertyInt(Constants.INITIAL_POBLACION);
        generations = PropertiesManager.getPropertyInt(Constants.GENERATIONS);
        learning = serializationLearningManager.readLearning();
        LearningManager.setLearning(learning);
        fileOutManager = new FileOutManager(createFile);
    }

    /**
     *
     * @return
     */
    public FileOutManager getFileOutManager() {
        return fileOutManager;
    }

    /**
     *
     * @return
     * @throws IOException
     */
    public Poblacion process() throws IOException {
        Poblacion poblacion = new Poblacion();
        Poblacion newPoblacion = null;
        Poblacion poblacionHija;
        Poblacion poblacionPadre;
        Poblacion cachePoblacionPadre = new Poblacion();
        ProcessPoblacionThread threadProcessPoblacionAcumulada = null;
        ProcessPoblacionThread threadProcessPoblacionNueva = null;
        ProcessGeneracion threadProcessGeneracion;
        List<ProcessPoblacionThread> threads = new ArrayList<>(generations);
        List<ProcessPoblacionThread> threadsNew = new ArrayList<>(generations);
        PoblacionLoadThread[] threadPoblacionesLoad = new PoblacionLoadThread[PropertiesManager.getPropertyInt(Constants.END_POBLACION) + 1];

        List<SerializationReadAllThread> readSerialize = new ArrayList<>();
        SerializationReadAllThread threadSerReadAll = null;
        PoblacionManager currentPoblacionManager;
        String strPoblacionBase = PropertiesManager.getPoblacionBase();
        Poblacion poblacionBase = null;
        if ((!PropertiesManager.isReadSpecific()) && (strPoblacionBase != null) && (!strPoblacionBase.isEmpty())) {
            poblacionBase = serializationPoblacionManager.readByPoblacionId(PropertiesManager.getSerialicePath(), strPoblacionBase);
        }
        for (int poblacionIndex = initialPoblacion;
                poblacionIndex <= PropertiesManager.getPropertyInt(Constants.END_POBLACION) && !PropertiesManager.getPropertyBoolean(Constants.TERMINAR);
                poblacionIndex++) {
            System.gc();
            int generacionIndex = 0;
            int poblacionFromIndex = 0;
            int root1 = rootPoblacion;
            int back = backPoblacion;
            if (back < 0) {
                back = (PropertiesManager.getPropertyInt(Constants.END_POBLACION));
            }
            int root2 = poblacionIndex - back + 1;
            poblacionFromIndex = Math.max(root1, root2);

            if (poblacionFromIndex > root1) {
                threadPoblacionesLoad[poblacionFromIndex - 2] = null;
            }
            if (threadPoblacionesLoad[poblacionIndex] == null) {
                threadPoblacionesLoad[poblacionIndex] = launchLoad(poblacionIndex + 1, poblacionIndex + 1, 0, true);
            }

            //LogUtil.logTime("Reading padres", 1);
            Poblacion[] padresHijos = null;//serializationPoblacionManager.readPadres(PropertiesManager.getSerialicePath(), poblacion, cachePoblacionPadre);
            poblacionPadre = new Poblacion();//padresHijos[0];
            poblacionHija = new Poblacion();//padresHijos[1];
            cachePoblacionPadre.addAll(poblacionPadre);
            for (generacionIndex = 1; generacionIndex <= generations
                    && !PropertiesManager.getPropertyBoolean(Constants.TERMINAR); generacionIndex++) {
                generations = PropertiesManager.getPropertyInt(Constants.GENERATIONS);
                EstadisticaManager.addGeneracion(1);
                if (threadPoblacionesLoad[poblacionFromIndex - 1] == null) {
                    threadPoblacionesLoad[poblacionFromIndex - 1] = launchLoad(poblacionFromIndex, poblacionFromIndex, 0, false);
                }
                if (threadPoblacionesLoad[poblacionIndex - 1] == null) {
                    threadPoblacionesLoad[poblacionIndex - 1] = launchLoad(poblacionIndex, poblacionIndex, 0, false);
                }

                PoblacionLoadThread currentThreadPoblacionLoad = threadPoblacionesLoad[poblacionIndex - 1];
                currentPoblacionManager = currentThreadPoblacionLoad.getPoblacionManager();

                if (threadPoblacionesLoad.length < PropertiesManager.getPropertyInt(Constants.END_POBLACION)) {
                    threadPoblacionesLoad = Arrays.copyOf(threadPoblacionesLoad, PropertiesManager.getPropertyInt(Constants.END_POBLACION));
                }
                //ThreadUtil.joinThread(threadPoblacionesLoad[poblacionFromIndex - 1]);
                newPoblacion = new Poblacion();
                unirPoblaciones(threads, threadsNew, poblacion, newPoblacion, poblacionHija, poblacionPadre);
                processSerialized(newPoblacion, poblacion, readSerialize,
                        poblacionIndex, (generacionIndex == generations - 1), poblacionHija, poblacionPadre);
                if ((currentPoblacionManager != null) && (!currentThreadPoblacionLoad.isAlive())
                        && (generacionIndex == generations - 1)) {
                    newPoblacion.addAll(currentPoblacionManager.getPoblacion(), poblacion);
                }
                if (poblacionBase != null) {
                    newPoblacion.addAll(poblacionBase);
                    poblacionBase = null;
                }

                boolean processedWeakestPoblacion = false;
                LogUtil.logTime("Generacion = " + (generacionIndex - 1) + " Poblacion=" + poblacionIndex, 1);
                PropertiesManager.load();
                for (int poblacionManagerIndex = poblacionFromIndex;
                        poblacionManagerIndex <= poblacionIndex; poblacionManagerIndex++) {
                    LogUtil.logTime("Individuos=" + poblacion.getIndividuos().size(), 1);
                    LogUtil.logTime("Individuos Nuevos=" + newPoblacion.getIndividuos().size(), 1);
                    if ((poblacionManagerIndex < poblacionIndex) && (threadPoblacionesLoad[poblacionManagerIndex] == null)) {
                        threadPoblacionesLoad[poblacionManagerIndex] = launchLoad(poblacionManagerIndex, poblacionIndex, 1, false);
                    }
                    if ((poblacionManagerIndex < poblacionIndex - 1) && (threadPoblacionesLoad[poblacionManagerIndex + 1] == null)) {
                        threadPoblacionesLoad[poblacionManagerIndex + 1] = launchLoad(poblacionManagerIndex + 1, poblacionIndex, 1, false);
                    }
                    /*if ((poblacionManagerIndex < poblacionIndex - 2) && (threadPoblacionesLoad[poblacionManagerIndex + 2] == null)) {
                        threadPoblacionesLoad[poblacionManagerIndex + 2] = launchLoad(poblacionManagerIndex + 2, poblacionIndex, 1, false);
                    }
                    if ((poblacionManagerIndex < poblacionIndex - 3) && (threadPoblacionesLoad[poblacionManagerIndex + 3] == null)) {
                        threadPoblacionesLoad[poblacionManagerIndex + 3] = launchLoad(poblacionManagerIndex + 3, poblacionIndex, 1, false);
                    }*/

                    currentThreadPoblacionLoad = threadPoblacionesLoad[poblacionManagerIndex - 1];
                    if (((poblacionManagerIndex > poblacionFromIndex)) && (poblacionManagerIndex < poblacionIndex)) {
                        threadPoblacionesLoad[poblacionManagerIndex - 1] = null;
                    }
                    currentPoblacionManager = currentThreadPoblacionLoad.getPoblacionManager();

                    //if (poblacionManagerIndex == poblacionFromIndex) {
                    if ((threadSerReadAll == null) || (!threadSerReadAll.isAlive())) {
                        //if ((PropertiesManager.getPropertyInt(Constants.GENERATIONS) == 1) || (generacionIndex <= PropertiesManager.getPropertyInt(Constants.GENERATIONS) * 2 / 3)) {
                        LogUtil.logTime("Cargar poblacion serializada " + (poblacionIndex) + "." + generacionIndex, 2);
                        threadSerReadAll = new SerializationReadAllThread("threadSerReadAll " + (poblacionIndex) + "." + generacionIndex,
                                PropertiesManager.getSerialicePath(),
                                PropertiesManager.getPropertyInt(Constants.READ_HARDEST),
                                poblacionIndex, poblacionFromIndex, learning);
                        if (PropertiesManager.isThread()) {
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
                        }
                        ThreadUtil.joinThread(currentThreadPoblacionLoad);
                        threadProcessPoblacionNueva =
                                new ProcessPoblacionThread("threadProcessPoblacionNueva "
                                + poblacionIndex + "." + generacionIndex + "." + poblacionManagerIndex,
                                currentPoblacionManager.getPoints(), newPoblacion,
                                recalculate(poblacionIndex, generacionIndex, poblacionManagerIndex, poblacionFromIndex),
                                poblacionManagerIndex, poblacionFromIndex, funcionFortalezaManager);
                        threadProcessPoblacionNueva.setProcessPattern(poblacionManagerIndex == poblacionIndex);
                        if (PropertiesManager.isThread()) {
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
                        LogUtil.logTime("Procesar mas debiles Poblacion. Individuos=" + poblacion.getIndividuos().size(), 2);
                        funcionFortalezaManager.processWeakestPoblacion(poblacion, PropertiesManager.getPropertyInt(Constants.INDIVIDUOS));
                        processedWeakestPoblacion = true;
                        //learningManager.processLearning(learning, poblacion, poblacionHija, poblacionPadre);
                        if (poblacion.getIndividuos().size() > 0) {
                            threadProcessGeneracion = new ProcessGeneracion("threadProcessGeneracion " + poblacionIndex + "." + generacionIndex, poblacion, generacionIndex, ControllerFactory.ControllerType.Individuo);
                            ThreadUtil.launchThread(threadProcessGeneracion);
                            threads.add(threadProcessGeneracion);
                        }
                        threadProcessPoblacionAcumulada = null;
                    }
                    if (generacionIndex == 1) {
                        if ((!poblacion.getIndividuos().isEmpty()) && (poblacionManagerIndex == poblacionIndex)) {
                            if (threadProcessPoblacionAcumulada != null) {
                                ThreadUtil.joinThread(threadProcessPoblacionAcumulada);
                            }
                            ThreadUtil.joinThread(currentThreadPoblacionLoad);
                            threadProcessPoblacionAcumulada =
                                    new ProcessPoblacionThread("threadProcessPoblacionAcumulada "
                                    + poblacionIndex + "." + generacionIndex + "." + poblacionManagerIndex,
                                    currentPoblacionManager.getPoints(), poblacion,
                                    recalculate(poblacionIndex, generacionIndex, poblacionManagerIndex, poblacionFromIndex),
                                    poblacionManagerIndex, poblacionFromIndex,
                                    funcionFortalezaManager);
                            threadProcessPoblacionAcumulada.setProcessPattern(true);
                            ThreadUtil.launchThread(threadProcessPoblacionAcumulada);
                        }
                    }
                }
            }
            if (threadProcessPoblacionAcumulada != null) {
                ThreadUtil.joinThread(threadProcessPoblacionAcumulada);
            }
            if (!poblacion.getIndividuos().isEmpty()) {
                LogUtil.logTime("Procesar mas debiles poblacion. Individuos=" + poblacion.getIndividuos().size(), 2);
                int zeroPosition = FuncionFortalezaManager.processWeakestPoblacion(poblacion, PropertiesManager.getPropertyInt(Constants.INDIVIDUOS));
                learningManager.processLearning(learning, poblacion, poblacionHija, poblacionPadre);
                int size = poblacion.getIndividuos().size();
                int positives = Math.min(size, zeroPosition);
                int negatives = Math.max(size - positives, 0);
                EstadisticaManager.setIndividuos(size);
                EstadisticaManager.setIndividuosPositivos(positives);
                EstadisticaManager.setIndividuosNegativos(negatives);
            }

            if (threadProcessPoblacionNueva != null) {
                if (poblacionIndex < PropertiesManager.getPropertyInt(Constants.END_POBLACION) && !PropertiesManager.getPropertyBoolean(Constants.TERMINAR)) {
                    ThreadUtil.joinThread(threadProcessPoblacionNueva);
                    if (!newPoblacion.getIndividuos().isEmpty()) {
                        LogUtil.logTime("Procesar mas debiles newPoblacion. Individuos=" + newPoblacion.getIndividuos().size(), 2);
                        FuncionFortalezaManager.processWeakestPoblacion(newPoblacion, PropertiesManager.getPropertyInt(Constants.INDIVIDUOS));
                    }
                }
            }

            LogUtil.logTime("Generaciones=" + (generacionIndex - 1), 3);
            outPoblacion(poblacion);
            try {
                PoblacionManager nextPoblacionManager = null;
                if (threadPoblacionesLoad.length > (poblacionIndex)) {
                    PoblacionLoadThread nextThreadPoblacionLoad = threadPoblacionesLoad[poblacionIndex];
                    nextPoblacionManager = nextThreadPoblacionLoad.getPoblacionManager();
                }
                fileOutManager.write("\n**************** Tendencia=" + poblacion.getTendencia().getTendencia() + " ********************");
                fileOutManager.write(poblacion.getFirst(PropertiesManager.getPropertyInt(Constants.SHOW_HARDEST)),
                        (nextPoblacionManager != null) ? nextPoblacionManager.getDateInterval() : null, true);
                //if ((currentPoblacionManager.getPoblacion() != null) && (!currentPoblacionManager.getPoblacion().getIndividuos().isEmpty())) {
                serializationPoblacionManager.writeObject(getId(), poblacion,
                        null,
                        poblacionIndex, poblacionFromIndex);
                //}
                serializationLearningManager.writeObject(learning);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            LogUtil.logTime(learning.toString(), 1);
            EstadisticaManager.showEstadisticas();
        }

        return poblacion;
    }

    private PoblacionLoadThread launchLoad(int poblacionManagerIndex, int poblacionIndex, int plus, boolean poblar) {
        LogUtil.logTime("Launching Thread threadPoblacionLoad " + (poblacionManagerIndex + plus), 3);
        PoblacionLoadThread threadPoblacionesLoad = new PoblacionLoadThread("threadPoblacionLoad " + (poblacionManagerIndex + plus),
                new PoblacionManager(), poblacionManagerIndex + plus,
                poblar);
        if (PropertiesManager.isThread()) {
            threadPoblacionesLoad.start();
        } else {
            threadPoblacionesLoad.run();
        }
        return threadPoblacionesLoad;
    }

    private void processSerialized(Poblacion newPoblacion, Poblacion poblacion,
            List<SerializationReadAllThread> readSerialize, int waitIndex, boolean wait,
            Poblacion poblacionHija, Poblacion poblacionPadre) {
        for (ListIterator<SerializationReadAllThread> it = readSerialize.listIterator(); it.hasNext();) {
            SerializationReadAllThread threadSerReadAll = it.next();
            if ((wait) && (threadSerReadAll.getProcessedUntil() == waitIndex)) {
                //threadSerReadAll.endProcess();
                ThreadUtil.joinThread(threadSerReadAll);
            }
            if (!threadSerReadAll.isAlive()) {
                Poblacion p = threadSerReadAll.getPoblacion();
                if ((p != null) && (p.getIndividuos() != null)) {
                    newPoblacion.addAll(p, poblacion);
                    EstadisticaManager.addIndividuoLeido(p.getIndividuos().size());
                    if (threadSerReadAll.getPoblacionHija() != null) {
                        poblacionHija.addAll(threadSerReadAll.getPoblacionHija());
                    }
                    if (threadSerReadAll.getPoblacionPadre() != null) {
                        poblacionPadre.addAll(threadSerReadAll.getPoblacionPadre());
                    }
                }
                it.remove();
            }
        }
    }

    private void unirPoblaciones(List<ProcessPoblacionThread> threads, List<ProcessPoblacionThread> threadsNew,
            Poblacion poblacion, Poblacion newPoblacion, Poblacion poblacionHija, Poblacion poblacionPadre) {
        for (Iterator<ProcessPoblacionThread> it = threads.iterator(); it.hasNext();) {
            ProcessPoblacionThread processPoblacionThread = it.next();
            //processPoblacionThread.endProcess();
            ThreadUtil.joinThread(processPoblacionThread);
            newPoblacion.addAll(processPoblacionThread.getNewPoblacion(), poblacion);
            poblacionHija.addAll(processPoblacionThread.getPoblacionHija());
            poblacionPadre.addAll(processPoblacionThread.getPoblacionPadre());
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
        return (((poblacionManagerIndex == rootPoblacion)
                || (poblacionManagerIndex == poblacionFromIndex))
                && (generacionIndex == 1));
    }

    /**
     *
     * @param p
     * @throws IOException
     */
    public void outPoblacion(Poblacion p) throws IOException {
        Poblacion poblacion = p.getFirst(PropertiesManager.getPropertyInt(Constants.SHOW_HARDEST));
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        for (int i = individuos.size() - 1; i >= 0; i--) {
            IndividuoEstrategia individuo = (IndividuoEstrategia) individuos.get(i);
            fileOutManager.write("\n");
            fileOutManager.write("i=" + i + "; ");
            outIndividuo(individuo);
        }
    }

    /**
     *
     * @param individuo
     * @throws IOException
     */
    public void outIndividuo(IndividuoEstrategia individuo) throws IOException {
        //System.out.println(individuo.toString());
        fileOutManager.write("\n");
        fileOutManager.write(individuo.toString());
    }
}

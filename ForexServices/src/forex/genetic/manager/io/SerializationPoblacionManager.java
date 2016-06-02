/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.io;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.IndividuoReadData;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Learning;
import forex.genetic.entities.Poblacion;
import forex.genetic.util.io.PropertiesManager;
import forex.genetic.manager.statistic.EstadisticaManager;
import forex.genetic.util.CollectionUtil;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author ricardorq85
 */
public class SerializationPoblacionManager {

    private final static List<File> loadedFiles = new Vector<File>();
    private static int totalCounter = 0;
    private boolean endProcess = false;

    /**
     *
     */
    protected Poblacion poblacionHija = null;

    /**
     *
     */
    protected Poblacion poblacionPadre = null;
    private static Map<String, Poblacion> cachePoblacion = new Hashtable<String, Poblacion>();
    private static final long PERIOD_TIME = (1L * 30L * 24L * 60L * 60L * 1000L);
    private static final long LIMIT_TIME = (1L * 30L * 24L * 60L * 60L * 1000L);

    /**
     *
     */
    public synchronized void endProcess() {
        this.endProcess = true;
    }

    private synchronized boolean getEndProcess() {
        return this.endProcess;
    }

    /**
     *
     * @param totalCounter
     */
    public synchronized static void setTotalCounter(int totalCounter) {
        SerializationPoblacionManager.totalCounter = totalCounter;
    }

    /**
     *
     * @param totalCounter
     */
    public synchronized static void addTotalCounter(int totalCounter) {
        SerializationPoblacionManager.totalCounter += totalCounter;
    }

    /**
     *
     * @param path
     * @param counter
     * @param processedUntil
     * @param processedFrom
     * @param learning
     * @return
     */
    public Poblacion readAll(final String path, int counter, final int processedUntil,
            final int processedFrom, Learning learning) {
        Poblacion poblacion = new Poblacion();

        File root = new File(path);
        int fileCounter = 0;
        int filterProcessed = 0;
        int maxFilePerRead = PropertiesManager.getMaxFilePerRead();
        long dateTime = System.currentTimeMillis() - PERIOD_TIME;
        long limitDateTime = System.currentTimeMillis() - LIMIT_TIME;
        while ((fileCounter < maxFilePerRead)) {
            FileFilter filter = new PoblacionFileFilter(loadedFiles, filterProcessed++, learning, maxFilePerRead, dateTime);
            File[] files = root.listFiles(filter);
            if ((files.length == 0) && (filterProcessed > (learning.getRelacionMonedas().size()))) {
                if (dateTime < limitDateTime) {
                    SerializationPoblacionManager.addTotalCounter(counter);
                    loadedFiles.clear();
                    fileCounter = maxFilePerRead;
                } else {
                    dateTime -= PERIOD_TIME;
                    filterProcessed = 0;
                }
            } else {
                if (files.length > 0) {
                    Arrays.sort(files, new FilePoblacionComparator());
                    int size = loadedFiles.size();
                    List<File> listFiles = Arrays.asList(files);
                    loadedFiles.addAll(CollectionUtil.subListReverse(listFiles, listFiles.size(), listFiles.size() - maxFilePerRead));
                    EstadisticaManager.addArchivoLeido(loadedFiles.size() - size);
                    for (int i = files.length - 1; (!this.getEndProcess()) && (i >= Math.max(0, files.length - maxFilePerRead)); i--) {
                        File file = null;
                        try {
                            file = files[i];
                            String name = file.getName();
                            Poblacion p = (cachePoblacion.get(name));
                            if (p == null) {
                                p = this.readObject(file);
                                //cachePoblacion.put(name, p);
                            }
                            IndividuoReadData individuoReadData = new IndividuoReadData();
                            individuoReadData.setOperationType(p.getOperationType());
                            individuoReadData.setPair(p.getPair());
                            Poblacion poblacionByProcessedUntil = p.getByProcessedUntil(processedUntil, processedFrom, individuoReadData);
                            int fromIndex = 0;
                            if (totalCounter < poblacionByProcessedUntil.getIndividuos().size()) {
                                fromIndex = Math.max(0, totalCounter - 1);
                            } else {
                                fromIndex = 0;
                                totalCounter = 0;
                            }
                            int sizePrev = poblacion.getIndividuos().size();
                            poblacion.addAll(poblacionByProcessedUntil.getFirst(counter, fromIndex));
                            if (poblacion.getIndividuos().size() != sizePrev) {
                                fileCounter++;
                            }
                        } catch (IOException | ClassNotFoundException ex) {
                            if (file != null) {
                                LogUtil.logTime("Error: " + file.getName(), 1);
                            }
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        //Poblacion[] padresHijos = readPadres(path, poblacion);
        //poblacionPadre = padresHijos[0];
        //poblacionHija = padresHijos[1];
        return poblacion;
    }

    /**
     *
     * @param path
     * @param hijos
     * @param poblacionBase
     * @return
     */
    public Poblacion[] readPadres(final String path, Poblacion hijos, Poblacion poblacionBase) {
        Poblacion[] padresHijos = new Poblacion[2];
        Poblacion padresReales = new Poblacion();
        Poblacion hijosReales = new Poblacion();
        List<IndividuoEstrategia> listHijos = hijos.getIndividuos();
        List<IndividuoEstrategia> listBase = poblacionBase.getIndividuos();
        IndividuoEstrategia padre = null;
        int count = 0;
        for (Iterator<IndividuoEstrategia> it = listHijos.iterator(); it.hasNext();) {
            IndividuoEstrategia individuoEstrategia = it.next();
            if ((individuoEstrategia.getIdParent1() != null) && ((individuoEstrategia.getIndividuoType().equals(Constants.IndividuoType.MUTATION))
                    || (individuoEstrategia.getIndividuoType().equals(Constants.IndividuoType.OPTIMIZED)))) {
                Poblacion read = null;
                padre = new IndividuoEstrategia();
                padre.setId(individuoEstrategia.getIdParent1());
                int indexPadre = listHijos.indexOf(padre);
                if (indexPadre >= 0) {
                    padre = listHijos.get(indexPadre);
                } else {
                    indexPadre = hijosReales.getIndividuos().indexOf(padre);
                    if (indexPadre >= 0) {
                        padre = hijosReales.getIndividuos().get(indexPadre);
                    } else {
                        indexPadre = padresReales.getIndividuos().indexOf(padre);
                        if (indexPadre >= 0) {
                            padre = padresReales.getIndividuos().get(indexPadre);
                        } else {
                            indexPadre = listBase.indexOf(padre);
                            if (indexPadre >= 0) {
                                padre = listBase.get(indexPadre);
                            }
                        }
                    }
                }
                if (indexPadre >= 0) {
                    read = new Poblacion();
                    read.add(padre);
                } else {
                    read = this.readByEstrategyId(path, individuoEstrategia.getIdParent1(), individuoEstrategia);
                }
                count++;
                if ((read != null) && (read.getIndividuos() != null) && (!read.getIndividuos().isEmpty())) {
                    hijosReales.add(individuoEstrategia);
                    padresReales.addAll(read);
                }
            }
        }
        padresHijos[0] = padresReales;
        padresHijos[1] = hijosReales;
        return padresHijos;
    }

    /**
     *
     * @param file
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Poblacion readObject(File file)
            throws IOException, ClassNotFoundException {
        Poblacion p;
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file))) {
            p = (Poblacion) reader.readObject();
        }
        return p;
    }

    /**
     *
     * @param file
     * @param id
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Poblacion readStrategy(File file, String id)
            throws IOException, ClassNotFoundException {
        Poblacion p = new Poblacion();
        Poblacion pFile = this.readObject(file);
        IndividuoEstrategia ie = new IndividuoEstrategia();
        ie.setId(id);
        int index = pFile.getIndividuos().indexOf(ie);
        if (index >= 0) {
            IndividuoReadData individuoReadData = new IndividuoReadData();
            individuoReadData.setOperationType(pFile.getOperationType());
            individuoReadData.setPair(pFile.getPair());
            IndividuoEstrategia ind = pFile.getIndividuos().get(index);
            p.add(ind);
            ind.corregir(individuoReadData);
        }
        return p;
    }

    /**
     *
     * @param path
     * @param id
     * @return
     */
    public Poblacion readByEstrategyId(String path, final String id) {
        return this.readByEstrategyId(path, id, null);
    }

    /**
     *
     * @param path
     * @param id
     * @param hijo
     * @return
     */
    public Poblacion readByEstrategyId(String path, final String id, IndividuoEstrategia hijo) {
        Poblacion poblacion = new Poblacion();

        File root = new File(path);
        File[] files = root.listFiles(new StrategyFileFilter(hijo));
        Arrays.sort(files, new FilePoblacionComparator());
        for (int i = (files.length - 1); ((i >= 0) && (poblacion.getIndividuos().isEmpty())); i--) {
            try {
                File file = files[i];
                Poblacion p = this.readObject(file);
                IndividuoReadData individuoReadData = new IndividuoReadData();
                individuoReadData.setOperationType(p.getOperationType());
                individuoReadData.setPair(p.getPair());
                IndividuoEstrategia ie = new IndividuoEstrategia();
                ie.setId(id);
                int index = p.getIndividuos().indexOf(ie);
                if (index >= 0) {
                    IndividuoEstrategia individuoEstrategia = p.getIndividuos().get(index);
                    individuoEstrategia.corregir(individuoReadData);
                    poblacion.add(individuoEstrategia);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return poblacion;
    }

    /**
     *
     * @param path
     * @param commaId
     * @return
     */
    public Poblacion readByPoblacionId(String path, final String commaId) {
        Poblacion poblacion = new Poblacion();
        File root = new File(path);
        String[] ids = commaId.split(",");
        IndividuoReadData individuoReadData = null;
        int firstCount = PropertiesManager.getPropertyInt(Constants.READ_HARDEST);
        for (int k = 0; k < ids.length; k++) {
            String id = ids[k];
            File[] files = root.listFiles(new PoblacionIdFileFilter(id));
            List<File> listFiles = Arrays.asList(files);
            loadedFiles.addAll(listFiles);
            for (int i = 0; i < files.length; i++) {
                try {
                    File file = files[i];
                    Poblacion p = this.readObject(file);
                    if (individuoReadData == null) {
                        individuoReadData = new IndividuoReadData();
                        individuoReadData.setOperationType(p.getOperationType());
                        individuoReadData.setPair(p.getPair());
                    }
                    poblacion.addAll(p.getFirst(firstCount));
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }
        initPoblacion(poblacion, individuoReadData);
        return poblacion;
    }

    private void initPoblacion(Poblacion poblacion, IndividuoReadData individuoReadData) {
        List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
        for (int i = 0; i < individuos.size(); i++) {
            IndividuoEstrategia individuoEstrategia = individuos.get(i);
            individuoEstrategia.corregir(individuoReadData);
        }
    }

    /**
     *
     * @param id
     * @param poblacion
     * @param dateInterval
     * @param poblacionIndex
     * @param poblacionFromIndex
     * @throws IOException
     */
    public void writeObject(String id, Poblacion poblacion, Interval<Date> dateInterval, int poblacionIndex, int poblacionFromIndex)
            throws IOException {
        try (ObjectOutputStream writer = new ObjectOutputStream(
                new FileOutputStream(PropertiesManager.getSerialicePath()
                                + PropertiesManager.getOperationType() + PropertiesManager.getPair()
                                + PropertiesManager.getFileId() + "_"
                                + id + "-" + poblacionFromIndex + "-" + poblacionIndex + ".gfx"))) {
            writer.writeObject(poblacion);
        }
    }

    /**
     *
     * @return
     */
    public Poblacion getPoblacionHija() {
        return poblacionHija;
    }

    /**
     *
     * @return
     */
    public Poblacion getPoblacionPadre() {
        return poblacionPadre;
    }
}

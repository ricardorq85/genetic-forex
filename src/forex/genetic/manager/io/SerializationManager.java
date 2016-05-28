/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.io;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Poblacion;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.statistic.EstadisticaManager;
import forex.genetic.util.CollectionUtil;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author ricardorq85
 */
public class SerializationManager {

    private final static List<File> loadedFiles = new Vector<File>();
    private static int totalCounter = 0;
    private boolean endProcess = false;

    public synchronized void endProcess() {
        this.endProcess = true;
    }

    public Poblacion readAll(final String path, int counter, final int processedUntil, final int processedFrom) {
        Poblacion poblacion = new Poblacion();

        File root = new File(path);
        FileFilter filter = new FileFilter() {

            public boolean accept(File file) {
                boolean accept = false;
                String name = file.getName();
                if ((name.contains(".gfx")) /*&& (name.contains(PropertiesManager.getOperationType().name()))*/) {
                    if ((loadedFiles.isEmpty() || (!loadedFiles.contains(file))) && (name.contains("-" + processedFrom + "-" + processedUntil + "."))
                            && (name.contains(PropertiesManager.getPropertyString(Constants.FILE_ID)))
                            && (name.contains(PropertiesManager.getOperationType().name()))
                            && (name.contains(PropertiesManager.getPropertyString(Constants.PAIR)))) {
                        return true;
                    } else if ((!loadedFiles.contains(file)) && ((name.contains("-" + processedFrom + "-" + processedUntil + "."))
                            || (!name.contains(PropertiesManager.getPropertyString(Constants.FILE_ID)))
                            || (!name.contains(PropertiesManager.getPropertyString(Constants.PAIR))))) {
                        return true;
                    }
                }
                return accept;
            }
        };
        File[] files = root.listFiles(filter);
        Arrays.sort(files, new FilePoblacionComparator());
        if (files.length == 0) {
            totalCounter += counter;
            loadedFiles.clear();
        } else {
            int size = loadedFiles.size();
            List<File> listFiles = Arrays.asList(files);
            loadedFiles.addAll(CollectionUtil.subListReverse(listFiles, listFiles.size(), listFiles.size() - PropertiesManager.getPropertyInt(Constants.MAX_FILE_PER_READ)));
            EstadisticaManager.addArchivoLeido(loadedFiles.size() - size);
        }
        for (int i = files.length - 1; (!endProcess) && (i >= Math.max(0, files.length - PropertiesManager.getPropertyInt(Constants.MAX_FILE_PER_READ))); i--) {
            File file = null;
            try {
                file = files[i];
                Poblacion p = this.readObject(file);
                /*if ((p.getOperationType().equals(PropertiesManager.getOperationType()))
                && (p.getPair().equals(PropertiesManager.getPropertyString(Constants.PAIR)))) {*/
                Poblacion poblacionByProcessedUntil = p.getByProcessedUntil(processedUntil, processedFrom);
                int fromIndex = 0;
                if (totalCounter < poblacionByProcessedUntil.getIndividuos().size()) {
                    fromIndex = totalCounter - 1;
                } else {
                    fromIndex = 0;
                    totalCounter = 0;
                }
                //FuncionFortalezaManager.processWeakestPoblacion(poblacionByProcessedUntil, counter, fromIndex);
                poblacion.addAll(poblacionByProcessedUntil.getFirst(counter, fromIndex));
                //}
            } catch (IOException ex) {
                if (file != null) {
                    LogUtil.logTime("Error: " + file.getName(), 1);
                }
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                if (file != null) {
                    LogUtil.logTime("Error: " + file.getName(), 1);
                }
                ex.printStackTrace();
            }
        }
        return poblacion;
    }

    public Poblacion readObject(File file)
            throws IOException, ClassNotFoundException {
        ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file));
        Poblacion p = (Poblacion) reader.readObject();
        reader.close();
        return p;
    }

    public Poblacion readStrategy(File file, String id)
            throws IOException, ClassNotFoundException {
        Poblacion p = new Poblacion();
        Poblacion pFile = this.readObject(file);
        IndividuoEstrategia ie = new IndividuoEstrategia();
        ie.setId(id);
        int index = pFile.getIndividuos().indexOf(ie);
        if (index >= 0) {
            p.add(pFile.getIndividuos().get(index));
        }
        return p;
    }

    public Poblacion readByEstrategyId(String path, final String id) {
        Poblacion poblacion = new Poblacion();

        File root = new File(path);
        FilenameFilter nameFilter = new FilenameFilter() {

            public boolean accept(File dir, String name) {
                boolean accept = false;
                if ((name.contains(".gfx")) && (name.contains(PropertiesManager.getOperationType().name()))) {
                    if (name.contains(id.substring(0, id.indexOf(".")))) {
                        return true;
                    }
                }
                return accept;
            }
        };
        File[] files = root.listFiles(nameFilter);
        for (int i = 0; i < files.length; i++) {
            try {
                File file = files[i];
                Poblacion p = this.readObject(file);
                IndividuoEstrategia ie = new IndividuoEstrategia();
                ie.setId(id);
                int index = p.getIndividuos().indexOf(ie);
                if (index >= 0) {
                    IndividuoEstrategia ind = p.getIndividuos().get(index);
                    ind.setFortaleza(null);
                    ind.setListaFortaleza(null);
                    ind.setProcessedUntil(0);
                    ind.setProcessedFrom(0);
                    ind.setActiveOperation(false);
                    poblacion.add(ind);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return poblacion;
    }

    public void writeObject(String id, Poblacion poblacion, Interval<Date> dateInterval, int poblacionIndex, int poblacionFromIndex)
            throws IOException {
        ObjectOutputStream writer = new ObjectOutputStream(
                new FileOutputStream(
                PropertiesManager.getPropertyString(Constants.SERIALICE_PATH)
                + PropertiesManager.getOperationType() + PropertiesManager.getPropertyString(Constants.PAIR) + ""
                + PropertiesManager.getPropertyString(Constants.FILE_ID) + "_"
                + id + "-" + poblacionFromIndex + "-" + poblacionIndex + ".gfx"));
        writer.writeObject(poblacion);
        writer.close();
    }
}

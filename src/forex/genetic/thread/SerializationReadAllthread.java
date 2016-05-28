/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread;

import forex.genetic.entities.Poblacion;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.io.SerializationManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class SerializationReadAllthread extends Thread {

    private String path;
    private int counter;
    private int processedUntil;
    private int processedFrom;
    private SerializationManager sm = null;
    private Poblacion poblacion = null;

    public SerializationReadAllthread(String name, String path, int counter, int processedUntil, int processedFrom, SerializationManager sm) {
        super(name);
        this.path = path;
        this.counter = counter;
        this.processedUntil = processedUntil;
        this.processedFrom = processedFrom;
        this.sm = sm;
    }

    public void endProcess() {
        sm.endProcess();
    }

    public void run() {
        try {
            poblacion = sm.readAll(path, counter, processedUntil, processedFrom);
            /*if (processedUntil == PropertiesManager.getPropertyInt(Constants.INITIAL_POBLACION)) {
                poblacion = sm.readByEstrategyId(path, "1336344492091.626");
                poblacion.addAll(sm.readByEstrategyId(path, "1336344492091.621"));
            } else {
                poblacion = new Poblacion();
            }*/
            LogUtil.logTime("End Cargar poblacion serializada " + this.getName() + " Individuos=" + poblacion.getIndividuos().size(), 1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Poblacion getPoblacion() {
        return poblacion;
    }

    public int getProcessedUntil() {
        return processedUntil;
    }
}

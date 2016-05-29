/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread;

import forex.genetic.entities.Learning;
import forex.genetic.entities.Poblacion;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.io.SerializationPoblacionManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class SerializationReadAllThread extends Thread {

    private String path;
    private int counter;
    private int processedUntil;
    private int processedFrom;
    private SerializationPoblacionManager sm = new SerializationPoblacionManager();
    private Poblacion poblacion = null;
    private Learning learning = null;

    public SerializationReadAllThread(String name, String path, int counter,
            int processedUntil, int processedFrom, Learning learning) {
        super(name);
        this.path = path;
        this.counter = counter;
        this.processedUntil = processedUntil;
        this.processedFrom = processedFrom;
        this.learning = learning;
    }

    public void endProcess() {
        sm.endProcess();
    }

    public void run() {
        try {
            if (!PropertiesManager.isReadSpecific()) {
                poblacion = sm.readAll(path, counter, processedUntil, processedFrom, learning);
                LogUtil.logTime("End Cargar poblacion serializada " + this.getName() + " Individuos=" + poblacion.getIndividuos().size(), 2);
            } else {
                String testStrategy = PropertiesManager.getPropertyString(Constants.TEST_STRATEGY);
                String[] ids = testStrategy.split(",");
                if (processedUntil == PropertiesManager.getPropertyInt(Constants.INITIAL_POBLACION)) {
                    poblacion = new Poblacion();
                    for (int k = 0; k < ids.length; k++) {
                        String id = ids[k];
                        poblacion.addAll(sm.readByEstrategyId(path, id));
                    }
                }
            }
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

    public Poblacion getPoblacionHija() {
        return sm.getPoblacionHija();
    }

    public Poblacion getPoblacionPadre() {
        return sm.getPoblacionPadre();
    }
}

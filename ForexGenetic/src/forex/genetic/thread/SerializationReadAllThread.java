/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread;
	
import java.io.File;
import java.io.IOException;

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

    /**
     *
     * @param name
     * @param path
     * @param counter
     * @param processedUntil
     * @param processedFrom
     * @param learning
     */
    public SerializationReadAllThread(String name, String path, int counter,
            int processedUntil, int processedFrom, Learning learning) {
        super(name);
        this.path = path;
        this.counter = counter;
        this.processedUntil = processedUntil;
        this.processedFrom = processedFrom;
        this.learning = learning;
    }

    /**
     *
     */
    public void endProcess() {
        sm.endProcess();
    }

    @Override
    public void run() {
        try {
            poblacion = new Poblacion();
            String testFile = PropertiesManager.getPropertyString(Constants.TEST_FILE);
            String testStrategy = PropertiesManager.getPropertyString(Constants.TEST_STRATEGY);
            if ((testStrategy != null) && (!testStrategy.isEmpty()) && (testFile != null) && (!testFile.isEmpty())) {
                String[] ids = testStrategy.split(",");
                String[] files = testFile.split(",");
                String serPath = PropertiesManager.getSerialicePath();
                if (processedUntil == PropertiesManager.getPropertyInt(Constants.INITIAL_POBLACION)) {
                    for (int k = 0; k < ids.length; k++) {
                        String id = ids[k];
                        String f = files[k];
                        poblacion.addAll(sm.readStrategy(new File(
                                serPath
                                + File.separatorChar
                                + f), id));
                        //poblacion.addAll(sm.readByEstrategyId(path, id));
                    }
                }
            }
            if (!PropertiesManager.isReadSpecific()) {
                poblacion.addAll(sm.readAll(path, counter, processedUntil, processedFrom, learning));
                LogUtil.logTime("End Cargar poblacion serializada " + this.getName() + " Individuos=" + poblacion.getIndividuos().size(), 2);
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public Poblacion getPoblacion() {
        return poblacion;
    }

    /**
     *
     * @return
     */
    public int getProcessedUntil() {
        return processedUntil;
    }

    /**
     *
     * @return
     */
    public Poblacion getPoblacionHija() {
        return sm.getPoblacionHija();
    }

    /**
     *
     * @return
     */
    public Poblacion getPoblacionPadre() {
        return sm.getPoblacionPadre();
    }
}

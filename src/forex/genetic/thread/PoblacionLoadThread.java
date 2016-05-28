/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread;

import forex.genetic.entities.Poblacion;
import forex.genetic.manager.PoblacionManager;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class PoblacionLoadThread extends Thread {

    private Poblacion poblacion = null;
    private PoblacionManager poblacionManager;
    private int poblacionIndex;

    public PoblacionLoadThread(PoblacionManager poblacionManager, Poblacion poblacion, int poblacionIndex) {
        this.poblacionManager = poblacionManager;
        this.poblacion = poblacion;
        this.poblacionIndex = poblacionIndex;        
    }        

    public void run() {
        LogUtil.logTime("\n Crear poblacion " + poblacionIndex);
        poblacionManager.load("" + poblacionIndex, true);
        LogUtil.logTime("Crear poblacion " + poblacionIndex);

        if (poblacionManager.getPoblacion() != null) {
            poblacion.addAll(poblacionManager.getPoblacion());
            LogUtil.logTime(" Fecha = " + poblacionManager.getDateInterval());
        } else {
            LogUtil.logTime("\n");
        }
    }

    public int getPoblacionIndex() {
        return poblacionIndex;
    }

    public void setPoblacionIndex(int poblacionIndex) {
        this.poblacionIndex = poblacionIndex;
    }

    public PoblacionManager getPoblacionManager() {
        return poblacionManager;
    }

    public void setPoblacionManager(PoblacionManager poblacionManager) {
        this.poblacionManager = poblacionManager;
    }

    public Poblacion getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(Poblacion poblacion) {
        this.poblacion = poblacion;
    }
}

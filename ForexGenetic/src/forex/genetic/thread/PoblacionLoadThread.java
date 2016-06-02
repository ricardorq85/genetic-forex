/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread;

import forex.genetic.manager.PoblacionManager;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class PoblacionLoadThread extends Thread {

    private PoblacionManager poblacionManager;
    private int poblacionIndex;
    private boolean poblar;

    /**
     *
     * @param name
     * @param poblacionManager
     * @param poblacionIndex
     * @param poblar
     */
    public PoblacionLoadThread(String name, PoblacionManager poblacionManager, int poblacionIndex, boolean poblar) {
        super(name);
        this.poblacionManager = poblacionManager;
        this.poblacionIndex = poblacionIndex;
        this.poblar = poblar;
    }

    @Override
    public void run() {
        LogUtil.logTime("Loading poblacion " + poblacionIndex, 1);
        poblacionManager.load("" + poblacionIndex, poblar);
        if (poblacionManager.getPoblacion() != null) {
            LogUtil.logTime("Loaded poblacion " + poblacionIndex + " " + poblacionManager.getPoblacion().getIndividuos().size(), 3);
        }
    }

    /**
     *
     * @return
     */
    public PoblacionManager getPoblacionManager() {
        return poblacionManager;
    }
}

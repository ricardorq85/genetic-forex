/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.controller;

import forex.genetic.manager.indicator.IndicadorManager;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public abstract class IndicadorController {

    /**
     *
     */
    protected static List<IndicadorManager> list;

    /**
     *
     */
    protected String nombreTabla;

    /**
     *
     * @param nombreTabla
     */
    public IndicadorController(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    /**
     *
     * @return
     */
    public String getNombreTabla() {
        return nombreTabla;
    }

    /**
     *
     * @return
     */
    public int getIndicatorNumber() {
        int indicatorNumber = 0;
        synchronized (this) {
            if (list == null) {
                load();
            }
            indicatorNumber = list.size();
        }
        return indicatorNumber;
    }

    /**
     *
     * @param i
     * @return
     */
    public IndicadorManager getManagerInstance(int i) {
        IndicadorManager im = null;
        synchronized (this) {
            if (list == null) {
                load();
            }
            im = list.get(i);
        }
        return im;
    }

    /**
     *
     */
    protected abstract void load();
}

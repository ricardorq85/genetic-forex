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
 * @author USER
 */
public abstract class IndicadorController {

    protected List<IndicadorManager> list;
    protected String nombreTabla;

    public IndicadorController(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public int getIndicatorNumber() {
        if (list == null) {
            load();
        }
        return list.size();
    }

    public IndicadorManager getManagerInstance(int i) {
        if (list == null) {
            load();
        }
        return list.get(i);
    }

    protected abstract void load();
}

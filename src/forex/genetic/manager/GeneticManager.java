/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.Learning;
import forex.genetic.util.LogUtil;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.indicator.IndicadorManager;
import forex.genetic.util.NumberUtil;
import java.util.List;
import java.util.Random;
import static forex.genetic.util.Constants.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author ricardorq85
 */
public abstract class GeneticManager {

    protected boolean endProcess = false;
    protected final IndicadorController indicadorController;

    public GeneticManager(IndicadorController indicadorController) {
        this.indicadorController = indicadorController;
    }

    public synchronized void endProcess() {
        this.endProcess = true;
    }
}

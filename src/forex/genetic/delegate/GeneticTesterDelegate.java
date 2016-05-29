/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.delegate;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.Point;
import forex.genetic.manager.FuncionFortalezaManager;
import forex.genetic.manager.PoblacionManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class GeneticTesterDelegate extends GeneticDelegate {
    
    public GeneticTesterDelegate() throws FileNotFoundException {
        super(false);
    }

    public void process(IndividuoEstrategia individuoEstrategia) throws FileNotFoundException, IOException {
        FuncionFortalezaManager funcionFortalezaManager = new FuncionFortalezaManager();
        funcionFortalezaManager.setTest(true);
        for (int poblacionIndex = PropertiesManager.getPropertyInt(Constants.INITIAL_POBLACION);
                poblacionIndex <= PropertiesManager.getPropertyInt(Constants.END_POBLACION) && !PropertiesManager.getPropertyBoolean(Constants.TERMINAR); poblacionIndex++) {
            LogUtil.logTime("Crear poblacion " + poblacionIndex, 1);
            PoblacionManager poblacionManager = new PoblacionManager();
            poblacionManager.load("" + poblacionIndex, false);
            LogUtil.logTime("Crear poblacion " + poblacionIndex + " Fecha = " + poblacionManager.getDateInterval(), 1);
            /** Se calcula la fortaleza de los individuos */
            LogUtil.logTime("Calcular fortaleza", 1);
            funcionFortalezaManager.calculateFortaleza(poblacionManager.getPoints(), individuoEstrategia,
                    (poblacionIndex == PropertiesManager.getPropertyInt(Constants.INITIAL_POBLACION)),
                    (poblacionIndex > PropertiesManager.getPropertyInt(Constants.INITIAL_POBLACION)), 
                    poblacionIndex,
                    PropertiesManager.getPropertyInt(Constants.INITIAL_POBLACION));
            if ((poblacionIndex > individuoEstrategia.getProcessedUntil())) {
                individuoEstrategia.setProcessedUntil(poblacionIndex);
            }
            super.outIndividuo(individuoEstrategia);
            try {
                fileOutManager.write(individuoEstrategia, poblacionManager.getDateInterval(), true, 0);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }

    public void process(Poblacion poblacion, int poblacionIndex, int endPoblacionIndex) {
        this.process(poblacion, poblacionIndex, endPoblacionIndex, 0);
    }

    public void process(Poblacion poblacion, int poblacionIndex, int endPoblacionIndex, int indexPoint) {
        this.process(poblacion, poblacionIndex, endPoblacionIndex, false, indexPoint);
    }

    public void process(Poblacion poblacion, int poblacionIndex, int endPoblacionIndex, boolean onlyClose) {
        this.process(poblacion, poblacionIndex, endPoblacionIndex, onlyClose, 0);
    }

    public void process(Poblacion poblacion, int poblacionIndex, int endPoblacionIndex, boolean onlyClose, int indexPoint) {
        FuncionFortalezaManager funcionFortalezaManager = new FuncionFortalezaManager();
        funcionFortalezaManager.setTest(true);
        funcionFortalezaManager.setOnlyClose(onlyClose);
        for (int i = poblacionIndex;
                i <= endPoblacionIndex && !PropertiesManager.getPropertyBoolean(Constants.TERMINAR); i++) {
            LogUtil.logTime("Crear poblacion " + i, 1);
            PoblacionManager poblacionManager = new PoblacionManager();
            poblacionManager.load("" + i, false);
            if (poblacionManager.getPoints() != null) {
                LogUtil.logTime("Crear poblacion " + i + " Fecha = " + poblacionManager.getDateInterval(), 1);
                /** Se calcula la fortaleza de los individuos */
                LogUtil.logTime("Calcular fortaleza", 1);
                List<Point> points = poblacionManager.getPoints();
                funcionFortalezaManager.calculateFortaleza(points, poblacion, !onlyClose, i, poblacionIndex, indexPoint);
            }
        }
    }
}

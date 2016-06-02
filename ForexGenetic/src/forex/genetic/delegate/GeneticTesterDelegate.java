/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.delegate;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.entities.Point;
import forex.genetic.manager.FuncionFortalezaManager;
import forex.genetic.manager.PatternManager;
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

    private PatternManager patternManager = new PatternManager();

    /**
     *
     * @throws FileNotFoundException
     */
    public GeneticTesterDelegate() throws FileNotFoundException {
        super(false);
        patternManager.initPatternManager();
    }

    /**
     *
     * @param individuoEstrategia
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void process(IndividuoEstrategia individuoEstrategia) throws FileNotFoundException, IOException {
        FuncionFortalezaManager funcionFortalezaManager = new FuncionFortalezaManager();
        funcionFortalezaManager.setTest(true);
        int endPoblacion = PropertiesManager.getPropertyInt(Constants.END_POBLACION);
        for (int poblacionIndex = PropertiesManager.getPropertyInt(Constants.INITIAL_POBLACION);
                poblacionIndex <= endPoblacion && !PropertiesManager.getPropertyBoolean(Constants.TERMINAR); poblacionIndex++) {
            LogUtil.logTime("Crear poblacion " + poblacionIndex, 1);
            PoblacionManager poblacionManager = new PoblacionManager();
            poblacionManager.load("" + poblacionIndex, false);
            PoblacionManager nextPoblacionManager = new PoblacionManager();
            if (poblacionIndex < endPoblacion) {
                nextPoblacionManager.load("" + (poblacionIndex + 1), false);
            }
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
            if (poblacionIndex > 145) {
                int r = 88;
            }
            patternManager.processPatterns(individuoEstrategia);
            super.outIndividuo(individuoEstrategia);
            try {
                fileOutManager.write(individuoEstrategia, nextPoblacionManager.getDateInterval(), true, 0);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     *
     * @param poblacion
     * @param poblacionIndex
     * @param endPoblacionIndex
     */
    public void process(Poblacion poblacion, int poblacionIndex, int endPoblacionIndex) {
        this.process(poblacion, poblacionIndex, endPoblacionIndex, 0);
    }

    /**
     *
     * @param poblacion
     * @param poblacionIndex
     * @param endPoblacionIndex
     * @param indexPoint
     */
    public void process(Poblacion poblacion, int poblacionIndex, int endPoblacionIndex, int indexPoint) {
        this.process(poblacion, poblacionIndex, endPoblacionIndex, false, indexPoint);
    }

    /**
     *
     * @param poblacion
     * @param poblacionIndex
     * @param endPoblacionIndex
     * @param onlyClose
     */
    public void process(Poblacion poblacion, int poblacionIndex, int endPoblacionIndex, boolean onlyClose) {
        this.process(poblacion, poblacionIndex, endPoblacionIndex, onlyClose, 0);
    }

    /**
     *
     * @param poblacion
     * @param poblacionIndex
     * @param endPoblacionIndex
     * @param onlyClose
     * @param indexPoint
     */
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

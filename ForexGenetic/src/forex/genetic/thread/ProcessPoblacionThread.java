package forex.genetic.thread;

import java.util.List;

import forex.genetic.entities.Poblacion;
import forex.genetic.entities.Point;
import forex.genetic.manager.FuncionFortalezaManager;
import forex.genetic.manager.PatternManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class ProcessPoblacionThread extends Thread {

    private List<Point> points;

    /**
     *
     */
    protected Poblacion poblacion;

    /**
     *
     */
    protected Poblacion newPoblacion = new Poblacion();

    /**
     *
     */
    protected Poblacion poblacionHija = new Poblacion();

    /**
     *
     */
    protected Poblacion poblacionPadre = new Poblacion();
    private boolean recalculate;
    private boolean processPattern;
    private int poblacionIndex;
    private int poblacionFromIndex;
    private FuncionFortalezaManager ffm;
    private static PatternManager patternManager = new PatternManager();

    /**
     *
     * @param name
     */
    public ProcessPoblacionThread(String name) {
        super(name);
    }

    /**
     *
     * @param name
     * @param points
     * @param poblacion
     * @param recalculate
     * @param poblacionIndex
     * @param poblacionFromIndex
     * @param ffm
     */
    public ProcessPoblacionThread(String name, List<Point> points,
            Poblacion poblacion, boolean recalculate, int poblacionIndex, int poblacionFromIndex,
            FuncionFortalezaManager ffm) {
        super(name);
        this.points = points;
        LogUtil.logTime(this.getName() + " : Points=" + this.points.size(), 5);
        this.poblacion = poblacion;
        this.recalculate = recalculate;
        this.poblacionIndex = poblacionIndex;
        this.poblacionFromIndex = poblacionFromIndex;
        this.ffm = ffm;
    }

    @Override
    public void run() {
        LogUtil.logTime("Procesar Generacion " + this.getName() + " Individuos=" + poblacion.getIndividuos().size(), 3);
        LogUtil.logTime("Calcular fortaleza " + this.getName() + " Individuos=" + poblacion.getIndividuos().size() + " Points=" + points.size(), 2);
        ffm.calculateFortaleza(points, poblacion, recalculate, poblacionIndex, poblacionFromIndex);
        if (processPattern) {
            if (PropertiesManager.getFortalezaType().equals(Constants.FortalezaType.PatternAdvanced)) {
                if (poblacionIndex > 34) {
                    int r = 88;
                }
                patternManager.processPattern(poblacion);
            }
        }
        LogUtil.logTime(this.getName() + " : Points=" + this.points.size(), 3);
        LogUtil.logTime("Procesar invalidos " + this.getName() + " Individuos=" + poblacion.getIndividuos().size(), 3);
        ffm.processInvalids(poblacion);
        LogUtil.logTime(this.getName() + " Individuos=" + poblacion.getIndividuos().size() + " Points=" + this.points.size(), 5);
    }

    /**
     *
     * @return
     */
    public boolean isProcessPattern() {
        return processPattern;
    }

    /**
     *
     * @param processPattern
     */
    public void setProcessPattern(boolean processPattern) {
        this.processPattern = processPattern;
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
    public Poblacion getNewPoblacion() {
        return newPoblacion;
    }

    /**
     *
     * @return
     */
    public Poblacion getPoblacionHija() {
        return poblacionHija;
    }

    /**
     *
     * @return
     */
    public Poblacion getPoblacionPadre() {
        return poblacionPadre;
    }

    /**
     *
     * @return
     */
    public int getPoblacionIndex() {
        return poblacionIndex;
    }

    /**
     *
     */
    public void endProcess() {
    }
}

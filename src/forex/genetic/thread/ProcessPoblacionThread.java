package forex.genetic.thread;

import forex.genetic.entities.Poblacion;
import forex.genetic.entities.Point;
import forex.genetic.manager.FuncionFortalezaManager;
import forex.genetic.util.LogUtil;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class ProcessPoblacionThread extends Thread {

    private List<Point> points;
    protected Poblacion poblacion;
    protected Poblacion newPoblacion = new Poblacion();
    protected Poblacion poblacionHija = new Poblacion();
    protected Poblacion poblacionPadre = new Poblacion();
    private boolean recalculate;
    private int poblacionIndex;
    private int poblacionFromIndex;
    private FuncionFortalezaManager ffm;

    public ProcessPoblacionThread(String name) {
        super(name);
    }

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

    public void run() {
        LogUtil.logTime("Procesar Generacion " + this.getName() + " Individuos=" + poblacion.getIndividuos().size(), 3);
        LogUtil.logTime("Calcular fortaleza " + this.getName() + " Individuos=" + poblacion.getIndividuos().size() + " Points=" + points.size(), 2);
        ffm.calculateFortaleza(points, poblacion, recalculate, poblacionIndex, poblacionFromIndex);
        LogUtil.logTime(this.getName() + " : Points=" + this.points.size(), 3);
        LogUtil.logTime("Procesar invalidos " + this.getName() + " Individuos=" + poblacion.getIndividuos().size(), 3);
        ffm.processInvalids(poblacion);
        LogUtil.logTime(this.getName() + " Individuos=" + poblacion.getIndividuos().size() + " Points=" + this.points.size(), 5);
    }

    public Poblacion getPoblacion() {
        return poblacion;
    }

    public Poblacion getNewPoblacion() {
        return newPoblacion;
    }

    public Poblacion getPoblacionHija() {
        return poblacionHija;
    }

    public Poblacion getPoblacionPadre() {
        return poblacionPadre;
    }

    public int getPoblacionIndex() {
        return poblacionIndex;
    }

    public void endProcess() {
    }
}

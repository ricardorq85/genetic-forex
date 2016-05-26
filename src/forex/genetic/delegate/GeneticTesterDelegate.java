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
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class GeneticTesterDelegate {

    public void process(Poblacion poblacion) {
        this.process(null, poblacion);
    }

    public void process(List<Point> p, Poblacion poblacion) {
        List<Point> points = null;
        if (p == null) {
            PoblacionManager poblacionManager = PoblacionManager.getInstance();
            points = poblacionManager.getPoints();
        } else {
            points = p;
        }

        FuncionFortalezaManager funcionFortalezaManager = new FuncionFortalezaManager();
        funcionFortalezaManager.calculateFortaleza(points, poblacion, true);
    }

    public void process(IndividuoEstrategia individuoEstrategia) {
        PoblacionManager poblacionManager = PoblacionManager.getInstance(false);

        FuncionFortalezaManager funcionFortalezaManager = new FuncionFortalezaManager();
        funcionFortalezaManager.calculateFortaleza(poblacionManager.getPoints(), individuoEstrategia, true);
    }

    public void process(List<Point> p, IndividuoEstrategia individuoEstrategia) {
        List<Point> points = null;
        if (p == null) {
            PoblacionManager poblacionManager = PoblacionManager.getInstance();
            points = poblacionManager.getPoints();
        } else {
            points = p;
        }

        FuncionFortalezaManager funcionFortalezaManager = new FuncionFortalezaManager();
        funcionFortalezaManager.calculateFortaleza(points, individuoEstrategia);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Indicator;
import java.util.List;

/**
 *
 * @author ricardorq85
 * @param <E> Tipo indicador
 */
public abstract class IndicadorManager<E> {

    private boolean priceDependence = false;
    private boolean obligatory = false;
    protected String id = null;

    protected IndicadorManager(boolean priceDependence) {
        this(priceDependence, false);
    }

    protected IndicadorManager(boolean priceDependence, boolean obligatory) {
        this.priceDependence = priceDependence;
        this.obligatory = obligatory;
    }

    public boolean operate(E individuo, E indicator, List<Point> points, int i) {
        return true;
    }

    public boolean isPriceDependence() {
        return priceDependence;
    }

    public void setPriceDependence(boolean priceDependence) {
        this.priceDependence = priceDependence;
    }

    public boolean isObligatory() {
        return obligatory;
    }

    public void setObligatory(boolean obligatory) {
        this.obligatory = obligatory;
    }

    public String getId() {
        return id;
    }

    public abstract Indicator generate(E indicator, Point point);

    public boolean operate(E individuo, E indicator, Point point) {
        return true;
    }

    public boolean operate(E individuo, E indicator, Point currentPoint, Point previousPoint) {
        return true;
    }

    public abstract Interval calculateInterval(E individuo, E indicator, Point point);

    public abstract Indicator crossover(E indicator1, E indicator2);

    public abstract Indicator mutate(E indicator);

    public abstract Indicator optimize(E individuo, E optimizedIndividuo, E indicator, Point prevPoint, Point point, double pips);

    public abstract void round(E indicator);

    public abstract double getValue(E indicator, Point prevPoint, Point point);
}

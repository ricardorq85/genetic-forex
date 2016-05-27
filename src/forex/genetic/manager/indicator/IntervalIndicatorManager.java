/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.manager.IntervalManager;

/**
 *
 * @author ricardorq85
 */
public abstract class IntervalIndicatorManager<E extends IntervalIndicator> extends IndicatorManager<E> {

    protected IntervalManager intervalManager = null;

    public IntervalIndicatorManager(boolean priceDependence, String name) {
        super(priceDependence);
        this.intervalManager = new IntervalManager(name);
    }

    public abstract IntervalIndicator getIndicatorInstance ();

    public Indicator crossover(E obj1, E obj2) {
        IntervalIndicator objHijo = this.getIndicatorInstance();
        Interval interval = null;
        if ((obj1 == null) && (obj2 == null)) {
            objHijo = null;
        } else {
            interval = intervalManager.crossover(
                    (obj1 == null) ? null : obj1.getInterval(),
                    (obj2 == null) ? null : obj2.getInterval());
            objHijo.setInterval(interval);
        }
        return objHijo;
    }

    public Indicator mutate(E obj) {
        IntervalIndicator objHijo = this.getIndicatorInstance();
        Interval interval = intervalManager.mutate((obj == null) ? null : obj.getInterval());
        objHijo.setInterval(interval);
        return objHijo;
    }

    public Interval calculateInterval(E objIndividuo, E iE, Point point) {
        throw new UnsupportedOperationException("Not supported.");
    }
}

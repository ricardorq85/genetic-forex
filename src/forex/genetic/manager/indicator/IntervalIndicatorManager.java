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
 * @param <E> Tipo
 */
public abstract class IntervalIndicatorManager<E extends IntervalIndicator> extends IndicadorIndividuoManager<E> {

    protected IntervalManager intervalManager = null;

    public IntervalIndicatorManager(boolean priceDependence, String name) {
        this(priceDependence, false, name);
    }

    public IntervalIndicatorManager(boolean priceDependence, boolean obligatory, String name) {
        super(priceDependence, obligatory);
        this.intervalManager = new IntervalManager(name);
    }

    public IntervalIndicatorManager() {
        super(false, false);
    }

    public IntervalManager getIntervalManager() {
        return intervalManager;
    }

    public void setIntervalManager(IntervalManager intervalManager) {
        this.intervalManager = intervalManager;
    }

    public abstract IntervalIndicator getIndicatorInstance();

    @Override
    public Indicator crossover(E obj1, E obj2) {
        IntervalIndicator objHijo = this.getIndicatorInstance();
        Interval interval;
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

    @Override
    public Indicator mutate(E obj) {
        IntervalIndicator objHijo = this.getIndicatorInstance();
        Interval interval = intervalManager.mutate((obj == null) ? null : obj.getInterval());
        objHijo.setInterval(interval);
        return objHijo;
    }

    @Override
    public Interval calculateInterval(E objIndividuo, E iE, Point point) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void round(E indicator) {
        if (indicator != null) {
            Interval<Double> interval = indicator.getInterval();
            if (interval != null) {
                intervalManager.round(interval);
            }
        }
    }

    @Override
    public Indicator optimize(E individuo, E optimizedIndividuo, E indicator, Point prevPoint, Point point, double pips) {
        IntervalIndicator optimized = getIndicatorInstance();
        double value = getValue(indicator, prevPoint, point);
        Interval generated = intervalManager.generate(value, (isPriceDependence()) ? point.getLow() : 0.0, (isPriceDependence()) ? point.getHigh() : 0.0);
        intervalManager.round(generated);
        if (pips > 0) {
            Interval intersected = IntervalManager.intersect(generated, individuo.getInterval());
            Interval intervalOptimized = intervalManager.optimize((optimizedIndividuo == null) ? null : optimizedIndividuo.getInterval(), intersected);
            optimized.setInterval(intervalOptimized);
        } else {
            if (optimizedIndividuo != null) {
                Interval difference = IntervalManager.difference(optimizedIndividuo.getInterval(), generated);
                optimized.setInterval(difference);
            }
        }
        if ((optimized.getInterval() == null) || (optimized.getInterval().getLowInterval() == null) || (optimized.getInterval().getHighInterval() == null)) {
            optimized = optimizedIndividuo;
        }
        return optimized;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.manager.indicator.IndicadorIndividuoManager;

/**
 *
 * @author ricardorq85
 */
public class DummyIndicatorManager extends IndicadorIndividuoManager<Object> {

    /**
     *
     * @param priceDependence
     */
    public DummyIndicatorManager(boolean priceDependence) {
        super(priceDependence);
    }

    /**
     *
     * @param indicator
     * @param point
     * @return
     */
    @Override
    public Indicator generate(Object indicator, Point point) {
        return null;
    }

    /**
     *
     * @param objIndividuo
     * @param obj
     * @param point
     * @return
     */
    @Override
    public boolean operate(Object objIndividuo, Object obj, Point point) {
        return true;
    }

    /**
     *
     * @param obj1
     * @param obj2
     * @return
     */
    @Override
    public Indicator crossover(Object obj1, Object obj2) {
        return null;
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public Indicator mutate(Object obj) {
        return null;
    }

    /**
     *
     * @param individuo
     * @param indicator
     * @param point
     * @return
     */
    @Override
    public Interval calculateInterval(Object individuo, Object indicator, Point point) {
        throw new UnsupportedOperationException("Not supported.");
    }

    /**
     *
     * @param indicator
     */
    @Override
    public void round(Object indicator) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param individuo
     * @param optimizedIndividuo
     * @param indicator
     * @param prevPoint
     * @param point
     * @param pips
     * @return
     */
    @Override
    public Indicator optimize(Object individuo, Object optimizedIndividuo, Object indicator, Point prevPoint, Point point, double pips) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @param indicator
     * @param prevPoint
     * @param point
     * @return
     */
    @Override
    public double getValue(Object indicator, Point prevPoint, Point point) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

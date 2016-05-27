/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Sar;

/**
 *
 * @author ricardorq85
 */
public class SarIndicatorManager extends IntervalIndicatorManager<Sar> {

    protected SarIndicatorManager(boolean priceDependence) {
        super(priceDependence, "Sar");
    }

    public Sar getIndicatorInstance() {
        return new Sar("Sar");
    }

    public Indicator generate(Sar indicator, Point point) {
        Interval interval = null;
        Sar sar = new Sar("Sar");
        if (indicator != null) {
            sar.setSar(indicator.getSar());
            interval = intervalManager.generate(indicator.getSar(), point.getLow(), point.getHigh());
        } else {
            interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
        }
        sar.setInterval(interval);

        return sar;
    }

    @Override
    public boolean operate(Sar sarIndividuo, Sar iSar, Point point) {
        return intervalManager.operate(sarIndividuo.getInterval(), iSar.getSar(), point);
    }

    @Override
    public Interval calculateInterval(Sar sarIndividuo, Sar iSar, Point point) {
        return intervalManager.calculateInterval(sarIndividuo.getInterval(), iSar.getSar(), point);
    }
}

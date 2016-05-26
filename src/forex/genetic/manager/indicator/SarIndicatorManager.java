/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.Indicator;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Sar;
import forex.genetic.manager.IntervalManager;
import static forex.genetic.util.Constants.PriceType;

/**
 *
 * @author ricardorq85
 */
public class SarIndicatorManager extends IndicatorManager<Sar> {

    private IntervalManager intervalManager = new IntervalManager();
    private PriceType type = null;

    protected SarIndicatorManager(PriceType type, boolean priceDependence) {
        super(priceDependence);
        this.type = type;
    }

    public Indicator generate(Sar indicator, Point point) {
        Interval interval = null;
        Sar sar = new Sar();
        if (indicator != null) {
            sar.setSar(indicator.getSar());
            interval = intervalManager.generate(indicator.getSar(), (type.equals(PriceType.COMPARE_CLOSE)) ? point.getCloseCompare() : point.getHigh(), (type.equals(PriceType.COMPARE_CLOSE)) ? point.getCloseCompare() : point.getLow());
        } else {
            interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
        }
        sar.setInterval(interval);

        return sar;
    }

    public boolean operate(Sar sarIndividuo, Sar iSar, Point point) {
        if (type.equals(PriceType.COMPARE_CLOSE)) {
            return intervalManager.operate(sarIndividuo.getInterval(), iSar.getSar(), point.getCloseCompare());
        } else {
            return intervalManager.operate(sarIndividuo.getInterval(), iSar.getSar(), point);
        }
    }

    public Indicator crossover(Sar sar1, Sar sar2) {
        Sar avgHijo = new Sar();
        Interval interval = null;
        if ((sar1 == null) && (sar2 == null)) {
            avgHijo = null;
        } else {
            interval = intervalManager.crossover(
                    (sar1 == null) ? null : sar1.getInterval(),
                    (sar2 == null) ? null : sar2.getInterval());
            avgHijo.setInterval(interval);
        }
        return avgHijo;
    }

    public Indicator mutate(Sar sar) {
        Sar avgHijo = new Sar();
        Interval interval = intervalManager.mutate((sar == null) ? null : sar.getInterval());
        avgHijo.setInterval(interval);
        return avgHijo;
    }

    public Interval calculateInterval(Sar sarIndividuo, Sar iSar, Point point) {
        return intervalManager.calculateInterval(sarIndividuo.getInterval(), iSar.getSar(), point);
    }
}

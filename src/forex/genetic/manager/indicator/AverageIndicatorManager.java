/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Average;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import static forex.genetic.util.Constants.PriceType;

/**
 *
 * @author ricardorq85
 */
public class AverageIndicatorManager extends IntervalIndicatorManager<Average> {

    private PriceType type = null;

    protected AverageIndicatorManager(PriceType type, boolean priceDependence) {
        super(priceDependence, (type.equals(PriceType.COMPARE_CLOSE))
                ? "MaCompare"
                : "Ma");
        this.type = type;
    }

    public Average getIndicatorInstance() {
        return (type.equals(PriceType.COMPARE_CLOSE))
                ? new Average("MaCompare")
                : new Average("Ma");
    }

    public Indicator generate(Average indicator, Point point) {
        Interval interval = null;
        Average average = (type.equals(PriceType.COMPARE_CLOSE))
                ? new Average("MaCompare")
                : new Average("Ma");
        if (indicator != null) {
            average.setAverage(indicator.getAverage());
            interval = intervalManager.generate(indicator.getAverage(),
                    (type.equals(PriceType.COMPARE_CLOSE)) ? point.getCloseCompare() : point.getLow(),
                    (type.equals(PriceType.COMPARE_CLOSE)) ? Double.NaN : point.getHigh());
        } else {
            interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
        }
        average.setInterval(interval);

        return average;
    }

    @Override
    public boolean operate(Average averageIndividuo, Average iAverage, Point currentPoint, Point previousPoint) {
        if (type.equals(PriceType.COMPARE_CLOSE)) {
            return intervalManager.operate(averageIndividuo.getInterval(), iAverage.getAverage(), previousPoint.getCloseCompare());
        } else {
            return intervalManager.operate(averageIndividuo.getInterval(), iAverage.getAverage(), currentPoint);
        }
    }

    @Override
    public Interval calculateInterval(Average averageIndividuo, Average iAverage, Point point) {
        return intervalManager.calculateInterval(averageIndividuo.getInterval(), iAverage.getAverage(), point);
    }
}

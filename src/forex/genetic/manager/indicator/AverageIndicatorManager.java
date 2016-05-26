/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Average;
import forex.genetic.entities.Indicator;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.manager.IntervalManager;
import static forex.genetic.util.Constants.PriceType;

/**
 *
 * @author ricardorq85
 */
public class AverageIndicatorManager extends IndicatorManager<Average> {

    private IntervalManager intervalManager = new IntervalManager();
    private PriceType type = null;

    protected AverageIndicatorManager(PriceType type, boolean priceDependence) {
        super(priceDependence);
        this.type = type;
    }

    public Indicator generate(Average indicator, Point point) {
        Interval interval = null;
        Average average = new Average();
        if (indicator != null) {
            average.setAverage(indicator.getAverage());
            interval = intervalManager.generate(indicator.getAverage(), (type.equals(PriceType.COMPARE_CLOSE))
                    ? point.getCloseCompare() : point.getHigh(), (type.equals(PriceType.COMPARE_CLOSE)) ? Double.NaN : point.getLow());
        } else {
            interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
        }
        average.setInterval(interval);

        return average;
    }

    public boolean operate(Average averageIndividuo, Average iAverage, Point point) {
        if (type.equals(PriceType.COMPARE_CLOSE)) {
            return intervalManager.operate(averageIndividuo.getInterval(), iAverage.getAverage(), point.getCloseCompare());
        } else {
            return intervalManager.operate(averageIndividuo.getInterval(), iAverage.getAverage(), point);
        }
    }

    public Indicator crossover(Average average1, Average average2) {
        Average avgHijo = new Average();
        Interval interval = null;
        if ((average1 == null) && (average2 == null)) {
            avgHijo = null;
        } else {
            interval = intervalManager.crossover(
                    (average1 == null) ? null : average1.getInterval(),
                    (average2 == null) ? null : average2.getInterval());
            avgHijo.setInterval(interval);
        }
        return avgHijo;
    }

    public Indicator mutate(Average average) {
        Average avgHijo = new Average();
        Interval interval = intervalManager.mutate((average == null) ? null : average.getInterval());
        avgHijo.setInterval(interval);
        return avgHijo;
    }

    public Interval calculateInterval(Average averageIndividuo, Average iAverage, Point point) {
        return intervalManager.calculateInterval(averageIndividuo.getInterval(), iAverage.getAverage(), point);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Average;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;

/**
 *
 * @author ricardorq85
 */
public class MaIndicatorManager extends IntervalIndicatorManager<Average> {

    public MaIndicatorManager() {
        super(true, false, "Ma");
        this.id = "MA";
    }

    public Average getIndicatorInstance() {
        return new Average("Ma");
    }

    public Indicator generate(Average indicator, Point point) {
        Interval interval = null;
        Average average = getIndicatorInstance();
        if (indicator != null) {
            average.setAverage(indicator.getAverage());
            interval = intervalManager.generate(indicator.getAverage(), point.getLow(), point.getHigh());
        } else {
            interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
        }
        average.setInterval(interval);

        return average;
    }

    @Override
    public boolean operate(Average averageIndividuo, Average iAverage, Point currentPoint, Point previousPoint) {
        return intervalManager.operate(averageIndividuo.getInterval(), iAverage.getAverage(), currentPoint);
    }

    @Override
    public Interval calculateInterval(Average averageIndividuo, Average iAverage, Point point) {
        return intervalManager.calculateInterval(averageIndividuo.getInterval(), iAverage.getAverage(), point);
    }

    @Override
    public double getValue(Average indicator, Point prevPoint, Point point) {
        double value;
        value = indicator.getAverage();
        return value;
    }
}

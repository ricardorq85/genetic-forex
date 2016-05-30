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
public class CompareMaIndicatorManager extends IntervalIndicatorManager<Average> {

    public CompareMaIndicatorManager() {
        super(false, false, "MaCompare");
        this.id = "COMPARE_MA";
    }

    @Override
    public Average getIndicatorInstance() {
        return new Average("MaCompare");
    }

    @Override
    public Indicator generate(Average indicator, Point point) {
        Interval interval = null;
        Average average = getIndicatorInstance();
        if (indicator != null) {
            average.setAverage(indicator.getAverage());
            double value = indicator.getAverage() - point.getCloseCompare();
            interval = intervalManager.generate(value, -value * 0.1, value * 0.1);
        } else {
            interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
        }
        average.setInterval(interval);

        return average;
    }

    @Override
    public boolean operate(Average averageIndividuo, Average iAverage, Point currentPoint, Point previousPoint) {
        return intervalManager.operate(averageIndividuo.getInterval(), iAverage.getAverage() - previousPoint.getCloseCompare(), 0.0);
    }

    @Override
    public double getValue(Average indicator, Point prevPoint, Point point) {
        double value;
        value = indicator.getAverage() - prevPoint.getCloseCompare();
        return value;
    }
}

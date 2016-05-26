/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.Average;
import forex.genetic.entities.Indicator;
import forex.genetic.entities.Point;
import forex.genetic.util.NumberUtil;
import java.util.List;
import java.util.Random;
import static forex.genetic.util.Constants.AverageType;

/**
 *
 * @author ricardorq85
 */
public class AverageIndicatorManager extends IndicatorManager<Average> {

    private double min = Double.POSITIVE_INFINITY;
    private double max = Double.NEGATIVE_INFINITY;
    private Random random = new Random();
    private static EspecificMutationManager especificMutationManager = EspecificMutationManager.getInstance();
    private AverageType type = null;

    protected AverageIndicatorManager(AverageType type) {
        this.type = type;
    }

    public Indicator generate(Average indicator, Point point) {
        Average average = new Average();
        double interval1 = 0.0;
        if (indicator != null) {
            average.setAverage(indicator.getAverage());
            interval1 = indicator.getAverage() - ((type.equals(AverageType.COMPARE_CLOSE)) ? point.getCloseCompare() : point.getClose());
        } else {
            if (Double.isInfinite(min) || Double.isInfinite(max)) {
                interval1 = (random.nextBoolean()) ? random.nextDouble() : -random.nextDouble();
            } else {
                interval1 = min + ((random.nextBoolean()) ? random.nextDouble() * max : -random.nextDouble() * max);
            }
        }
        double interval2 = (interval1 + random.nextDouble()) + interval1 * ((random.nextBoolean()) ? random.nextDouble() : -random.nextDouble());
        average.getInterval().setLowerInterval(NumberUtil.round(Math.min(interval1, interval2)));
        average.getInterval().setHigherInterval(NumberUtil.round(Math.max(interval1, interval2)));

        min = Math.min(min, average.getInterval().getLowerInterval());
        max = Math.max(max, average.getInterval().getHigherInterval());

        return average;
    }

    public boolean operate(Average averageIndividuo, Average iAverage, Point point) {
        boolean operate = false;
        double lowerInterval = averageIndividuo.getInterval().getLowerInterval();
        double higherInterval = averageIndividuo.getInterval().getHigherInterval();

        double close = ((type.equals(AverageType.COMPARE_CLOSE)) ? point.getCloseCompare() : point.getClose());
        double averageValue = iAverage.getAverage();

        if (averageValue != 0.0) {
            double diff = averageValue - close;
            operate = (diff >= lowerInterval) && (diff <= higherInterval);
        }
        return operate;
    }

    public boolean operate(Average averageIndividuo, Average iAverage, List<Point> points, int i) {
        return true;
    }

    public Indicator crossover(Average average1, Average average2) {
        Average avgHijo = new Average();
        if (average1.getInterval().getLowerInterval() > average2.getInterval().getHigherInterval()) {
            avgHijo.getInterval().setLowerInterval(average2.getInterval().getLowerInterval());
            avgHijo.getInterval().setHigherInterval(average1.getInterval().getHigherInterval());
        } else {
            avgHijo.getInterval().setLowerInterval(average1.getInterval().getLowerInterval());
            avgHijo.getInterval().setHigherInterval(average2.getInterval().getHigherInterval());
        }
        return avgHijo;
    }

    public Indicator mutate(Average average) {
        Average avgHijo = new Average();
        avgHijo.getInterval().setLowerInterval(especificMutationManager.mutate(average.getInterval().getLowerInterval(), min, average.getInterval().getHigherInterval()));
        avgHijo.getInterval().setHigherInterval(especificMutationManager.mutate(average.getInterval().getHigherInterval(), avgHijo.getInterval().getLowerInterval(), max));
        return avgHijo;
    }
}

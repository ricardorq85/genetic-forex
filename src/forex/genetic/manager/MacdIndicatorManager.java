/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.Indicator;
import forex.genetic.entities.Macd;
import forex.genetic.entities.Point;
import forex.genetic.util.NumberUtil;
import java.util.List;
import java.util.Random;

/**
 *
 * @author ricardorq85
 */
public class MacdIndicatorManager extends IndicatorManager<Macd> {

    private double min = Double.POSITIVE_INFINITY;
    private double max = Double.NEGATIVE_INFINITY;
    private Random random = new Random();
    private static EspecificMutationManager especificMutationManager = EspecificMutationManager.getInstance();

    public Indicator generate(Macd indicator, Point point) {
        Macd macd = new Macd();
        double interval1 = 0.0;
        if (indicator != null) {
            macd.setMacdSignal(indicator.getMacdSignal());
            macd.setMacdValue(indicator.getMacdValue());
            interval1 = indicator.getMacdValue() - indicator.getMacdSignal();
        } else {
            if (Double.isInfinite(min) || Double.isInfinite(max)) {
                interval1 = (random.nextBoolean()) ? random.nextDouble() : -random.nextDouble();
            } else {
                interval1 = min + ((random.nextBoolean()) ? random.nextDouble() * max : -random.nextDouble() * max);
            }
        }
        double interval2 = (interval1 + random.nextDouble()) + interval1 * ((random.nextBoolean()) ? random.nextDouble() : -random.nextDouble());
        macd.getInterval().setLowerInterval(NumberUtil.round(Math.min(interval1, interval2)));
        macd.getInterval().setHigherInterval(NumberUtil.round(Math.max(interval1, interval2)));

        min = Math.min(min, macd.getInterval().getLowerInterval());
        max = Math.max(max, macd.getInterval().getHigherInterval());

        return macd;
    }

    public boolean operate(Macd macdIndividuo, Macd iMacd, Point point) {
        boolean operate = false;
        double lowerInterval = macdIndividuo.getInterval().getLowerInterval();
        double higherInterval = macdIndividuo.getInterval().getHigherInterval();

        double macdValue = iMacd.getMacdValue();
        double macdSignal = iMacd.getMacdSignal();

        if (macdValue != 0.0) {
            double diff = macdValue - macdSignal;
            operate = (diff >= lowerInterval) && (diff <= higherInterval);
        }
        return operate;
    }

    public boolean operate(Macd macdIndividuo, Macd iMacd, List<Point> points, int i) {
        return true;
    }

    public Indicator crossover(Macd macd1, Macd macd2) {
        Macd macdHijo = new Macd();
        if (macd1.getInterval().getLowerInterval() > macd2.getInterval().getHigherInterval()) {
            macdHijo.getInterval().setLowerInterval(macd2.getInterval().getLowerInterval());
            macdHijo.getInterval().setHigherInterval(macd1.getInterval().getHigherInterval());
        } else {
            macdHijo.getInterval().setLowerInterval(macd1.getInterval().getLowerInterval());
            macdHijo.getInterval().setHigherInterval(macd2.getInterval().getHigherInterval());
        }
        return macdHijo;
    }

    public Indicator mutate(Macd macd) {
        Macd macdHijo = new Macd();
        macdHijo.getInterval().setLowerInterval(especificMutationManager.mutate(macd.getInterval().getLowerInterval(), min, macd.getInterval().getHigherInterval()));
        macdHijo.getInterval().setHigherInterval(especificMutationManager.mutate(macd.getInterval().getHigherInterval(), macdHijo.getInterval().getLowerInterval(), max));
        return macdHijo;
    }
}

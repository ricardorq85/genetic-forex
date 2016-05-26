/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.entities.Indicator;
import forex.genetic.entities.Point;
import java.util.List;
import java.util.Vector;
import static forex.genetic.util.Constants.AverageType;

/**
 *
 * @author ricardorq85
 */
public abstract class IndicatorManager<E> {

    private static final AverageIndicatorManager averageIndicatorManager = new AverageIndicatorManager(AverageType.CLOSE);
    private static final MacdIndicatorManager macdIndicatorManager = new MacdIndicatorManager();
    private static final AverageIndicatorManager compareIndicatorManager = new AverageIndicatorManager(AverageType.COMPARE_CLOSE);
    private static List<IndicatorManager> list = null;

    public static IndicatorManager getInstance(int i) {
        if (list == null) {
            load();
        }
        return list.get(i);
    }

    private static void load() {
        list = new Vector<IndicatorManager>();
        list.add(averageIndicatorManager);
        list.add(macdIndicatorManager);
        list.add(compareIndicatorManager);
    }

    public abstract Indicator generate(E indicator, Point point);

    public abstract boolean operate(E individuo, E indicator, List<Point> points, int i);

    public abstract boolean operate(E individuo, E indicator, Point point);

    public abstract Indicator crossover(E indicator1, E indicator2);

    public abstract Indicator mutate(E indicator);
}


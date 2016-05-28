/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import java.util.List;
import java.util.Vector;
import static forex.genetic.util.Constants.*;

/**
 *
 * @author ricardorq85
 */
public abstract class IndicatorManager<E> {

    private static final IndicatorManager averageIndicatorManager = new AverageIndicatorManager(PriceType.AVERAGE, true);
    private static final IndicatorManager macdIndicatorManager = new MacdIndicatorManager();
    private static final IndicatorManager compareIndicatorManager = new AverageIndicatorManager(PriceType.COMPARE_CLOSE, false);
    private static final IndicatorManager sarIndicatorManager = new SarIndicatorManager(true);
    private static final IndicatorManager adxIndicatorManager = new AdxIndicatorManager();
    private static final IndicatorManager rsiIndicatorManager = new RsiIndicatorManager();
    private static final IndicatorManager bollingerBandIndicatorManager = new BollingerIndicatorManager();
    private static final IndicatorManager momentumIndicatorManager = new MomentumIndicatorManager();
    private static List<IndicatorManager> list = null;
    private boolean priceDependence = false;

    protected IndicatorManager(boolean priceDependence) {
        this.setPriceDependence(priceDependence);
    }

    public static int getIndicatorNumber() {
        if (list == null) {
            load();
        }
        return list.size();
    }

    public static IndicatorManager getInstance(int i) {
        if (list == null) {
            load();
        }
        return list.get(i);
    }

    private synchronized static void load() {
        if (list == null) {
            list = new Vector<IndicatorManager>();
            list.add(averageIndicatorManager);
            list.add(macdIndicatorManager);
            list.add(compareIndicatorManager);
            list.add(sarIndicatorManager);
            list.add(adxIndicatorManager);
            list.add(rsiIndicatorManager);
            list.add(bollingerBandIndicatorManager);
            list.add(momentumIndicatorManager);
        }
    }

    public boolean operate(E individuo, E indicator, List<Point> points, int i) {
        return true;
    }

    public boolean isPriceDependence() {
        return priceDependence;
    }

    public void setPriceDependence(boolean priceDependence) {
        this.priceDependence = priceDependence;
    }

    public abstract Indicator generate(E indicator, Point point);

    public boolean operate(E individuo, E indicator, Point point) {
        return true;
    }

    public boolean operate(E individuo, E indicator, Point currentPoint, Point previousPoint) {
        return true;
    }

    public abstract Interval calculateInterval(E individuo, E indicator, Point point);

    public abstract Indicator crossover(E indicator1, E indicator2);

    public abstract Indicator mutate(E indicator);
}

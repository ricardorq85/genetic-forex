/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Ichimoku;
import forex.genetic.entities.indicator.Indicator;

/**
 *
 * @author ricardorq85
 */
public class IchimokuTrendIndicatorManager extends IchimokuIndicatorManager {

    public IchimokuTrendIndicatorManager() {
        super(true, true, "IchiTrend");
    }

    public Ichimoku getIndicatorInstance() {
        return new Ichimoku("IchiTrend");
    }

    public Indicator generate(Ichimoku indicator, Point point) {
        Interval interval = null;
        Ichimoku ichimoku = getIndicatorInstance();
        if (indicator != null) {
            ichimoku.setChinkouSpan(indicator.getChinkouSpan());
            ichimoku.setKijunSen(indicator.getKijunSen());
            ichimoku.setSenkouSpanA(indicator.getSenkouSpanA());
            ichimoku.setSenkouSpanB(indicator.getSenkouSpanB());
            ichimoku.setTenkanSen(indicator.getTenkanSen());
            interval = intervalManager.generate(getValue(indicator, point, point), point.getLow(), point.getHigh());
        } else {
            interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
        }
        ichimoku.setInterval(interval);

        return ichimoku;
    }

    @Override
    public boolean operate(Ichimoku ichimokuIndividuo, Ichimoku iIchimoku, Point currentPoint, Point previousPoint) {
        return intervalManager.operate(ichimokuIndividuo.getInterval(),
                getValue(iIchimoku, previousPoint, previousPoint), currentPoint);
    }

    @Override
    public Interval calculateInterval(Ichimoku ichimokuIndividuo, Ichimoku iIchimoku, Point point) {
        return intervalManager.calculateInterval(ichimokuIndividuo.getInterval(),
                getValue(iIchimoku, point, point), point);
    }

    @Override
    public double getValue(Ichimoku indicator, Point prevPoint, Point point) {
        double value = (indicator.getSenkouSpanA() - indicator.getSenkouSpanB());
        return value;
    }
}

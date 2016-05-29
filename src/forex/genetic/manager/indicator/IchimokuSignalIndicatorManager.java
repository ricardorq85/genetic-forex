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
public class IchimokuSignalIndicatorManager extends IchimokuIndicatorManager {

    public IchimokuSignalIndicatorManager() {
        super(false, false, "IchiSignal");
    }

    public Ichimoku getIndicatorInstance() {
        return new Ichimoku("IchiSignal");
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
            double value = getValue(indicator, point, point);
            interval = intervalManager.generate(value, -value * 0.1, value * 0.1);
        } else {
            interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
        }
        ichimoku.setInterval(interval);
        return ichimoku;
    }

    @Override
    public boolean operate(Ichimoku ichiIndividuo, Ichimoku iIchimoku, Point point) {
        boolean operate = intervalManager.operate(ichiIndividuo.getInterval(), getValue(iIchimoku, point, point), 0.0);
        return operate;
    }

    @Override
    public double getValue(Ichimoku indicator, Point prevPoint, Point point) {
        double value = indicator.getChinkouSpan() * (indicator.getTenkanSen() - indicator.getKijunSen());
        return value;
    }
}

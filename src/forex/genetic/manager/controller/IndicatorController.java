/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.controller;

import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.manager.IntervalManager;
import forex.genetic.manager.indicator.IndicatorManager;
import forex.genetic.util.Constants;
import forex.genetic.util.NumberUtil;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class IndicatorController {

    private double pairFactor = Constants.getPairFactor(Constants.PAIR);

    public IndicatorController() {
    }

    public boolean operateOpen(IndividuoEstrategia individuoEstrategia, List<Point> points, int index) {
        boolean operate = true;
        Point currentPoint = points.get(index);
        Point previousPoint = points.get(index - 1);
        for (int j = 0; j < IndicatorManager.getIndicatorNumber() && operate; j++) {
            IndicatorManager indicatorManager = IndicatorManager.getInstance(j);
            if (individuoEstrategia.getOpenIndicators().size() > j) {
                Indicator openIndicatorIndividuo = individuoEstrategia.getOpenIndicators().get(j);
                Indicator openIndicator = previousPoint.getIndicators().get(j);
                if ((openIndicatorIndividuo != null) && (openIndicator != null)) {
                    operate = indicatorManager.operate(openIndicatorIndividuo, openIndicator, currentPoint)
                            && indicatorManager.operate(openIndicatorIndividuo, openIndicator, currentPoint, previousPoint)
                            && indicatorManager.operate(openIndicatorIndividuo, openIndicator, points, index);
                }
            }
        }
        return operate;
    }

    public boolean operateClose(IndividuoEstrategia individuoEstrategia, List<Point> points, int index) {
        boolean operate = false;
        boolean operateIndicator = true;
        Point currentPoint = points.get(index);
        Point previousPoint = points.get(index - 1);
        for (int j = 0; j < IndicatorManager.getIndicatorNumber() && operateIndicator; j++) {
            IndicatorManager indicatorManager = IndicatorManager.getInstance(j);
            if (individuoEstrategia.getCloseIndicators().size() > j) {
                Indicator closeIndicatorIndividuo = individuoEstrategia.getCloseIndicators().get(j);
                Indicator closeIndicator = previousPoint.getIndicators().get(j);
                if ((closeIndicatorIndividuo != null) && (closeIndicator != null)) {
                    operateIndicator = indicatorManager.operate(closeIndicatorIndividuo, closeIndicator, currentPoint)
                            && indicatorManager.operate(closeIndicatorIndividuo, closeIndicator, currentPoint, previousPoint)
                            && indicatorManager.operate(closeIndicatorIndividuo, closeIndicator, points, index);
                    operate = operateIndicator;
                }
            }
        }
        return operate;
    }

    public double calculateOpenPrice(IndividuoEstrategia individuoEstrategia, List<Point> points, int index) {
        double price = Double.NaN;
        Point currentPoint = points.get(index);
        Point previousPoint = points.get(index - 1);
        Interval currentInterval = new DoubleInterval(currentPoint.getLow(), currentPoint.getHigh());
        for (int j = 0; j < IndicatorManager.getIndicatorNumber() && ((currentInterval != null) || (j == 0)); j++) {
            IndicatorManager indicatorManager = IndicatorManager.getInstance(j);
            if (individuoEstrategia.getOpenIndicators().size() > j) {
                Indicator indicatorIndividuo = individuoEstrategia.getOpenIndicators().get(j);
                Indicator indicator = previousPoint.getIndicators().get(j);
                if ((indicatorIndividuo != null) && (indicator != null)) {
                    if (indicatorManager.isPriceDependence()) {
                        currentInterval = IntervalManager.intersect(currentInterval,
                                indicatorManager.calculateInterval(indicatorIndividuo, indicator, currentPoint));
                    }
                }
            }
        }
        if (currentInterval != null) {
            Interval pointInterval = new DoubleInterval(currentPoint.getLow(), currentPoint.getHigh());
            DoubleInterval resultInterval = (DoubleInterval) IntervalManager.intersect(currentInterval, pointInterval);
            if (resultInterval != null) {
                price = (currentPoint.getOpen() <= resultInterval.getLowInterval())
                        ? resultInterval.getLowInterval() : (currentPoint.getOpen() >= resultInterval.getHighInterval())
                        ? resultInterval.getHighInterval() : (currentPoint.getClose() <= resultInterval.getLowInterval())
                        ? resultInterval.getHighInterval() : (currentPoint.getClose() >= resultInterval.getHighInterval())
                        ? resultInterval.getLowInterval() : (resultInterval.getLowInterval() + resultInterval.getHighInterval()) / 2;
                if (Constants.OPERATION_TYPE.equals(Constants.OperationType.Buy)) {
                    price += Constants.PIPS_FIXER / pairFactor;
                } else {
                    price -= Constants.PIPS_FIXER / pairFactor;
                }
                price = NumberUtil.round(price);
            }
        }
        return price;
    }

    public double calculateClosePrice(IndividuoEstrategia individuoEstrategia, List<Point> points, int index) {
        double price = Double.NaN;
        Point currentPoint = points.get(index);
        Point previousPoint = points.get(index - 1);
        Interval currentInterval = new DoubleInterval(currentPoint.getLow(), currentPoint.getHigh());
        for (int j = 0; j < IndicatorManager.getIndicatorNumber() && ((currentInterval != null) || (j == 0)); j++) {
            IndicatorManager indicatorManager = IndicatorManager.getInstance(j);
            if (individuoEstrategia.getOpenIndicators().size() > j) {
                Indicator indicatorIndividuo = individuoEstrategia.getCloseIndicators().get(j);
                Indicator indicator = previousPoint.getIndicators().get(j);
                if ((indicatorIndividuo != null) && (indicator != null)) {
                    if (indicatorManager.isPriceDependence()) {
                        if (j == 0) {
                            currentInterval = indicatorManager.calculateInterval(indicatorIndividuo, indicator, currentPoint);
                        } else {
                            currentInterval = IntervalManager.intersect(currentInterval,
                                    indicatorManager.calculateInterval(indicatorIndividuo, indicator, currentPoint));
                        }
                    }
                }
            }
        }

        if (currentInterval != null) {
            Interval pointInterval = new DoubleInterval(currentPoint.getLow(), currentPoint.getHigh());
            Interval<Double> resultInterval = IntervalManager.intersect(currentInterval, pointInterval);
            if (resultInterval != null) {
                price = (currentPoint.getOpen() <= resultInterval.getLowInterval())
                        ? resultInterval.getLowInterval() : (currentPoint.getOpen() >= resultInterval.getHighInterval())
                        ? resultInterval.getHighInterval() : (currentPoint.getClose() <= resultInterval.getLowInterval())
                        ? resultInterval.getHighInterval() : (currentPoint.getClose() >= resultInterval.getHighInterval())
                        ? resultInterval.getLowInterval() : (resultInterval.getLowInterval() + resultInterval.getHighInterval()) / 2;
                if (Constants.OPERATION_TYPE.equals(Constants.OperationType.Buy)) {
                    price -= Constants.PIPS_FIXER / pairFactor;
                } else {
                    price += Constants.PIPS_FIXER / pairFactor;
                }
                price = NumberUtil.round(price);
            }
        }
        return price;
    }

    public double calculatePrice(List<Point> points, int index) {
        Point currentPoint = points.get(index);
        return currentPoint.getWeihgted();
    }
}


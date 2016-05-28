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
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.manager.IntervalManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.indicator.IndicatorManager;
import forex.genetic.util.Constants;
import forex.genetic.util.NumberUtil;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author ricardorq85
 */
public class IndicatorController {

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
                if (PropertiesManager.isBuy()) {
                    price += PropertiesManager.getPipsFixer() / PropertiesManager.getPairFactor();
                } else {
                    price -= PropertiesManager.getPipsFixer() / PropertiesManager.getPairFactor();
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
                if (PropertiesManager.isBuy()) {
                    price -= PropertiesManager.getPipsFixer() / PropertiesManager.getPairFactor();
                } else {
                    price += PropertiesManager.getPipsFixer() / PropertiesManager.getPairFactor();
                }
                price = NumberUtil.round(price);
            }
        }
        return price;
    }

    public double calculateStopLossPrice(List<Point> points, int index, Constants.OperationType operationType) {
        Point currentPoint = points.get(index);
        double value = 0.0D;
        if (operationType.equals(Constants.OperationType.Buy)) {
            value = currentPoint.getLow();
        } else {
            value = currentPoint.getHigh();
        }
        return value;
    }

    public double calculateTakePrice(List<Point> points, int index, Constants.OperationType operationType) {
        Point currentPoint = points.get(index);
        double value = 0.0D;
        if (operationType.equals(Constants.OperationType.Buy)) {
            value = currentPoint.getHigh();
        } else {
            value = currentPoint.getLow();
        }
        return value;
    }

    public void optimize(IndividuoEstrategia individuoEstrategia, Point prevOpenPoint, Point openPoint, Point closePoint, double pips) {
        List<Indicator> openOptimized = new Vector<Indicator>(IndicatorManager.getIndicatorNumber());
        List<Indicator> closeOptimized = new Vector<Indicator>(IndicatorManager.getIndicatorNumber());
        for (int j = 0; j < IndicatorManager.getIndicatorNumber(); j++) {
            IndicatorManager indicatorManager = IndicatorManager.getInstance(j);
            if (individuoEstrategia.getOpenIndicators().size() > j) {
                Indicator openIndicatorIndividuo = individuoEstrategia.getOpenIndicators().get(j);
                Indicator closeIndicatorIndividuo = individuoEstrategia.getCloseIndicators().get(j);
                Indicator openOptimizedIndividuo = null;
                Indicator closeOptimizedIndividuo = null;
                if ((individuoEstrategia.getOptimizedOpenIndicators() != null) && (j < individuoEstrategia.getOptimizedOpenIndicators().size())) {
                    openOptimizedIndividuo = individuoEstrategia.getOptimizedOpenIndicators().get(j);
                }
                if ((individuoEstrategia.getOptimizedCloseIndicators() != null) && (j < individuoEstrategia.getOptimizedCloseIndicators().size())) {
                    closeOptimizedIndividuo = individuoEstrategia.getOptimizedCloseIndicators().get(j);
                }
                Indicator openIndicator = prevOpenPoint.getIndicators().get(j);
                Indicator closeIndicator = closePoint.getIndicators().get(j);
                if ((openIndicatorIndividuo != null) && (openIndicator != null)) {
                    Indicator optimizedIndicator = indicatorManager.optimize(openIndicatorIndividuo, openOptimizedIndividuo, openIndicator, prevOpenPoint, openPoint, pips);
                    openOptimized.add(optimizedIndicator);
                } else {
                    openOptimized.add(null);
                }

                if ((closeIndicatorIndividuo != null) && (closeIndicator != null)) {
                    //Indicator optimizedIndicator = indicatorManager.optimize(closeIndicatorIndividuo, closeOptimizedIndividuo, closeIndicator, closePoint);
                    Indicator optimizedIndicator = closeIndicatorIndividuo;
                    closeOptimized.add(optimizedIndicator);
                } else {
                    closeOptimized.add(null);
                }
            } else {
                openOptimized.add(null);
                closeOptimized.add(null);
            }
        }
        individuoEstrategia.setOptimizedOpenIndicators(openOptimized);
        individuoEstrategia.setOptimizedCloseIndicators(closeOptimized);
    }
}

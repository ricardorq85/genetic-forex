/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.controller;

import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.IntervalManager;
import forex.genetic.util.io.PropertiesManager;
import forex.genetic.manager.indicator.IndicadorManager;
import forex.genetic.util.Constants;
import forex.genetic.util.NumberUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class OperationController {

    private final IndicadorController indicadorController = ControllerFactory.createIndicadorController(ControllerFactory.ControllerType.Individuo);
    
    /**
     *
     */
    public OperationController() {
    }

    /**
     *
     * @param individuoEstrategia
     * @param points
     * @param index
     * @return
     */
    public boolean operateOpen(IndividuoEstrategia individuoEstrategia, List<Point> points, int index) {
        boolean operate = true;
        Point currentPoint = points.get(index);
        Point previousPoint = points.get(index - 1);
        for (int j = 0; j < indicadorController.getIndicatorNumber() && operate; j++) {
            IndicadorManager indicatorManager = indicadorController.getManagerInstance(j);
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

    /**
     *
     * @param individuoEstrategia
     * @param points
     * @param index
     * @return
     */
    public boolean operateClose(IndividuoEstrategia individuoEstrategia, List<Point> points, int index) {
        boolean operate = false;
        boolean operateIndicator = true;
        Point currentPoint = points.get(index);
        Point previousPoint = points.get(index - 1);
        for (int j = 0; j < indicadorController.getIndicatorNumber() && operateIndicator; j++) {
            IndicadorManager indicatorManager = indicadorController.getManagerInstance(j);
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

    /**
     *
     * @param individuoEstrategia
     * @param points
     * @param index
     * @return
     */
    public double calculateOpenPrice(IndividuoEstrategia individuoEstrategia, List<Point> points, int index) {
        double price = Double.NaN;
        Point currentPoint = points.get(index);
        Point previousPoint = points.get(index - 1);
        Interval currentInterval = new DoubleInterval(currentPoint.getLow(), currentPoint.getHigh());
        for (int j = 0; j < indicadorController.getIndicatorNumber() && ((currentInterval != null) || (j == 0)); j++) {
            IndicadorManager indicatorManager = indicadorController.getManagerInstance(j);
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

    /**
     *
     * @param individuoEstrategia
     * @param points
     * @param index
     * @return
     */
    public double calculateClosePrice(IndividuoEstrategia individuoEstrategia, List<Point> points, int index) {
        double price = Double.NaN;
        Point currentPoint = points.get(index);
        Point previousPoint = points.get(index - 1);
        Interval currentInterval = new DoubleInterval(currentPoint.getLow(), currentPoint.getHigh());
        for (int j = 0; j < indicadorController.getIndicatorNumber() && ((currentInterval != null) || (j == 0)); j++) {
            IndicadorManager indicatorManager = indicadorController.getManagerInstance(j);
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

    /**
     *
     * @param points
     * @param index
     * @param operationType
     * @return
     */
    public double calculateStopLossPrice(List<Point> points, int index, Constants.OperationType operationType) {
        Point currentPoint = points.get(index);
        double value = 0.0D;
        if (operationType.equals(Constants.OperationType.BUY)) {
            value = currentPoint.getLow();
        } else {
            value = currentPoint.getHigh();
        }
        return value;
    }

    /**
     *
     * @param points
     * @param index
     * @param operationType
     * @return
     */
    public double calculateTakePrice(List<Point> points, int index, Constants.OperationType operationType) {
        Point currentPoint = points.get(index);
        double value = 0.0D;
        if (operationType.equals(Constants.OperationType.BUY)) {
            value = currentPoint.getHigh();
        } else {
            value = currentPoint.getLow();
        }
        return value;
    }

    /**
     *
     * @param individuoEstrategia
     * @param prevOpenPoint
     * @param openPoint
     * @param closePoint
     * @param pips
     */
    public void optimize(IndividuoEstrategia individuoEstrategia, Point prevOpenPoint, Point openPoint, Point closePoint, double pips) {
        List<Indicator> openOptimized = Collections.synchronizedList(new ArrayList<>(indicadorController.getIndicatorNumber()));
        List<Indicator> closeOptimized = Collections.synchronizedList(new ArrayList<>(indicadorController.getIndicatorNumber()));
        for (int j = 0; j < indicadorController.getIndicatorNumber(); j++) {
            IndicadorManager indicatorManager = indicadorController.getManagerInstance(j);
            Indicator openIndicatorIndividuo = null;
            if (individuoEstrategia.getOpenIndicators().size() > j) {
                openIndicatorIndividuo = individuoEstrategia.getOpenIndicators().get(j);
            }
            Indicator closeIndicatorIndividuo = null;
            if (individuoEstrategia.getCloseIndicators().size() > j) {
                closeIndicatorIndividuo = individuoEstrategia.getCloseIndicators().get(j);
            }
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
        }
        individuoEstrategia.setOptimizedOpenIndicators(openOptimized);
        individuoEstrategia.setOptimizedCloseIndicators(closeOptimized);
    }
}

package forex.genetic.manager;

import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.manager.controller.IndicatorController;
import forex.genetic.util.Constants;
import forex.genetic.util.NumberUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class OperacionesManager {

    private IndicatorController indicatorController = new IndicatorController();

    public boolean hasMinimumCriterion(Individuo individuo) {
        boolean has = true;
        return has;
    }

    public double calcularPips(List<Point> points, int index, Order operacion) {
        double pips = 0.0D;
        pips = (PropertiesManager.isBuy())
                ? (indicatorController.calculateStopLossPrice(points, index, Constants.OperationType.Buy)
                - operacion.getOpenOperationValue()) * PropertiesManager.getPairFactor()
                : (-indicatorController.calculateStopLossPrice(points, index, Constants.OperationType.Sell)
                + operacion.getOpenOperationValue()) * PropertiesManager.getPairFactor();
        //pips = pips - operacion.getOpenSpread();
        return pips;
    }

    public List<Order> calcularOperaciones(List<Point> points, Individuo individuo) {
        List<Order> ordenes = new ArrayList<Order>();
        double takeProfit = individuo.getTakeProfit();
        double stopLoss = individuo.getStopLoss();
        double lot = individuo.getLot();

        boolean hasMinimumCriterion = true;
        boolean activeOperation = (individuo.getFechaApertura() != null);
        Point openPoint = null;
        Order currentOrder = individuo.getCurrentOrder();
        double openOperationValue = (activeOperation) ? currentOrder.getOpenOperationValue() : 0.0D;
        for (int i = 1; (i < points.size() && hasMinimumCriterion); i++) {
            if (!activeOperation) {
                boolean operate = indicatorController.operateOpen(individuo, points, i);
                if (operate) {
                    //boolean operate2 = indicatorController.operateOpen(individuo, points, i);
                    openOperationValue = indicatorController.calculateOpenPrice(individuo, points, i);
                    individuo.setOpenOperationValue(openOperationValue);
                    operate = !Double.isNaN(openOperationValue);
                    if (operate) {
                        activeOperation = true;
                        openPoint = points.get(i);
                        currentOrder = new Order();
                        currentOrder.setOpenDate(openPoint.getDate());
                        currentOrder.setOpenOperationValue(openOperationValue);
                        currentOrder.setOpenSpread(openPoint.getSpread());
                        currentOrder.setLot(lot);
                        currentOrder.setTakeProfit(takeProfit);
                        currentOrder.setStopLoss(stopLoss);
                        individuo.setCurrentOrder(currentOrder);
                    }
                }
            } else {
                Point closePoint = points.get(i);
                double pips = 0.0D;
                double stopLossPips = (PropertiesManager.isBuy())
                        ? (indicatorController.calculateStopLossPrice(points, i, Constants.OperationType.Buy) - openOperationValue) * PropertiesManager.getPairFactor()
                        : (-indicatorController.calculateStopLossPrice(points, i, Constants.OperationType.Sell) + openOperationValue) * PropertiesManager.getPairFactor();
                double takeProfitPips = (PropertiesManager.isBuy())
                        ? (indicatorController.calculateTakePrice(points, i, Constants.OperationType.Buy) - openOperationValue) * PropertiesManager.getPairFactor()
                        : (-indicatorController.calculateTakePrice(points, i, Constants.OperationType.Sell) + openOperationValue) * PropertiesManager.getPairFactor();
                stopLossPips = stopLossPips - (currentOrder.getOpenSpread());
                takeProfitPips = takeProfitPips - (currentOrder.getOpenSpread());
                boolean operate = (((takeProfitPips >= (takeProfit)) || (stopLossPips <= -(stopLoss))));
                if (!operate) {
                    operate = indicatorController.operateClose(individuo, points, i);
                    if (operate) {
                        pips = (PropertiesManager.isBuy())
                                ? (indicatorController.calculateClosePrice(individuo, points, i) - openOperationValue) * PropertiesManager.getPairFactor()
                                : (-indicatorController.calculateClosePrice(individuo, points, i) + openOperationValue) * PropertiesManager.getPairFactor();
                        operate = !Double.isNaN(pips);
                        if (operate) {
                            //boolean operate2 = indicatorController.operateClose(individuo, points, i);
                            currentOrder.setCloseByTakeStop(false);
                            pips = (pips - currentOrder.getOpenSpread());
                        }
                    }
                } else {
                    currentOrder.setCloseByTakeStop(true);
                    if (takeProfitPips >= (takeProfit)) {
                        pips = (takeProfit);
                    } else if (stopLossPips <= -(stopLoss)) {
                        pips = -(stopLoss);
                    }
                }
                if (operate) {
                    activeOperation = false;
                    currentOrder.setCloseDate(closePoint.getDate());
                    currentOrder.setPips(pips);
                    ordenes.add(currentOrder);
                    currentOrder = null;
                    individuo.setCurrentOrder(null);
                }
            }
        }
        if (currentOrder != null) {
            ordenes.add(currentOrder);
        }
        return ordenes;
    }

    public double calculateOpenPrice(Point currentPoint) {
        double price = Double.NaN;
        Interval currentInterval = new DoubleInterval(currentPoint.getLow(), currentPoint.getHigh());
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
}
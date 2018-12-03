package forex.genetic.manager.mongodb;

import java.util.ArrayList;
import java.util.List;

import forex.genetic.entities.Individuo;
import forex.genetic.entities.Point;
import forex.genetic.entities.mongo.MongoOrder;
import forex.genetic.manager.OperacionesManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants;
import forex.genetic.util.Constants.CloseType;
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public class MongoOperacionesManager extends OperacionesManager {

	public MongoOperacionesManager(DataClient dc) {
		super(dc);
	}

	public List<MongoOrder> calcularOperaciones(List<Point> points, Individuo individuo) {
		List<MongoOrder> ordenes = new ArrayList<MongoOrder>();
		double takeProfit = individuo.getTakeProfit();
		double stopLoss = individuo.getStopLoss();
		double lot = individuo.getLot();

		boolean hasMinimumCriterion = true;
		boolean activeOperation = (individuo.getCurrentOrder() != null);
		Point openPoint;
		MongoOrder currentOrder = (MongoOrder)individuo.getCurrentOrder();
		double openOperationValue = (activeOperation) ? currentOrder.getOpenOperationValue() : 0.0D;
		for (int i = 1; (i < points.size() && hasMinimumCriterion); i++) {
			if (!activeOperation) {
				boolean operate = operationController.operateOpen(individuo, points, i);
				if (operate) {
					openOperationValue = operationController.calculateOpenPrice(individuo, points, i);
					individuo.setOpenOperationValue(openOperationValue);
					operate = !Double.isNaN(openOperationValue);
					if (operate) {
						activeOperation = true;
						openPoint = points.get(i);
						currentOrder = new MongoOrder();
						currentOrder.setIdIndividuo(individuo.getId());
						currentOrder.setOpenDate(openPoint.getDate());
						currentOrder.setOpenOperationValue(openOperationValue);
						currentOrder.setOpenSpread(openPoint.getSpread());
						currentOrder.setLot(lot);
						currentOrder.setTakeProfit(takeProfit);
						currentOrder.setStopLoss(stopLoss);
						currentOrder.setTipo(individuo.getTipoOperacion());
						currentOrder.setClosePriceByTakeProfit(operationController.calculateClosePriceByTakeProfit(currentOrder));
						currentOrder.setClosePriceByStopLoss(operationController.calculateClosePriceByStopLoss(currentOrder));
						individuo.setCurrentOrder(currentOrder);
					}
				}
			} else {
				Point closePoint = points.get(i);
				double pips = 0.0D;
				double stopLossPips = (individuo.isTipoBuy())
						? (operationController.calculateStopLossPrice(points, i, Constants.OperationType.BUY)
								- openOperationValue) * PropertiesManager.getPairFactor()
						: (-operationController.calculateStopLossPrice(points, i, Constants.OperationType.SELL)
								+ openOperationValue) * PropertiesManager.getPairFactor();
				double takeProfitPips = (individuo.isTipoBuy())
						? (operationController.calculateTakePrice(points, i, Constants.OperationType.BUY)
								- openOperationValue) * PropertiesManager.getPairFactor()
						: (-operationController.calculateTakePrice(points, i, Constants.OperationType.SELL)
								+ openOperationValue) * PropertiesManager.getPairFactor();
				stopLossPips = stopLossPips - (currentOrder.getOpenSpread());
				takeProfitPips = takeProfitPips - (currentOrder.getOpenSpread());
				boolean operate = (((takeProfitPips >= (takeProfit)) || (stopLossPips <= -(stopLoss))));
				if (!operate) {
					operate = operationController.operateClose(individuo, points, i);
					if (operate) {
						pips = (individuo.isTipoBuy())
								? (operationController.calculateClosePrice(individuo, points, i) - openOperationValue)
										* PropertiesManager.getPairFactor()
								: (-operationController.calculateClosePrice(individuo, points, i) + openOperationValue)
										* PropertiesManager.getPairFactor();
						operate = !Double.isNaN(pips);
						if (operate) {
							currentOrder.setCloseByTakeStop(false);
							currentOrder.setTipoCierre(CloseType.INDICADORES);
							pips = (pips - currentOrder.getOpenSpread());
						}
					}
				} else {
					currentOrder.setCloseByTakeStop(true);
					if (takeProfitPips >= (takeProfit)) {
						currentOrder.setTipoCierre(CloseType.TAKE_PROFIT);
						pips = (takeProfit);
					} else if (stopLossPips <= -(stopLoss)) {
						currentOrder.setTipoCierre(CloseType.STOP_LOSS);
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
	
}

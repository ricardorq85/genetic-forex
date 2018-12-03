package forex.genetic.manager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.IDatoHistoricoDAO;
import forex.genetic.dao.IOperacionesDAO;
import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.controller.OperationController;
import forex.genetic.util.Constants;
import forex.genetic.util.Constants.OperationType;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.NumberUtil;
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public abstract class OperacionesManager {

	protected final OperationController operationController = new OperationController();

	/**
	 *
	 */
	public OperacionesManager() {
	}


	protected abstract DataClient getDataClient() throws GeneticDAOException;
	
	/**
	 *
	 * @param individuo
	 * @return
	 */
	public boolean hasMinimumCriterion(Individuo individuo) {
		boolean has = true;
		return has;
	}

	/**
	 *
	 * @param points
	 * @param index
	 * @param operacion
	 * @return
	 */
	public double calcularPips(List<Point> points, int index, Order operacion) {
		return calcularPips(points.get(index), operacion);
	}

	public double calcularPips(Point currentPoint, Order operacion) {
		double pips = 0.0D;
		double stopLossPips = (operacion.getTipo().equals(Constants.OperationType.BUY))
				? (operationController.calculateStopLossPrice(currentPoint, Constants.OperationType.BUY)
						- operacion.getOpenOperationValue()) * PropertiesManager.getPairFactor()
				: (-operationController.calculateStopLossPrice(currentPoint, Constants.OperationType.SELL)
						+ operacion.getOpenOperationValue()) * PropertiesManager.getPairFactor();
		double takeProfitPips = (operacion.getTipo().equals(Constants.OperationType.BUY))
				? (operationController.calculateTakePrice(currentPoint, Constants.OperationType.BUY)
						- operacion.getOpenOperationValue()) * PropertiesManager.getPairFactor()
				: (-operationController.calculateTakePrice(currentPoint, Constants.OperationType.SELL)
						+ operacion.getOpenOperationValue()) * PropertiesManager.getPairFactor();

		if (stopLossPips < 0) {
			pips = stopLossPips;
		} else {
			pips = takeProfitPips;
		}

		return pips;
	}

	/**
	 *
	 * @param points
	 * @param individuo
	 * @return
	 */
	public List<? extends Order> calcularOperaciones(List<Point> points, Individuo individuo) {
		List<Order> ordenes = new ArrayList<>();
		double takeProfit = individuo.getTakeProfit();
		double stopLoss = individuo.getStopLoss();
		double lot = individuo.getLot();

		boolean hasMinimumCriterion = true;
		boolean activeOperation = (individuo.getFechaApertura() != null);
		Point openPoint;
		Order currentOrder = individuo.getCurrentOrder();
		double openOperationValue = (activeOperation) ? currentOrder.getOpenOperationValue() : 0.0D;
		for (int i = 1; (i < points.size() && hasMinimumCriterion); i++) {
			if (!activeOperation) {
				boolean operate = operationController.operateOpen(individuo, points, i);
				if (operate) {
					// boolean operate2 =
					// indicatorController.operateOpen(individuo, points, i);
					openOperationValue = operationController.calculateOpenPrice(individuo, points, i);
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
						currentOrder.setTipo(individuo.getTipoOperacion());
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
							// boolean operate2 =
							// indicatorController.operateClose(individuo,
							// points, i);
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

	/**
	 *
	 * @param points
	 * @param individuo
	 */
	public void calcularCierreOperacion(List<Point> points, Individuo individuo) {
		double takeProfit = individuo.getTakeProfit();
		double stopLoss = individuo.getStopLoss();

		boolean activeOperation = (individuo.getFechaApertura() != null);
		Order currentOrder = individuo.getCurrentOrder();
		double openOperationValue = (activeOperation) ? currentOrder.getOpenOperationValue() : 0.0D;
		for (int i = 1; (i < points.size() && (currentOrder != null)); i++) {
			Point closePoint = points.get(i);
			double pips = 0.0D;
			boolean operate = currentOrder.isCloseImmediate();
			if (operate) {
				pips = (currentOrder.getTipo().equals(Constants.OperationType.BUY))
						? (operationController.calculateClosePrice(individuo, points, i) - openOperationValue)
								* PropertiesManager.getPairFactor()
						: (-operationController.calculateClosePrice(individuo, points, i) + openOperationValue)
								* PropertiesManager.getPairFactor();
				currentOrder.setCloseByTakeStop(false);
				pips = (pips - currentOrder.getOpenSpread());
			} else {
				double stopLossPips = (currentOrder.getTipo().equals(Constants.OperationType.BUY))
						? (operationController.calculateStopLossPrice(points, i, Constants.OperationType.BUY)
								- openOperationValue) * PropertiesManager.getPairFactor()
						: (-operationController.calculateStopLossPrice(points, i, Constants.OperationType.SELL)
								+ openOperationValue) * PropertiesManager.getPairFactor();
				double takeProfitPips = (currentOrder.getTipo().equals(Constants.OperationType.BUY))
						? (operationController.calculateTakePrice(points, i, Constants.OperationType.BUY)
								- openOperationValue) * PropertiesManager.getPairFactor()
						: (-operationController.calculateTakePrice(points, i, Constants.OperationType.SELL)
								+ openOperationValue) * PropertiesManager.getPairFactor();
				stopLossPips = stopLossPips - (currentOrder.getOpenSpread());
				takeProfitPips = takeProfitPips - (currentOrder.getOpenSpread());
				operate = (((takeProfitPips >= (takeProfit)) || (stopLossPips <= -(stopLoss))));
				if (operate) {
					currentOrder.setCloseByTakeStop(true);
					if (takeProfitPips >= (takeProfit)) {
						pips = (takeProfit);
					} else if (stopLossPips <= -(stopLoss)) {
						pips = -(stopLoss);
					}
				}
			}
			if (operate) {
				activeOperation = false;
				currentOrder.setCloseDate(closePoint.getDate());
				currentOrder.setPips(pips);
				currentOrder = null;
				individuo.setCurrentOrder(null);
			}
		}
	}

	public double calculateOpenPrice(Point currentPoint) {
		return this.calculateOpenPrice(currentPoint, OperationType.SELL);
	}

	public double calculatePrice(OperationType tipoOperacion, double precioBase, double pips) {
		double precio = precioBase;
		if (tipoOperacion.equals(OperationType.BUY)) {
			precio += (pips / PropertiesManager.getPairFactor());
		} else {
			precio -= (pips / PropertiesManager.getPairFactor());
		}
		precio = NumberUtil.round(precio);
		return precio;
	}

	/**
	 *
	 * @param currentPoint
	 * @return
	 */
	public double calculateOpenPrice(Point currentPoint, OperationType tipoOperacion) {
		double price = Double.NaN;
		Interval currentInterval = new DoubleInterval(currentPoint.getLow(), currentPoint.getHigh());
		if (currentInterval != null) {
			Interval pointInterval = new DoubleInterval(currentPoint.getLow(), currentPoint.getHigh());
			DoubleInterval resultInterval = (DoubleInterval) IntervalManager.intersect(currentInterval, pointInterval);
			if (resultInterval != null) {
				double precioBase = (currentPoint.getOpen() <= resultInterval.getLowInterval())
						? resultInterval.getLowInterval()
						: (currentPoint.getOpen() >= resultInterval.getHighInterval())
								? resultInterval.getHighInterval()
								: (currentPoint.getClose() <= resultInterval.getLowInterval())
										? resultInterval.getHighInterval()
										: (currentPoint.getClose() >= resultInterval.getHighInterval())
												? resultInterval.getLowInterval()
												: (resultInterval.getLowInterval() + resultInterval.getHighInterval())
														/ 2;
				price = this.calculatePrice(tipoOperacion, precioBase, PropertiesManager.getPipsFixer());
			}
		}
		return price;
	}

	/**
	 *
	 * @param fechaMaximo
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws GeneticDAOException
	 */
	public void procesarMaximosRetroceso(Date fechaMaximo)
			throws ClassNotFoundException, SQLException, GeneticDAOException {
		IOperacionesDAO<? extends Order> operacionesDAO = getDataClient().getDaoOperaciones();
		List<Individuo> individuos = operacionesDAO.consultarOperacionesIndividuoRetroceso(fechaMaximo);
		while ((individuos != null) && (!individuos.isEmpty())) {
			for (Individuo individuo : individuos) {
				if (individuo.getOpenIndicators() == null) {
					individuo.setOpenIndicators(new ArrayList<>());
				}
				if (individuo.getCloseIndicators() == null) {
					individuo.setCloseIndicators(new ArrayList<>());
				}
				LogUtil.logTime("Individuo=" + individuo.getId() + ";Fecha apertura="
						+ DateUtil.getDateString(individuo.getOrdenes().get(0).getOpenDate()), 1);
				this.procesarMaximosRetroceso(individuo);
			}
			individuos = operacionesDAO.consultarOperacionesIndividuoRetroceso(fechaMaximo);
		}
	}

	/**
	 *
	 * @param individuo
	 * @param fechaMaximo
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws GeneticDAOException
	 */
	public void procesarMaximosRetroceso(Individuo individuo, Date fechaMaximo)
			throws ClassNotFoundException, SQLException, GeneticDAOException {
		IOperacionesDAO<? extends Order> operacionesDAO = getDataClient().getDaoOperaciones();
		individuo = operacionesDAO.consultarOperacionesIndividuoRetroceso(individuo, fechaMaximo);

		if (individuo.getOpenIndicators() == null) {
			individuo.setOpenIndicators(new ArrayList<>());
		}
		if (individuo.getCloseIndicators() == null) {
			individuo.setCloseIndicators(new ArrayList<>());
		}
		this.procesarMaximosRetroceso(individuo);
	}

	/**
	 *
	 * @param individuo
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws GeneticDAOException
	 */
	public void procesarMaximosRetroceso(Individuo individuo)
			throws ClassNotFoundException, SQLException, GeneticDAOException {
		@SuppressWarnings("unchecked")
		IOperacionesDAO<Order> operacionesDAO = getDataClient().getDaoOperaciones();
		List<Order> ordenes = individuo.getOrdenes();
		for (Order currentOrder : ordenes) {
			if ((currentOrder != null) && (currentOrder.getOpenDate() != null)
					&& (currentOrder.getCloseDate() != null)) {
				calcularRetrocesoOrden(currentOrder);
				operacionesDAO.updateMaximosRetrocesoOperacion(individuo, currentOrder);
			}
		}
		operacionesDAO.commit();
	}

	public void calcularRetrocesoOrden(Order currentOrder) throws GeneticDAOException {
		IDatoHistoricoDAO datoHistoricoDAO = getDataClient().getDaoDatoHistorico();
		Point pointRetroceso = datoHistoricoDAO.consultarRetroceso(currentOrder);
		if (pointRetroceso != null) {
			boolean isBuy = (currentOrder.getTipo().equals(Constants.OperationType.BUY));
			double valueRetroceso = (((((!isBuy) && (currentOrder.getPips() > 0))
					|| ((isBuy) && (currentOrder.getPips() < 0)))) ? pointRetroceso.getHigh()
							: pointRetroceso.getLow());

			double pips = (currentOrder.getTipo().equals(Constants.OperationType.BUY))
					? (valueRetroceso - currentOrder.getOpenOperationValue()) * PropertiesManager.getPairFactor()
					: (-valueRetroceso + currentOrder.getOpenOperationValue()) * PropertiesManager.getPairFactor();
			pips = (pips - currentOrder.getOpenSpread());
			currentOrder.setMaxPipsRetroceso(NumberUtil.round(pips));
			currentOrder.setMaxValueRetroceso(valueRetroceso);
			currentOrder.setMaxFechaRetroceso(pointRetroceso.getDate());
		} else {
			currentOrder.setMaxPipsRetroceso(0.0D);
			currentOrder.setMaxValueRetroceso(0.0D);
			currentOrder.setMaxFechaRetroceso(null);
		}
	}

}

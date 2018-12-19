/*
close * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.dao.mongodb.MongoIndividuoDAO;
import forex.genetic.dao.mongodb.MongoOperacionesDAO;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.entities.dto.ProcesoEjecucionDTO;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.entities.mongo.MongoOrder;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.manager.mongodb.MongoEstadisticasManager;
import forex.genetic.manager.mongodb.MongoOperacionesManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public class MongoProcesarIndividuoThread extends Thread {

	private List<MongoIndividuo> individuos = null;
	private MongoDatoHistoricoDAO daoDatoHistorico;
	private MongoOperacionesDAO daoOperaciones;
	private MongoIndividuoDAO daoIndividuo;
	private Date maxFechaHistorico = null;
	private Date minFechaHistorico = null;
	private DataClient dataClient = null;

	/**
	 *
	 * @param name
	 * @param individuos
	 */
	public MongoProcesarIndividuoThread(String name, List<MongoIndividuo> individuos) {
		super(name);
		this.individuos = individuos;
	}

	@Override
	public void run() {
		try {
			LogUtil.logTime("Inicia proceso para " + super.getName(), 1);
			dataClient = DriverDBFactory.createDataClient("mongodb");
			daoDatoHistorico = (MongoDatoHistoricoDAO) dataClient.getDaoDatoHistorico();
			daoOperaciones = (MongoOperacionesDAO) dataClient.getDaoOperaciones();
			daoIndividuo = (MongoIndividuoDAO) dataClient.getDaoIndividuo();
			for (MongoIndividuo individuo : individuos) {
				// TODO RROJASQ Quitar estas 2 lineas, solo para pruebas
				// individuo.getProcesoEjecucion().setMaxFechaHistorico(getMinFechaHistorico());
				// individuo.setCurrentOrder(null);
				runIndividuo(individuo);
			}
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (GeneticDAOException e) {
			e.printStackTrace();
		}
	}

	protected void runIndividuo(MongoIndividuo individuo) throws ClassNotFoundException, GeneticDAOException {
		runIndividuoByYear(individuo);
	}

	private DateInterval findNextInterval(Date currentDate) {
		DateInterval yearInterval;
		if (currentDate.before(maxFechaHistorico)) {
			yearInterval = DateUtil.obtenerIntervaloAnyo(currentDate);
			Date ultimaFechaDelAnyo = DateUtil.obtenerFechaMinima(maxFechaHistorico,
					DateUtil.adicionarMinutos(yearInterval.getHighInterval(), -1));
			yearInterval.setLowInterval(currentDate);
			yearInterval.setHighInterval(ultimaFechaDelAnyo);
		} else {
			yearInterval = new DateInterval(maxFechaHistorico, maxFechaHistorico);
		}
		return yearInterval;
	}

	protected void runIndividuoByYear(MongoIndividuo individuo) throws ClassNotFoundException, GeneticDAOException {
		if (!validarYBorrarIndividuoInvalido(individuo)) {
			ProcesoEjecucionDTO procesoEjecucionIndividuo = individuo.getProcesoEjecucion();
			DateInterval interval = null;
			if ((procesoEjecucionIndividuo == null) || (procesoEjecucionIndividuo.getMaxFechaHistorico() == null)) {
				interval = findNextInterval(procesoEjecucionIndividuo.getMaxFechaHistorico());
			} else {
				interval = findNextInterval(this.minFechaHistorico);
			}

			boolean processed = true;
			MongoEstadisticasManager estadisticasManager = new MongoEstadisticasManager(individuo);
			while (processed && !interval.getHighInterval().after(this.maxFechaHistorico)) {
				LogUtil.logTime(super.getName() + ":" + individuo.getId() + "," + interval.toString(), 1);

				LogUtil.logTime("Consultando puntos de apertura", 1);
				List<Point> puntosApertura = daoDatoHistorico.consultarProximosPuntosApertura(individuo, interval);

				LogUtil.logTime("Consultando puntos de cierre", 1);
				List<Point> puntosCierre = daoDatoHistorico.consultarPuntosCierre(individuo, interval);

				Date lastProcessedDate = null;
				if (puntosApertura.isEmpty()) {
					lastProcessedDate = interval.getHighInterval();
					procesarIndividuo(estadisticasManager, interval, null, puntosCierre);
				} else {
					lastProcessedDate = interval.getLowInterval();
				}
				for (Point point : puntosApertura) {
					if (point.getDate().after(lastProcessedDate)) {
						lastProcessedDate = procesarIndividuo(estadisticasManager, interval, point, puntosCierre);
					}
				}
				processed = (lastProcessedDate != null);
				if (processed) {
					interval = findNextInterval(DateUtil.adicionarMinutos(interval.getLowInterval(), 1));
				}
			}
			if (processed && !validarYBorrarIndividuoInvalido(individuo)) {
				// this.actualizarOperacionSemanal(individuo);
			}
		}
	}

	protected void runIndividuoByInterval(MongoIndividuo individuo) throws ClassNotFoundException, GeneticDAOException {
		int mesesAAdicionar = 3;
		if (!validarYBorrarIndividuoInvalido(individuo)) {
			ProcesoEjecucionDTO procesoEjecucionIndividuo = individuo.getProcesoEjecucion();
			Date fechaMayorQue = ((procesoEjecucionIndividuo == null)
					|| (procesoEjecucionIndividuo.getMaxFechaHistorico() == null)) ? this.minFechaHistorico
							: procesoEjecucionIndividuo.getMaxFechaHistorico();
			Date fechaMenorOIgualQue = DateUtil.obtenerFechaMinima(this.maxFechaHistorico,
					DateUtil.adicionarMes(fechaMayorQue, mesesAAdicionar));
			boolean processed = true;

			MongoEstadisticasManager estadisticasManager = new MongoEstadisticasManager(individuo);
			while (processed && !fechaMenorOIgualQue.after(this.maxFechaHistorico)
					&& fechaMenorOIgualQue.after(fechaMayorQue)) {
				DateInterval intervaloFechasIndividuo = new DateInterval(fechaMayorQue, fechaMenorOIgualQue);

				LogUtil.logTime(super.getName() + ":" + individuo.getId() + "," + intervaloFechasIndividuo.toString(),
						1);
				LogUtil.logTime("Consultando puntos de apertura", 3);
				List<Point> puntosApertura = daoDatoHistorico.consultarProximosPuntosApertura(individuo,
						intervaloFechasIndividuo);
				LogUtil.logTime("Consultando puntos de cierre", 3);
				List<Point> puntosCierre = daoDatoHistorico.consultarPuntosCierre(individuo, intervaloFechasIndividuo);

				Date lastProcessedDate = null;
				if (puntosApertura.isEmpty()) {
					lastProcessedDate = intervaloFechasIndividuo.getHighInterval();
					procesarIndividuo(estadisticasManager, intervaloFechasIndividuo, null, puntosCierre);
				} else {
					lastProcessedDate = intervaloFechasIndividuo.getLowInterval();
				}
				for (Point point : puntosApertura) {
					if (point.getDate().after(lastProcessedDate)) {
						lastProcessedDate = procesarIndividuo(estadisticasManager, intervaloFechasIndividuo, point,
								puntosCierre);
					}
				}
				processed = (lastProcessedDate != null);
				if (processed) {
					fechaMayorQue = lastProcessedDate;
					fechaMenorOIgualQue = DateUtil.obtenerFechaMinima(this.maxFechaHistorico,
							DateUtil.adicionarMes(fechaMayorQue, mesesAAdicionar));
				}
			}
			if (processed && !validarYBorrarIndividuoInvalido(individuo)) {
				// this.actualizarOperacionSemanal(individuo);
			}
		}
	}

	private boolean validarYBorrarIndividuoInvalido(Individuo individuo) throws GeneticDAOException {
		boolean borrado = false;
		/*
		 * try { ProcesosAlternosProxy alternosManager = new ProcesosAlternosProxy(0);
		 * alternosManager.procesar(individuo); Individuo indBorrado =
		 * daoIndividuo.consultarIndividuo(individuo.getId()); borrado = (indBorrado ==
		 * null); } catch (SQLException | ClassNotFoundException | FileNotFoundException
		 * e) { e.printStackTrace(); }
		 */
		return borrado;
	}

	private Date procesarIndividuo(MongoEstadisticasManager estadisticasManager, DateInterval intervaloFechasIndividuo,
			Point puntoApertura, List<Point> puntosCierre) throws GeneticDAOException {

		MongoIndividuo individuo = estadisticasManager.getIndividuo();
		Order order = individuo.getCurrentOrder();
		if ((order == null) || (order.getCloseDate() != null)) {
			individuo.setCurrentOrder(null);
			order = procesarNuevaOperacion(individuo, intervaloFechasIndividuo, puntoApertura);
		}

		if (order == null) {
			return intervaloFechasIndividuo.getHighInterval();
		} else if (order.getOpenOperationValue() == 0.0D) {
			return order.getOpenDate();
		} else {
			individuo.setCurrentOrder(order);
			DateInterval intervaloCierre = new DateInterval();
			intervaloCierre.setLowInterval(
					DateUtil.obtenerFechaMaxima(order.getOpenDate(), intervaloFechasIndividuo.getLowInterval()));
			intervaloCierre.setHighInterval(
					DateUtil.obtenerFechaMaxima(DateUtil.adicionarMes(intervaloCierre.getLowInterval()),
							intervaloFechasIndividuo.getHighInterval()));

			Date returnDate = procesarOperacionActiva(individuo, intervaloCierre, puntosCierre);
			Order closeOrder = individuo.getCurrentOrder();
			if ((closeOrder != null) && (closeOrder.getCloseDate() != null)) {
				estadisticasManager.addOrder(closeOrder);
				individuo.setCurrentOrder(null);
			}
			return returnDate;
		}
	}

	private MongoOrder procesarNuevaOperacion(MongoIndividuo individuo, DateInterval intervaloFechasIndividuo,
			Point puntoApertura) throws GeneticDAOException {

		if (puntoApertura == null) {
			LogUtil.logTime(super.getName() + ": Individuo sin operaciones: " + individuo.getId(), 2);
			updateProcesoIndividuo(individuo, null, intervaloFechasIndividuo.getHighInterval());
			return null;
		}
		LogUtil.logTime("procesarNuevaOperacion:" + this.getName() + ";" + individuo.getId() + ";puntoApertura="
				+ DateUtil.getDateString(puntoApertura.getDate()), 1);

		Point puntoAnterior = daoDatoHistorico.consultarPuntoAnterior(puntoApertura.getDate());
		LogUtil.logTime("puntoAnterior apertura:" + DateUtil.getDateString(puntoAnterior.getDate()), 2);

		MongoOrder order = new MongoOrder();
		order.setOpenDate(puntoApertura.getDate());
		List<Point> points = null;
		if (puntoAnterior != null) {
			points = new ArrayList<Point>(2);
			points.add(puntoAnterior);
			points.add(puntoApertura);

			MongoOperacionesManager operacionesManager = new MongoOperacionesManager(dataClient);
			List<MongoOrder> ordenes = operacionesManager.calcularOperaciones(points, individuo);
			if ((ordenes != null) && (!ordenes.isEmpty())) {
				order = ordenes.get(0);
				individuo.setCurrentOrder(order);
				daoOperaciones.insertOrUpdate(order);
				updateProcesoIndividuo(individuo, ordenes.get(0), puntoApertura.getDate());
			}
		}
		return order;
	}

	private Date procesarOperacionActiva(MongoIndividuo individuo, DateInterval intervaloCierre,
			List<Point> puntosCierre) throws GeneticDAOException {
		LogUtil.logTime("procesarOperacionActiva:" + this.getName() + ";" + individuo.getId(), 1);
		Point puntoCierre = null;
		MongoOrder closedOrder = null;
		if (individuo.getCurrentOrder() == null) {
			return null;
		}
		while ((closedOrder == null) || (closedOrder.getCloseDate() == null)) {
			LogUtil.logTime("antes de puntoCierreByIndicadores", 3);
			Point puntoCierreByIndicadores = null;
			for (Point point : puntosCierre) {
				if (point.getDate().after(intervaloCierre.getLowInterval())) {
					puntoCierreByIndicadores = point;
				}
			}
			LogUtil.logTime("puntoCierreByIndicadores:" + ((puntoCierreByIndicadores == null) ? null
					: DateUtil.getDateString(puntoCierreByIndicadores.getDate())), 2);
			Point puntoCierreByTakeStop = daoDatoHistorico.consultarPuntoCierreByTakeOrStop(individuo.getCurrentOrder(),
					intervaloCierre);
			LogUtil.logTime("puntoCierreByTakeStop:" + ((puntoCierreByTakeStop == null) ? null
					: DateUtil.getDateString(puntoCierreByTakeStop.getDate())), 2);

			puntoCierre = getPuntoMinimo(puntoCierreByIndicadores, puntoCierreByTakeStop);
			if (puntoCierre == null) {
				return intervaloCierre.getHighInterval();
			}
			Point puntoAnterior = daoDatoHistorico.consultarPuntoAnterior(puntoCierre.getDate());
			LogUtil.logTime("puntoAnterior cierre:" + DateUtil.getDateString(puntoAnterior.getDate()), 2);

			List<Point> points = null;
			if (puntoAnterior != null) {
				points = new ArrayList<Point>(2);
				points.add(puntoAnterior);
				points.add(puntoCierre);
				MongoOperacionesManager operacionesManager = new MongoOperacionesManager(dataClient);
				List<MongoOrder> ordenes = operacionesManager.calcularOperaciones(points, individuo);
				if ((ordenes != null) && (!ordenes.isEmpty())) {
					closedOrder = ordenes.get(0);
					if (closedOrder.getCloseDate() != null) {
						individuo.setCurrentOrder(closedOrder);
						LogUtil.logTime("antes de retrocesos:", 3);
						operacionesManager.calcularRetrocesoOrden(closedOrder);
						LogUtil.logTime("despues de retrocesos:", 3);

						daoOperaciones.insertOrUpdate(closedOrder);
						individuo.setCurrentOrder(null);
						updateProcesoIndividuo(individuo, closedOrder, closedOrder.getCloseDate());
						individuo.setCurrentOrder(closedOrder);
					}
				}
			}
			intervaloCierre.setLowInterval(puntoCierre.getDate());
		}
		if (puntoCierre == null) {
			return intervaloCierre.getHighInterval();
		} else {
			return puntoCierre.getDate();
		}
	}

	private void updateProcesoIndividuo(MongoIndividuo individuo, MongoOrder order, Date fechaHistorico)
			throws GeneticDAOException {
		ProcesoEjecucionDTO procesoEjecucionIndividuo = individuo.getProcesoEjecucion();
		if (procesoEjecucionIndividuo == null) {
			procesoEjecucionIndividuo = new ProcesoEjecucionDTO();
		}
		procesoEjecucionIndividuo.setMaxFechaHistorico(fechaHistorico);
		procesoEjecucionIndividuo.setFechaProceso(new Date());
		individuo.setProcesoEjecucion(procesoEjecucionIndividuo);
		daoIndividuo.insertOrUpdate(individuo);
	}

	private Point getPuntoMinimo(Point p1, Point p2) {
		if ((p1 == null) && (p2 == null)) {
			return null;
		}
		if ((p1 != null) && (p2 == null)) {
			return p1;
		}
		if ((p1 == null) && (p2 != null)) {
			return p2;
		}
		if (p1.getDate().before(p2.getDate())) {
			return p1;
		} else {
			return p2;
		}
	}

	public Date getMaxFechaHistorico() {
		return maxFechaHistorico;
	}

	public void setMaxFechaHistorico(Date maxFechaHistorico) {
		this.maxFechaHistorico = maxFechaHistorico;
	}

	public Date getMinFechaHistorico() {
		return minFechaHistorico;
	}

	public void setMinFechaHistorico(Date minFechaHistorico) {
		this.minFechaHistorico = minFechaHistorico;
	}

}

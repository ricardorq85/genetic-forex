/*
close * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread.mongo;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.dao.mongodb.MongoEstadisticasIndividuoDAO;
import forex.genetic.dao.mongodb.MongoIndividuoDAO;
import forex.genetic.dao.mongodb.MongoOperacionesDAO;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.entities.dto.ProcesoEjecucionDTO;
import forex.genetic.entities.mongo.MongoEstadistica;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.entities.mongo.MongoOrder;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.mongodb.MongoOperacionesManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class MongoProcesarIndividuoThread extends Thread {

	private List<MongoIndividuo> individuos = null;
	private MongoDatoHistoricoDAO daoDatoHistorico;
	private MongoOperacionesDAO daoOperaciones;
	private MongoIndividuoDAO daoIndividuo;
	private MongoEstadisticasIndividuoDAO daoEstadisticas;
	private Date maxFechaHistorico = null;
	private Date minFechaHistorico = null;

	/**
	 *
	 * @param name
	 * @param individuos
	 */
	public MongoProcesarIndividuoThread(String name, List<MongoIndividuo> individuos) {
		super(name);
		this.individuos = individuos;
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

	@Override
	public void run() {
		try {
			LogUtil.logTime("Inicia proceso para " + super.getName(), 1);
			daoDatoHistorico = new MongoDatoHistoricoDAO();
			daoOperaciones = new MongoOperacionesDAO();
			daoIndividuo = new MongoIndividuoDAO();
			daoEstadisticas = new MongoEstadisticasIndividuoDAO();
			for (MongoIndividuo individuo : individuos) {
				runIndividuo(individuo);
			}
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (GeneticDAOException e) {
			e.printStackTrace();
		}
	}

	protected void runIndividuo(MongoIndividuo individuo) throws ClassNotFoundException, GeneticDAOException {
		try {
			if (!validarYBorrarIndividuoInvalido(individuo)) {
				ProcesoEjecucionDTO procesoEjecucionIndividuo = individuo.getProcesoEjecucion();
				Date fechaMayorQue = ((procesoEjecucionIndividuo == null)
						|| (procesoEjecucionIndividuo.getMaxFechaHistorico() == null)) ? this.minFechaHistorico
								: procesoEjecucionIndividuo.getMaxFechaHistorico();
				Date fechaMenorOIgualQue = DateUtil.obtenerFechaMinima(this.maxFechaHistorico,
						DateUtil.adicionarMes(fechaMayorQue, 1));
				boolean processed = true;

				while (processed && !fechaMenorOIgualQue.after(this.maxFechaHistorico)
						&& fechaMenorOIgualQue.after(fechaMayorQue)) {
					DateInterval intervaloFechasIndividuo = new DateInterval(fechaMayorQue, fechaMenorOIgualQue);

					Date lastProcessedDate = procesarIndividuo(individuo, intervaloFechasIndividuo);
					processed = (lastProcessedDate != null);
					if (processed) {
						fechaMayorQue = lastProcessedDate;
						fechaMenorOIgualQue = DateUtil.obtenerFechaMinima(this.maxFechaHistorico,
								DateUtil.adicionarMes(fechaMayorQue, 1));
					}
				}
				if (processed && !validarYBorrarIndividuoInvalido(individuo)) {
					// this.actualizarOperacionSemanal(individuo);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage() + ": " + individuo.getId());
		} catch (ParseException ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage() + ": " + individuo.getId());
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

	private Date procesarIndividuo(MongoIndividuo individuo, DateInterval intervaloFechasIndividuo)
			throws SQLException, ClassNotFoundException, ParseException, GeneticDAOException {

		LogUtil.logTime(super.getName() + ":" + individuo.getId() + "," + intervaloFechasIndividuo.toString(), 1);

		Order order = individuo.getCurrentOrder();
		MongoEstadistica estadisticaAnterior = daoEstadisticas.getLast(individuo);
		if (order == null) {
			order = procesarNuevaOperacion(individuo, intervaloFechasIndividuo);
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

			Date returnDate = procesarOperacionActiva(individuo, intervaloCierre);
			Order closeOrder = individuo.getCurrentOrder();
			MongoEstadistica estadisticaNueva = new MongoEstadistica(estadisticaAnterior,
					intervaloFechasIndividuo.getLowInterval());
			estadisticaNueva.add(closeOrder);
			individuo.setCurrentOrder(null);
			return returnDate;
		}
	}

	private MongoOrder procesarNuevaOperacion(MongoIndividuo individuo, DateInterval intervaloFechasIndividuo)
			throws GeneticDAOException {

		LogUtil.logTime("procesarNuevaOperacion:" + this.getName() + ";" + individuo.getId(), 1);
		Point puntoApertura = daoDatoHistorico.consultarProximoPuntoApertura(individuo, intervaloFechasIndividuo);
		if (puntoApertura == null) {
			LogUtil.logTime(super.getName() + ": Individuo sin operaciones: " + individuo.getId(), 2);
			updateProcesoIndividuo(individuo, null, intervaloFechasIndividuo.getHighInterval());
			return null;
		}
		LogUtil.logTime("procesarNuevaOperacion:" + this.getName() + ";" + individuo.getId() + ";puntoApertura="
				+ DateUtil.getDateString(puntoApertura.getDate()), 1);

		Point puntoAnterior = daoDatoHistorico.consultarPuntoAnterior(puntoApertura.getDate());
		MongoOrder order = new MongoOrder();
		order.setOpenDate(puntoApertura.getDate());
		List<Point> points = null;
		if (puntoAnterior != null) {
			points = new ArrayList<Point>(2);
			points.add(puntoAnterior);
			points.add(puntoApertura);

			MongoOperacionesManager operacionesManager = new MongoOperacionesManager();
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

	private Date procesarOperacionActiva(MongoIndividuo individuo, DateInterval intervaloCierre)
			throws GeneticDAOException {
		LogUtil.logTime("procesarOperacionActiva:" + this.getName() + ";" + individuo.getId(), 1);
		if (individuo.getCurrentOrder() == null) {
			return null;
		}
		Point puntoCierreByIndicadores = daoDatoHistorico.consultarPuntoCierre(individuo, intervaloCierre);
		Point puntoCierreByTakeStop = daoDatoHistorico.consultarPuntoCierreByTakeOrStop(individuo.getCurrentOrder(),
				intervaloCierre);
		Point puntoCierre = getPuntoMinimo(puntoCierreByIndicadores, puntoCierreByTakeStop);
		if (puntoCierre == null) {
			return intervaloCierre.getHighInterval();
		}
		Point puntoAnterior = daoDatoHistorico.consultarPuntoAnterior(puntoCierre.getDate());
		List<Point> points = null;
		if (puntoAnterior != null) {
			points = new ArrayList<Point>(2);
			points.add(puntoAnterior);
			points.add(puntoCierre);
			MongoOperacionesManager operacionesManager = new MongoOperacionesManager();
			List<MongoOrder> ordenes = operacionesManager.calcularOperaciones(points, individuo);
			if ((ordenes != null) && (!ordenes.isEmpty())) {
				MongoOrder closedOrder = ordenes.get(0);
				daoOperaciones.insertOrUpdate(closedOrder);
				updateProcesoIndividuo(individuo, closedOrder, closedOrder.getCloseDate());
			}
		}
		return puntoCierre.getDate();
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

}

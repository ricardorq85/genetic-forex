/*
close * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread.mongo;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.IDatoHistoricoDAO;
import forex.genetic.dao.IIndividuoDAO;
import forex.genetic.dao.IOperacionesDAO;
import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.dao.mongodb.MongoIndividuoDAO;
import forex.genetic.dao.mongodb.MongoOperacionesDAO;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.entities.dto.ProcesoEjecucionDTO;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.OperacionesManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class MongoProcesarIndividuoThread extends Thread {

	private List<MongoIndividuo> individuos = null;
	private IDatoHistoricoDAO daoDatoHistorico;
	private IOperacionesDAO daoOperaciones;
	private IIndividuoDAO daoIndividuo, daoIndividuoDDL;
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
			for (MongoIndividuo individuo : individuos) {
				runIndividuo(individuo);
			}
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (GeneticDAOException e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn);
		}
	}

	protected void runIndividuo(MongoIndividuo individuo) throws ClassNotFoundException, GeneticDAOException {
		try {
			// daoIndividuoDDL.insertarIndividuoIndicadoresColumnas(individuo.getId());
			// connDDL.commit();
			if (!validarYBorrarIndividuoInvalido(individuo)) {
				Date fechaMayorQue = (individuo.getFechaHistorico() == null) ? this.minFechaHistorico
						: individuo.getFechaHistorico();
				Date fechaMenorOIgualQue = DateUtil.obtenerFechaMinima(this.maxFechaHistorico,
						DateUtil.adicionarMes(fechaMayorQue, 12));
				boolean processed = true;
				while (processed && !fechaMenorOIgualQue.after(this.maxFechaHistorico)
						&& fechaMenorOIgualQue.after(fechaMayorQue)) {
					DateInterval intervaloFechasIndividuo = new DateInterval(fechaMayorQue, fechaMenorOIgualQue);

					Date lastProcessedDate = procesarIndividuo(individuo, intervaloFechasIndividuo);

					processed = (lastProcessedDate != null);
					if (processed) {
						fechaMayorQue = lastProcessedDate;
						fechaMenorOIgualQue = DateUtil.obtenerFechaMinima(this.maxFechaHistorico,
								DateUtil.adicionarMes(fechaMayorQue, 12));
					}
				}
				if (processed && !validarYBorrarIndividuoInvalido(individuo)) {
					// this.actualizarOperacionSemanal(individuo);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage() + ": " + individuo.getId());
			JDBCUtil.rollback(conn);
		} catch (ParseException ex) {
			ex.printStackTrace();
			System.err.println(ex.getMessage() + ": " + individuo.getId());
			JDBCUtil.rollback(conn);
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
		List<Point> points;
		Date fechaInicialHistorico;
		int indexFecha = 0;
		int duracionPromedio = Math.max(3000, daoIndividuo.duracionPromedioMinutos(individuo.getId()));
		List<? extends Point> puntosApertura = daoDatoHistorico.consultarPuntosApertura(individuo,
				intervaloFechasIndividuo);
		LogUtil.logTime(super.getName() + ":" + individuo.getId() + "," + intervaloFechasIndividuo.toString()
				+ ",Fechas consultadas: " + puntosApertura.size(), 1);
		ProcesoEjecucionDTO procesoEjecucionIndividuo = individuo.getProcesoEjecucion();
		if (procesoEjecucionIndividuo.getFechaAperturaActiva() == null) {
			if (puntosApertura.isEmpty()) {
				LogUtil.logTime(super.getName() + ": Individuo sin operaciones: " + individuo.getId(), 2);
				updateProceso(individuo, intervaloFechasIndividuo.getHighInterval());
				return intervaloFechasIndividuo.getHighInterval();
			} else {
//				indexFecha = proximaFechaApertura(fechas, intervaloFechasIndividuo.getLowInterval(), indexFecha);
				indexFecha = 0;
//				fechaInicialHistorico = fechas.get(indexFecha);
				fechaInicialHistorico = puntosApertura.get(indexFecha).getDate();
//				points = daoDatoHistorico.consultarHistorico(fechaInicialHistorico,
//						DateUtil.adicionarMinutos(fechaInicialHistorico, duracionPromedio));
			}
		} else {
			fechaInicialHistorico = intervaloFechasIndividuo.getLowInterval();
//			Date nextFechaHistorico = daoDatoHistorico.getFechaHistoricaMinima(fechaInicialHistorico);
//			points = daoDatoHistorico.consultarHistorico(fechaInicialHistorico,
//					DateUtil.adicionarMinutos(nextFechaHistorico, duracionPromedio));
		}

//		daoIndividuo.consultarDetalleIndividuoProceso(individuo, this.maxFechaHistorico);
		OperacionesManager operacionesManager = new OperacionesManager();
		Date lastDate = fechaInicialHistorico;
		if ((points != null) && (!points.isEmpty())) {
			lastDate = points.get(points.size() - 1).getDate();
		}
		Date fechaRetorno = lastDate;
		while ((lastDate.before(intervaloFechasIndividuo.getHighInterval()))
				|| ((lastDate.equals(intervaloFechasIndividuo.getHighInterval())))) {
			if ((points != null) && (!points.isEmpty())) {
				lastDate = points.get(points.size() - 1).getDate();
				fechaRetorno = lastDate;
				LogUtil.logTime("Procesar Individuo;" + this.getName() + ";" + individuo.getId() + ";lastDate="
						+ DateUtil.getDateString(lastDate), 1);
				List<Order> ordenes = operacionesManager.calcularOperaciones(points, individuo);
				Order updateOrder = null;
				if (individuo.getFechaApertura() != null) {
					if (ordenes.get(0).getCloseDate() != null) {
						daoOperaciones.updateOperacion(individuo, ordenes.get(0), individuo.getFechaApertura());
						updateOrder = ordenes.get(0);
					}
					ordenes.remove(0);
					individuo.setFechaApertura(null);
				}
				daoOperaciones.insertOperaciones(individuo, ordenes);
				this.updateProceso(lastDate, individuo.getId());
				if (updateOrder != null) {
					ordenes.add(updateOrder);
				}
				individuo.setOrdenes(ordenes);
				operacionesManager.procesarMaximosReproceso(individuo);
				if (individuo.getCurrentOrder() != null) {
					individuo.setFechaApertura(individuo.getCurrentOrder().getOpenDate());
					duracionPromedio = Math.max(3000, daoIndividuo.duracionPromedioMinutos(individuo.getId()));
				}
			} else {
				lastDate = fechaInicialHistorico;
			}
			if (DateUtil.anyoMesMayorQue(fechaInicialHistorico, lastDate)) {
				if (validarYBorrarIndividuoInvalido(individuo)) {
					return null;
				}
			}
			if (individuo.getFechaApertura() == null) {
				int index = proximaFechaApertura(fechas, lastDate, indexFecha);
				if (((index > 0) && (index == indexFecha)) || (index >= fechas.size())) {
					this.updateProceso(intervaloFechasIndividuo.getHighInterval(), individuo.getId());
					break;
				} else {
					indexFecha = index;
					fechaInicialHistorico = fechas.get(indexFecha);
					points = daoDatoHistorico.consultarHistorico(fechaInicialHistorico,
							DateUtil.adicionarMinutos(fechaInicialHistorico, duracionPromedio));
				}
			} else {
				fechaInicialHistorico = getLastDate(lastDate, fechaInicialHistorico);
				Date nextFechaHistorico = daoDatoHistorico.getFechaHistoricaMinima(fechaInicialHistorico);
				if (nextFechaHistorico != null) {
					points = daoDatoHistorico.consultarHistorico(fechaInicialHistorico,
							DateUtil.adicionarMinutos(nextFechaHistorico, duracionPromedio));
				} else {
					break;
				}
			}
		}
		return fechaRetorno;
	}

	@SuppressWarnings("unchecked")
	private void updateProceso(MongoIndividuo individuo, Date fechaHistorico) throws GeneticDAOException {
		ProcesoEjecucionDTO procesoEjecucionIndividuo = individuo.getProcesoEjecucion();
		if (procesoEjecucionIndividuo == null) {
			procesoEjecucionIndividuo = new ProcesoEjecucionDTO();
		}
		procesoEjecucionIndividuo.setFechaProceso(fechaHistorico);
		procesoEjecucionIndividuo.setFechaProceso(new Date());
		daoIndividuo.insertOrUpdate(individuo);
	}

	private Date getLastDate(Date lastDate, Date fechaInicialHistorico) {
		if (lastDate.equals(fechaInicialHistorico)) {
			return (DateUtil.adicionarMinutos(lastDate, 1));
		} else {
			return lastDate;
		}
	}

}

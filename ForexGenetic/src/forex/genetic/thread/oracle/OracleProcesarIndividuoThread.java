/*
close * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread.oracle;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.IProcesoEjecucionDAO;
import forex.genetic.dao.oracle.OracleDatoHistoricoDAO;
import forex.genetic.dao.oracle.OracleIndividuoDAO;
import forex.genetic.dao.oracle.OracleOperacionesDAO;
import forex.genetic.dao.oracle.OracleProcesoEjecucionDAO;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.OperacionesManager;
import forex.genetic.proxy.ProcesosAlternosProxy;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class OracleProcesarIndividuoThread extends Thread {

	private List<Individuo> individuos = null;
	private Connection conn = null, connDDL = null;
	private OracleDatoHistoricoDAO daoHistorico;
	private OracleOperacionesDAO daoOperaciones;
	private OracleIndividuoDAO daoIndividuo, daoIndividuoDDL;
	private IProcesoEjecucionDAO daoProceso;
	private Date maxFechaHistorico = null;
	private Date minFechaHistorico = null;

	/**
	 *
	 * @param name
	 * @param individuos
	 */
	public OracleProcesarIndividuoThread(String name, List<Individuo> individuos) {
		super(name);
		this.individuos = individuos;
	}

	@Override
	public void run() {
		try {
			LogUtil.logTime("Inicia proceso para " + super.getName(), 1);
			conn = JDBCUtil.getConnection();
			connDDL = JDBCUtil.getConnection();
			daoHistorico = new OracleDatoHistoricoDAO(conn);
			daoOperaciones = new OracleOperacionesDAO(conn);
			daoIndividuo = new OracleIndividuoDAO(conn);
			daoProceso = new OracleProcesoEjecucionDAO(conn);

			daoIndividuoDDL = new OracleIndividuoDAO(connDDL);
			for (Individuo individuo : individuos) {
				runIndividuo(individuo);
			}
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (GeneticDAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCUtil.close(conn);
		}
	}

	protected void runIndividuo(Individuo individuo) throws ClassNotFoundException, GeneticDAOException {
		try {
			daoIndividuoDDL.insertarIndividuoIndicadoresColumnas(individuo.getId());
			connDDL.commit();
			if (!validarYBorrarIndividuoInvalido(individuo)) {
			//if (true) {
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
		try {
			ProcesosAlternosProxy alternosManager = new ProcesosAlternosProxy(0);
			alternosManager.procesar(individuo);
			Individuo indBorrado = daoIndividuo.consultarIndividuo(individuo.getId());
			borrado = (indBorrado == null);
		} catch (SQLException | ClassNotFoundException | FileNotFoundException e) {
			e.printStackTrace();
		}
		return borrado;
	}

	private int proximaFechaApertura(List<Date> fechas, Date fechaInicial, int index) {
		int new_index = index;
		while ((fechaInicial != null) && (new_index < fechas.size()) && (fechas.get(new_index).before(fechaInicial))) {
			new_index++;
		}
		return new_index;
	}

	private Date procesarIndividuo(Individuo individuo, DateInterval intervaloFechasIndividuo)
			throws SQLException, ClassNotFoundException, ParseException, GeneticDAOException {
		List<Point> points;
		Date fechaInicialHistorico;
		int indexFecha = 0;
		long duracionPromedio = Math.max(3000, daoOperaciones.duracionPromedioMinutos(individuo.getId()));
		List<Date> fechas = daoHistorico.consultarPuntosApertura(intervaloFechasIndividuo, individuo.getId());
		//fechas.set(0, DateUtil.obtenerFecha("2008/08/05 20:32"));
		LogUtil.logTime(super.getName() + ":" + individuo.getId() + "," + intervaloFechasIndividuo.toString()
				+ ",Fechas consultadas: " + fechas.size(), 1);
		if (individuo.getFechaApertura() == null) {
			if (fechas.isEmpty()) {
				LogUtil.logTime(super.getName() + ": Individuo sin operaciones: " + individuo.getId(), 2);
				this.updateProceso(intervaloFechasIndividuo.getHighInterval(), individuo.getId());
				return intervaloFechasIndividuo.getHighInterval();
			} else {
				indexFecha = proximaFechaApertura(fechas, intervaloFechasIndividuo.getLowInterval(), indexFecha);
				fechaInicialHistorico = fechas.get(indexFecha);
				points = daoHistorico.consultarHistorico(fechaInicialHistorico,
						DateUtil.adicionarMinutos(fechaInicialHistorico, (int) duracionPromedio));
			}
		} else {
			fechaInicialHistorico = intervaloFechasIndividuo.getLowInterval();
			Date nextFechaHistorico = daoHistorico.getFechaHistoricaMinima(fechaInicialHistorico);
			points = daoHistorico.consultarHistorico(fechaInicialHistorico,
					DateUtil.adicionarMinutos(nextFechaHistorico, (int) duracionPromedio));
		}

		daoIndividuo.consultarDetalleIndividuoProceso(individuo, this.maxFechaHistorico);
		OperacionesManager operacionesManager = new OperacionesManager(conn);
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
				List<Order> ordenes = (List<Order>) operacionesManager.calcularOperaciones(points, individuo);
				Order updateOrder = null;
				if (individuo.getFechaApertura() != null) {
					if (ordenes.get(0).getCloseDate() != null) {
						//daoOperaciones.update(individuo, ordenes.get(0), individuo.getFechaApertura());
						updateOrder = ordenes.get(0);
					}
					ordenes.remove(0);
					individuo.setFechaApertura(null);
				}
				//daoOperaciones.insert(individuo, ordenes);
				this.updateProceso(lastDate, individuo.getId());
				if (updateOrder != null) {
					ordenes.add(updateOrder);
				}
				individuo.setOrdenes(ordenes);
				operacionesManager.procesarMaximosReproceso(individuo);
				if (individuo.getCurrentOrder() != null) {
					individuo.setFechaApertura(individuo.getCurrentOrder().getOpenDate());
					duracionPromedio = Math.max(3000, daoOperaciones.duracionPromedioMinutos(individuo.getId()));
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
					points = daoHistorico.consultarHistorico(fechaInicialHistorico,
							DateUtil.adicionarMinutos(fechaInicialHistorico, (int) duracionPromedio));
				}
			} else {
				fechaInicialHistorico = getLastDate(lastDate, fechaInicialHistorico);
				Date nextFechaHistorico = daoHistorico.getFechaHistoricaMinima(fechaInicialHistorico);
				if (nextFechaHistorico != null) {
					points = daoHistorico.consultarHistorico(fechaInicialHistorico,
							DateUtil.adicionarMinutos(nextFechaHistorico, (int) duracionPromedio));
				} else {
					break;
				}
			}
		}
		return fechaRetorno;
	}

	private void updateProceso(Date fechaHistorico, String idIndividuo) throws GeneticDAOException {
		int processed = daoProceso.updateProceso(fechaHistorico, idIndividuo);
		if (processed == 0) {
			daoProceso.insertProceso(fechaHistorico, idIndividuo);
		}
		daoProceso.commit();
	}

	private Date getLastDate(Date lastDate, Date fechaInicialHistorico) {
		if (lastDate.equals(fechaInicialHistorico)) {
			return (DateUtil.adicionarMinutos(lastDate, 1));
		} else {
			return lastDate;
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

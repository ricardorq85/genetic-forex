/*
close * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.IndividuoDAO;
import forex.genetic.dao.OperacionSemanalDAO;
import forex.genetic.dao.OperacionesDAO;
import forex.genetic.dao.ProcesoPoblacionDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.manager.OperacionesManager;
import forex.genetic.proxy.ProcesosAlternosProxy;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class ProcesarIndividuoThreadBD extends Thread {

	private List<Individuo> individuos = null;
	private Connection conn = null;
	private DatoHistoricoDAO daoHistorico;
	private OperacionesDAO daoOperaciones;
	private OperacionSemanalDAO daoOperacionSemanal;
	private IndividuoDAO daoIndividuo;
	private ProcesoPoblacionDAO daoProceso;
	private Date maxFechaHistorico = null;
	private Date minFechaHistorico = null;

	/**
	 *
	 * @param name
	 * @param individuos
	 */
	public ProcesarIndividuoThreadBD(String name, List<Individuo> individuos) {
		super(name);
		this.individuos = individuos;
	}

	@Override
	public void run() {
		try {
			LogUtil.logTime("Inicia proceso para " + super.getName(), 1);
			conn = JDBCUtil.getConnection();
			daoHistorico = new DatoHistoricoDAO(conn);
			daoOperaciones = new OperacionesDAO(conn);
			daoIndividuo = new IndividuoDAO(conn);
			daoProceso = new ProcesoPoblacionDAO(conn);
			daoOperacionSemanal = new OperacionSemanalDAO(conn);
			for (Individuo individuo : individuos) {
				try {
					procesarIndividuo(individuo);
					if (!validarYBorrarIndividuoInvalido(individuo)) {
						actualizarOperacionSemanal(individuo);
					}
				} catch (SQLException ex) {
					JDBCUtil.rollback(conn);
					ex.printStackTrace();
					System.err.println(ex.getMessage() + " " + individuo.getId());					
				} catch (ParseException ex) {
					JDBCUtil.rollback(conn);
					ex.printStackTrace();
					System.err.println(ex.getMessage() + " " + individuo.getId());
				}
			}
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		} finally {
			JDBCUtil.close(conn);
		}
	}

	private boolean validarYBorrarIndividuoInvalido(Individuo individuo) {
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

	private void actualizarOperacionSemanal(Individuo individuo) throws SQLException {
		// Semana
		int cDelete = daoOperacionSemanal.deleteOperacionesSemana(individuo, "OPERACION_X_SEMANA");
		int cInsert = daoOperacionSemanal.insertOperacionesSemana(individuo);
		conn.commit();
		LogUtil.logTime(individuo.getId() + ". Borrados OPERACION_X_SEMANA: " + cDelete, 2);
		LogUtil.logTime(individuo.getId() + ". Insertados OPERACION_X_SEMANA: " + cInsert, 2);
		// TODO: hacerlo por las semanas de OPERACION x individuo, no de
		// OPERACION_X_SEMANA
		cInsert = daoOperacionSemanal.insertSemanas(individuo);
		conn.commit();
		LogUtil.logTime("Insertados SEMANAS: " + cInsert, 1);
		// Mes
		cDelete = daoOperacionSemanal.deleteOperacionesSemana(individuo, "OPERACIONES_ACUM_SEMANA_MES");
		cInsert = daoOperacionSemanal.insertOperacionesSemanaAcumuladas(individuo, "OPERACIONES_ACUM_SEMANA_MES", -2000,
				1);
		conn.commit();
		LogUtil.logTime(individuo.getId() + ". Borrados OPERACIONES_ACUM_SEMANA_MES: " + cDelete, 2);
		LogUtil.logTime(individuo.getId() + ". Insertados OPERACIONES_ACUM_SEMANA_MES: " + cInsert, 2);
		// Anyo
		cDelete = daoOperacionSemanal.deleteOperacionesSemana(individuo, "OPERACIONES_ACUM_SEMANA_ANYO");
		cInsert = daoOperacionSemanal.insertOperacionesSemanaAcumuladas(individuo, "OPERACIONES_ACUM_SEMANA_ANYO",
				-5000, 12);
		conn.commit();
		LogUtil.logTime(individuo.getId() + ". Borrados OPERACIONES_ACUM_SEMANA_ANYO: " + cDelete, 2);
		LogUtil.logTime(individuo.getId() + ". Insertados OPERACIONES_ACUM_SEMANA_ANYO: " + cInsert, 2);
		// Consolidado
		cDelete = daoOperacionSemanal.deleteOperacionesSemana(individuo, "OPERACIONES_ACUM_SEMANA_CONSOL");
		cInsert = daoOperacionSemanal.insertOperacionesSemanaAcumuladas(individuo, "OPERACIONES_ACUM_SEMANA_CONSOL",
				-10000, 240);
		conn.commit();
		LogUtil.logTime(individuo.getId() + ". Borrados OPERACIONES_ACUM_SEMANA_CONSOL: " + cDelete, 2);
		LogUtil.logTime(individuo.getId() + ". Insertados OPERACIONES_ACUM_SEMANA_CONSOL: " + cInsert, 2);
		// Previo
		cDelete = daoOperacionSemanal.deleteOperacionesSemana(individuo, "PREVIO_TOFILESTRING");
		cInsert = daoOperacionSemanal.insertOperacionesSemanaPrevio(individuo);
		conn.commit();
		LogUtil.logTime(individuo.getId() + ". Borrados PREVIO_TOFILESTRING: " + cDelete, 2);
		LogUtil.logTime(individuo.getId() + ". Insertados PREVIO_TOFILESTRING: " + cInsert, 2);
	}

	private int proximaFechaApertura(List<Date> fechas, Date fechaInicial, int index) {
		int new_index = index;
		while ((fechaInicial != null) && (new_index < fechas.size()) && (fechas.get(new_index).before(fechaInicial))) {
			new_index++;
		}
		return new_index;
	}

	private void procesarIndividuo(Individuo individuo) throws SQLException, ClassNotFoundException, ParseException {
		List<Point> points;
		Date fechaInicialHistorico;
		int indexFecha = 0;
		int duracionPromedio = Math.max(5000, daoIndividuo.duracionPromedioMinutos(individuo.getId()));
		daoIndividuo.insertarIndividuoIndicadoresColumnas(individuo.getId());
		conn.commit();
		Date fechaMayorQue = (individuo.getFechaHistorico() == null) ? this.minFechaHistorico
				: individuo.getFechaHistorico();
		List<Date> fechas = daoIndividuo.consultarPuntosApertura(fechaMayorQue, individuo.getId());
		LogUtil.logTime(super.getName() + ": Fechas consultadas: " + fechas.size() + " :" + individuo.getId(), 1);
		if (individuo.getFechaApertura() == null) {
			if (fechas.isEmpty()) {
				LogUtil.logTime(super.getName() + ": Individuo sin operaciones: " + individuo.getId(), 1);
				this.updateProceso(maxFechaHistorico, individuo.getId());
				conn.commit();
				return;
			} else {
				indexFecha = proximaFechaApertura(fechas, individuo.getFechaHistorico(), indexFecha);
				fechaInicialHistorico = fechas.get(indexFecha);
				points = daoHistorico.consultarHistorico(fechaInicialHistorico,
						DateUtil.adicionarMinutos(fechaInicialHistorico, duracionPromedio));
			}
		} else {
			fechaInicialHistorico = individuo.getFechaHistorico();
			Date nextFechaHistorico = daoHistorico.getFechaHistoricaMinima(fechaInicialHistorico);
			points = daoHistorico.consultarHistorico(fechaInicialHistorico,
					DateUtil.adicionarMinutos(nextFechaHistorico, duracionPromedio));
		}

		daoIndividuo.consultarDetalleIndividuoProceso(individuo, this.maxFechaHistorico);
		OperacionesManager operacionesManager = new OperacionesManager(conn);
		Date lastDate = fechaInicialHistorico;
		if ((points != null) && (!points.isEmpty())) {
			lastDate = points.get(points.size() - 1).getDate();
		}
		while ((lastDate.before(maxFechaHistorico)) || ((lastDate.equals(maxFechaHistorico)))) {
			if ((points != null) && (!points.isEmpty())) {
				lastDate = points.get(points.size() - 1).getDate();
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
				conn.commit();
				if (updateOrder != null) {
					ordenes.add(updateOrder);
				}
				individuo.setOrdenes(ordenes);
				operacionesManager.procesarMaximosReproceso(individuo);
				if (individuo.getCurrentOrder() != null) {
					individuo.setFechaApertura(individuo.getCurrentOrder().getOpenDate());
				}
			} else {
				lastDate = fechaInicialHistorico;
			}
			if (individuo.getFechaApertura() == null) {
				int index = proximaFechaApertura(fechas, lastDate, indexFecha);
				if (((index > 0) && (index == indexFecha)) || (index >= fechas.size())) {
					this.updateProceso(maxFechaHistorico, individuo.getId());
					conn.commit();
					break;
				} else {
					indexFecha = index;
					fechaInicialHistorico = fechas.get(indexFecha);
					points = daoHistorico.consultarHistorico(fechaInicialHistorico,
							DateUtil.adicionarMinutos(fechaInicialHistorico, duracionPromedio));
				}
			} else {
				fechaInicialHistorico = getLastDate(lastDate, fechaInicialHistorico);
				Date nextFechaHistorico = daoHistorico.getFechaHistoricaMinima(fechaInicialHistorico);
				if (nextFechaHistorico != null) {
					points = daoHistorico.consultarHistorico(fechaInicialHistorico,
							DateUtil.adicionarMinutos(nextFechaHistorico, duracionPromedio));
				} else {
					break;
				}
			}
			duracionPromedio = Math.max(3000, daoIndividuo.duracionPromedioMinutos(individuo.getId()));
		}
	}

	private void updateProceso(Date fechaHistorico, String idIndividuo) throws SQLException {
		int processed = daoProceso.updateProceso(fechaHistorico, idIndividuo);
		if (processed == 0) {
			daoProceso.insertProceso(fechaHistorico, idIndividuo);
		}
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

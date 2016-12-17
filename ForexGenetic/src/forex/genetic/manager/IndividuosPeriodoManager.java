package forex.genetic.manager;

import static forex.genetic.util.LogUtil.logTime;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import forex.genetic.dao.EstrategiaOperacionPeriodoDAO;
import forex.genetic.dao.OperacionesDAO;
import forex.genetic.dao.ParametroDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.ParametroOperacionPeriodo;
import forex.genetic.entities.PipsAgrupado;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class IndividuosPeriodoManager {

	private Connection conn = null;
	private InclusionesManager inclusionesManager;
	private ParametroDAO parametroDAO;
	private OperacionesDAO operacionesDAO;
	private EstrategiaOperacionPeriodoDAO estrategiaOperacionPeriodoDAO;
	private static final String[] ORDERS = { "PIPS_SEMANA", "PIPS_MES", "PIPS_ANYO", "PIPS_TOTALES" };
	private static final Constants.OperationType[] TIPO_OPERACION = { Constants.OperationType.SELL,
			Constants.OperationType.BUY };
	// Para filtrar las operaciones por la cantidad de órdenes que podría
	// obtener
	private static final int MESES_CONSULTA = 1;
	private Random random = new Random();

	private Date parametroFechaInicioProceso, parametroFechaFinProceso;
	private int parametroMesesProceso, parametroDiasRotacion;

	public IndividuosPeriodoManager() throws ClassNotFoundException, SQLException {
		super();
		inclusionesManager = new InclusionesManager();
		conn = JDBCUtil.getConnection();
		parametroDAO = new ParametroDAO(conn);
		operacionesDAO = new OperacionesDAO(conn);
		estrategiaOperacionPeriodoDAO = new EstrategiaOperacionPeriodoDAO(conn);
		this.inclusionesManager.setEstrategiaOperacionPeriodoDAO(estrategiaOperacionPeriodoDAO);

		parametroFechaInicioProceso = parametroDAO.getDateValorParametro("FECHA_INDIVIDUO_PERIODO");
		parametroFechaFinProceso = parametroDAO.getDateValorParametro("FECHA_FIN_INDIVIDUO_PERIODO");
		parametroMesesProceso = parametroDAO.getIntValorParametro("MESES_INDIVIDUO_PERIODO");
		parametroDiasRotacion = parametroDAO.getIntValorParametro("DIAS_ROTACION_INDIVIDUO_PERIODO");
	}

	public int procesarIndividuosXPeriodo() throws SQLException {
		int inclusionesAcumuladas = 0;
		int mesesProceso = parametroMesesProceso;
		while (mesesProceso < 13) {
			logTime("Meses proceso: " + mesesProceso, 1);
			Date fechaFinProceso = parametroFechaFinProceso;
			Date fechaInicioProceso = DateUtil.adicionarMes(fechaFinProceso, -mesesProceso);
			while (fechaFinProceso.after(this.parametroFechaInicioProceso)) {
				logTime("Fecha Inicio Proceso:" + DateUtil.getDateString(fechaInicioProceso) + "; Fecha Fin Proceso:"
						+ DateUtil.getDateString(fechaFinProceso), 1);
				List<ParametroOperacionPeriodo> inclusiones = inclusionesManager.consultarInclusiones();
				logTime("Inclusiones:" + inclusiones.size(), 1);
				int contadorParametrosYaProcesados = 0;
				for (ParametroOperacionPeriodo paramBase : inclusiones) {
					for (int i = 0; i < TIPO_OPERACION.length; i++) {
						ParametroOperacionPeriodo param;
						try {
							param = paramBase.clone();
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
							param = null;
						}
						param.setFirstOrder(ORDERS[random.nextInt(ORDERS.length)]);
						param.setSecondOrder(ORDERS[random.nextInt(ORDERS.length)]);
						param.setFecha(new Date());
						param.setFechaInicial(fechaInicioProceso);
						param.setFechaFinal(fechaFinProceso);
						param.setTipoOperacion(TIPO_OPERACION[i]);
						if (param.isFiltroValido()) {
							logTime(param.toString(), 2);
							contadorParametrosYaProcesados += this.procesarIndividuosXPeriodo(param);
						} else {
							logTime("Filtros inválidos: " + param.toString(), 3);
						}
					}
				}
				// System.out.println("");
				logTime("Parametros ya procesados: " + contadorParametrosYaProcesados, 1);
				fechaFinProceso = DateUtil.adicionarDias(fechaFinProceso, -parametroDiasRotacion);
				fechaInicioProceso = DateUtil.adicionarMes(fechaFinProceso, -mesesProceso);
				inclusionesAcumuladas += inclusiones.size();
			}
			mesesProceso++;
		}
		return inclusionesAcumuladas;
	}

	public int procesarIndividuosXPeriodo(ParametroOperacionPeriodo param) throws SQLException {
		if (estrategiaOperacionPeriodoDAO.existe(param)) {
			logTime("Ya procesado:" + param.toString(), 3);
			System.out.print(".");
			return 1;
		} else {
			operacionesDAO.cleanOperacionesPeriodo();
			logTime("Registros borrados.", 2);

			int insertados = operacionesDAO.insertOperacionesPeriodo(param);
			conn.commit();
			logTime("Registro insertados TMP_TOFILESTRING: " + insertados, 2);

			procesarIndividuoYOperaciones(param, insertados);
			return 0;
		}
	}

	private void procesarIndividuoYOperaciones(ParametroOperacionPeriodo param, int insertados) throws SQLException {
		List<Individuo> ordenesCreadas = this.ejecutarIndividuosXPeriodo(param, insertados);
		int id = estrategiaOperacionPeriodoDAO.insert(param);
		param.setId(id);

		for (Individuo ind : ordenesCreadas) {
			estrategiaOperacionPeriodoDAO.insertOperacionesPeriodo(param, ind, ind.getOrdenes());
		}
		conn.commit();
		logTime(param.toString(), 2);
	}

	public List<Individuo> ejecutarIndividuosXPeriodo(ParametroOperacionPeriodo param, int insertados)
			throws SQLException {
		Date fechaPeriodo = param.getFechaInicial();
		List<Individuo> ordenesCreadas = new ArrayList<>();
		double pips = 0.0D;
		double pipsParalelas = 0.0D;
		int c = 0, cantidadParalelas = 0;
		PipsAgrupado agrupadoMinutos = new PipsAgrupado("yyyy/MM/dd HH:mm");
		PipsAgrupado agrupadoHoras = new PipsAgrupado("yyyy/MM/dd HH");
		PipsAgrupado agrupadoDias = new PipsAgrupado("yyyy/MM/dd");

		Date fechaInicial = fechaPeriodo;
		Date maxFechaCierre = null;
		List<String> individuosExcluyente = new ArrayList<>();
		if (insertados > 0) {
			// Se hace incremental porque podría consultar muchas operaciones y
			// generaría error
			while (param.getFechaFinal().after(fechaInicial)) {
				Date fechaFinal = DateUtil.obtenerFechaMinima(param.getFechaFinal(),
						DateUtil.adicionarMes(fechaInicial, MESES_CONSULTA));
				// Date fechaFinal = param.getFechaFinal();
				List<Individuo> ordenes = operacionesDAO.consultarOperacionesXPeriodo(fechaInicial, fechaFinal);
				for (Individuo individuo : ordenes) {
					Order order = individuo.getCurrentOrder();
					pipsParalelas += order.getPips();
					if (maxFechaCierre == null) {
						maxFechaCierre = new Date(order.getCloseDate().getTime());
					} else if (order.getCloseDate().after(maxFechaCierre)) {
						maxFechaCierre = new Date(order.getCloseDate().getTime());
					}
					if (order.getOpenDate().after(fechaPeriodo)) {
						ordenesCreadas.add(individuo);
						if (!individuosExcluyente.contains(individuo.getId())) {
							individuosExcluyente.add(individuo.getId());
						}
						logTime("Individuo=" + individuo.getId() + ",Orden=" + order, 2);
						pips += order.getPips();
						fechaPeriodo = order.getCloseDate();
						logTime("Fecha=" + DateUtil.getDateString(fechaPeriodo) + ",Pips=" + pips, 2);
						c++;
					}
					agrupadoMinutos.addOrder(order);
					agrupadoHoras.addOrder(order);
					agrupadoDias.addOrder(order);
				}
				cantidadParalelas += ordenes.size();
				fechaInicial = fechaFinal;
			}
		}
		agrupadoMinutos.finish();
		agrupadoHoras.finish();
		agrupadoDias.finish();

		param.setPipsTotales(pips);
		param.setCantidad(c);
		param.setPipsParalelas(pipsParalelas);
		param.setCantidadParalelas(cantidadParalelas);
		param.setPipsAgrupadoMinutos(agrupadoMinutos.getPips());
		param.setPipsAgrupadoHoras(agrupadoHoras.getPips());
		param.setPipsAgrupadoDias(agrupadoDias.getPips());
		param.setCantidadIndividuos(individuosExcluyente.size());
		param.setMaxFechaCierre(maxFechaCierre);
		logTime(param.getId() + ": " + param.getTipoOperacion() + ", Cantidad operaciones=" + c + ", Pips totales=" + pips + ", Pips paralelas=" + pipsParalelas, 1);
		return ordenesCreadas;
	}

	private void setPipsXAgrupacion(ParametroOperacionPeriodo param) throws SQLException {
		double pipsAgrupadoMinutos = operacionesDAO.consultarPipsXAgrupacion("YYYYMMDD HH24:MI");
		double pipsAgrupadoHoras = operacionesDAO.consultarPipsXAgrupacion("YYYYMMDD HH24");
		double pipsAgrupadoDias = operacionesDAO.consultarPipsXAgrupacion("YYYYMMDD");

		param.setPipsAgrupadoMinutos(pipsAgrupadoMinutos);
		param.setPipsAgrupadoHoras(pipsAgrupadoHoras);
		param.setPipsAgrupadoDias(pipsAgrupadoDias);
	}

	public EstrategiaOperacionPeriodoDAO getEstrategiaOperacionPeriodoDAO() {
		return estrategiaOperacionPeriodoDAO;
	}

	public void setEstrategiaOperacionPeriodoDAO(EstrategiaOperacionPeriodoDAO estrategiaOperacionPeriodoDAO) {
		this.estrategiaOperacionPeriodoDAO = estrategiaOperacionPeriodoDAO;
		this.inclusionesManager.setEstrategiaOperacionPeriodoDAO(estrategiaOperacionPeriodoDAO);
	}

}
package forex.genetic.manager;

import static forex.genetic.util.LogUtil.logAvance;
import static forex.genetic.util.LogUtil.logTime;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import forex.genetic.dao.oracle.OracleEstrategiaOperacionPeriodoDAO;
import forex.genetic.dao.oracle.OracleOperacionesDAO;
import forex.genetic.dao.oracle.OracleParametroDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.ParametroOperacionPeriodo;
import forex.genetic.entities.PipsAgrupado;
import forex.genetic.exception.GeneticDAOException;
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
	private OracleParametroDAO parametroDAO;
	private OracleOperacionesDAO operacionesDAO;
	private OracleEstrategiaOperacionPeriodoDAO estrategiaOperacionPeriodoDAO;
	private static final String[] ORDERS = { "NVL(PIPS_SEMANA,0)", "NVL(PIPS_MES,0)", "NVL(PIPS_ANYO,0)",
			"PIPS_TOTALES" };
	// Para filtrar las operaciones por la cantidad de órdenes que podría
	// obtener
	private static final int MESES_CONSULTA = 3;
	private Random random = new Random();

	private Date parametroFechaInicioProceso, parametroFechaFinProceso;
	private int parametroDiasProceso, parametroDiasRotacion;
	private String[] parametroTiposOperacion;
	private String[] vistas = { "FILTERED_PTFS", "FILTERED_EOP", "FILTERED_PARA_OPERAR_SELL",
			"FILTERED_PARA_OPERAR_BUY" };

	public IndividuosPeriodoManager() throws ClassNotFoundException, SQLException, GeneticDAOException {
		super();
		inclusionesManager = new InclusionesManager();
		conn = JDBCUtil.getConnection();
		parametroDAO = new OracleParametroDAO(conn);
		operacionesDAO = new OracleOperacionesDAO(conn);
		estrategiaOperacionPeriodoDAO = new OracleEstrategiaOperacionPeriodoDAO(conn);
		this.inclusionesManager.setEstrategiaOperacionPeriodoDAO(estrategiaOperacionPeriodoDAO);

		parametroFechaInicioProceso = parametroDAO.getDateValorParametro("FECHA_INDIVIDUO_PERIODO");
		parametroFechaFinProceso = parametroDAO.getDateValorParametro("FECHA_FIN_INDIVIDUO_PERIODO");
		parametroDiasProceso = parametroDAO.getIntValorParametro("DIAS_INDIVIDUO_PERIODO");
		parametroDiasRotacion = parametroDAO.getIntValorParametro("DIAS_ROTACION_INDIVIDUO_PERIODO");
		parametroTiposOperacion = parametroDAO.getArrayStringParametro("TIPOS_OPERACION");
	}

	public int procesarIndividuosXPeriodo() throws GeneticDAOException {
		int inclusionesAcumuladas = 0;
		int diasProceso = parametroDiasProceso;
		this.configurarAmbiente();
		while (diasProceso > 0) {
			logTime("Dias proceso: " + diasProceso, 1);
			Date fechaFinProceso = parametroFechaFinProceso;
			Date fechaInicioProceso = DateUtil.adicionarDias(fechaFinProceso, -diasProceso);
			while (fechaFinProceso.after(this.parametroFechaInicioProceso)) {
				logTime("Fecha Proceso:" + DateUtil.getDateString(fechaInicioProceso) + " - "
						+ DateUtil.getDateString(fechaFinProceso), 1);
				int contadorParametrosYaProcesados = 0;
				for (int i = 0; i < parametroTiposOperacion.length; i++) {
					List<ParametroOperacionPeriodo> inclusiones = inclusionesManager
							.consultarInclusiones(fechaInicioProceso, parametroTiposOperacion[i]);
					logTime("Inclusiones:" + inclusiones.size(), 1);
					for (ParametroOperacionPeriodo paramBase : inclusiones) {
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
						param.setTipoOperacion(Constants.getOperationType(parametroTiposOperacion[i]));
						if (param.isFiltroValido()) {
							logTime(param.toString(), 2);
							contadorParametrosYaProcesados += this.procesarIndividuosXPeriodo(param);
						} else {
							logAvance("F", 1);
						}
					}
					inclusionesAcumuladas += inclusiones.size();
				}
				this.consolidarEstrategias();
				logTime("Parametros ya procesados: " + contadorParametrosYaProcesados, 1);
				fechaFinProceso = DateUtil.adicionarDias(fechaFinProceso, -parametroDiasRotacion);
				fechaInicioProceso = DateUtil.adicionarDias(fechaFinProceso, -diasProceso);
			}
			diasProceso -= 7;
		}
		return inclusionesAcumuladas;
	}

	private void configurarAmbiente() throws GeneticDAOException {
		JDBCUtil.refreshMaterializedViews(conn, vistas);
	}

	private void consolidarEstrategias() throws GeneticDAOException {
		JDBCUtil.refreshMaterializedViews(conn, vistas);
	}

	public int procesarIndividuosXPeriodo(ParametroOperacionPeriodo param) throws GeneticDAOException {
		if (estrategiaOperacionPeriodoDAO.existe(param)) {
			logTime("Ya procesado:" + param.toString(), 3);
			logAvance(1);
			return 1;
		} else {
			operacionesDAO.cleanOperacionesPeriodo();
			logTime("Registros borrados.", 2);

			int insertados = operacionesDAO.insertOperacionesPeriodo(param);
			operacionesDAO.commit();
			logTime(param.getTipoOperacion() + ". Registro insertados TMP_TOFILESTRING: " + insertados, 1);

			if (insertados > 0) {
				procesarIndividuoYOperaciones(param, insertados);
			} else {
				logAvance("Z", 1);
			}

			return 0;
		}
	}

	private void procesarIndividuoYOperaciones(ParametroOperacionPeriodo param, int insertados) throws GeneticDAOException {
		List<Individuo> ordenesCreadas = this.ejecutarIndividuosXPeriodo(param, insertados);
		if (param.isResultadoValido()) {
			int id = estrategiaOperacionPeriodoDAO.insertParametroOperacionPeriodo(param);
			param.setId(id);
			//logEnter(1);
			logTime(param.getId() + ": " + param.getTipoOperacion() + ", Cantidad=" + param.getCantidad()
					+ ", Pips totales=" + param.getPipsTotales() + ", Pips paralelas=" + param.getPipsParalelas(), 1);
			for (Individuo ind : ordenesCreadas) {
				estrategiaOperacionPeriodoDAO.insertOperacionesPeriodo(param, ind, ind.getOrdenes());
			}
			estrategiaOperacionPeriodoDAO.commit();
			logTime(param.toString(), 2);
		} else {
			logAvance("C", 1);
		}
	}

	public List<Individuo> ejecutarIndividuosXPeriodo(ParametroOperacionPeriodo param, int insertados)
			throws GeneticDAOException {
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
		return ordenesCreadas;
	}

	public OracleEstrategiaOperacionPeriodoDAO getEstrategiaOperacionPeriodoDAO() {
		return estrategiaOperacionPeriodoDAO;
	}

	public void setEstrategiaOperacionPeriodoDAO(OracleEstrategiaOperacionPeriodoDAO estrategiaOperacionPeriodoDAO) {
		this.estrategiaOperacionPeriodoDAO = estrategiaOperacionPeriodoDAO;
		this.inclusionesManager.setEstrategiaOperacionPeriodoDAO(estrategiaOperacionPeriodoDAO);
	}

}
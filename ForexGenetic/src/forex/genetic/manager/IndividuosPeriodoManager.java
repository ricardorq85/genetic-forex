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
	private ParametroDAO parametroDAO;
	private OperacionesDAO operacionesDAO;
	private EstrategiaOperacionPeriodoDAO estrategiaOperacionPeriodoDAO;
	private static final String[] ORDERS = { "OPER_SEMANA.PIPS", "OPER_MES.PIPS", "OPER_ANYO.PIPS", "OPER.PIPS" };
	private static final Constants.OperationType[] TIPO_OPERACION = { Constants.OperationType.SELL,
			Constants.OperationType.BUY };
	private Random random = new Random();
	private static final int INCREMENTO_SEMANA = 500, INCREMENTO_MES = 800, INCREMENTO_ANYO = 1200,
			INCREMENTO_TOTALES = 1500;
	private static final int MINIMO_SEMANA = -1000, MINIMO_MES = -1000, MINIMO_ANYO = -2000, MINIMO_TOTALES = -3000;
	private static final int MAXIMO_SEMANA = 4000, MAXIMO_MES = 6000, MAXIMO_ANYO = 15000, MAXIMO_TOTALES = 20000;

	private Date parametroFechaInicioProceso, parametroFechaFinProceso;
	private int mesesProceso;

	public IndividuosPeriodoManager() throws ClassNotFoundException, SQLException {
		super();
		conn = JDBCUtil.getConnection();
		parametroDAO = new ParametroDAO(conn);
		operacionesDAO = new OperacionesDAO(conn);
		estrategiaOperacionPeriodoDAO = new EstrategiaOperacionPeriodoDAO(conn);

		parametroFechaInicioProceso = parametroDAO.getDateValorParametro("FECHA_INDIVIDUO_PERIODO");
		parametroFechaFinProceso = parametroDAO.getDateValorParametro("FECHA_FIN_INDIVIDUO_PERIODO");
		mesesProceso = parametroDAO.getIntValorParametro("MESES_INDIVIDUO_PERIODO");

		/*
		 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd"); try {
		 * fechaHistoricaMaxima = sdf.parse("2011/01/11"); } catch
		 * (ParseException e) { e.printStackTrace(); }
		 */
	}

	public void procesarIndividuosXPeriodo() throws SQLException {
		Date fechaFinProceso = parametroFechaFinProceso;
		Date fechaInicioProceso = DateUtil.adicionarMes(fechaFinProceso, -mesesProceso);
		while (fechaInicioProceso.after(this.parametroFechaInicioProceso)) {
			logTime("Fecha Inicio Proceso:" + DateUtil.getDateString(fechaInicioProceso) + "; Fecha Fin Proceso:"
					+ DateUtil.getDateString(fechaFinProceso), 1);			
			List<ParametroOperacionPeriodo> inclusiones = estrategiaOperacionPeriodoDAO.consultarInclusiones();
			for (ParametroOperacionPeriodo paramBase : inclusiones) {
				for (int i = 0; i < TIPO_OPERACION.length; i++) {
					ParametroOperacionPeriodo param = new ParametroOperacionPeriodo(paramBase.getFiltroPipsXSemana(),
							paramBase.getFiltroPipsXMes(), paramBase.getFiltroPipsXAnyo(),
							paramBase.getFiltroPipsTotales(), ORDERS[random.nextInt(ORDERS.length)],
							ORDERS[random.nextInt(ORDERS.length)]);
					param.setFechaInicial(fechaInicioProceso);
					param.setFechaFinal(fechaFinProceso);
					param.setTipoOperacion(TIPO_OPERACION[i]);
					if (param.isFiltroValido()) {
						logTime(param.toString(), 2);
						this.procesarIndividuosXPeriodo(param);
					} else {
						logTime("Filtros inválidos: " + param.toString(), 3);
					}
				}
			}
			fechaFinProceso = DateUtil.adicionarMes(fechaFinProceso, -1);
			fechaInicioProceso = DateUtil.adicionarMes(fechaFinProceso, -mesesProceso);
		}
	}

	public void procesarIndividuosXPeriodo(ParametroOperacionPeriodo param) throws SQLException {
		if (estrategiaOperacionPeriodoDAO.existe(param)) {
			logTime("Parametros ya procesados previamente", 2);
		} else {
			operacionesDAO.cleanOperacionesPeriodo();
			logTime("Registros borrados.", 2);

			int insertados = operacionesDAO.insertOperacionesPeriodo(param);
			conn.commit();
			logTime("Registro insertados: " + insertados, 1);

			List<Individuo> ordenesCreadas = this.ejecutarIndividuosXPeriodo(param);
			int id = estrategiaOperacionPeriodoDAO.insert(param);
			param.setId(id);

			for (Individuo ind : ordenesCreadas) {
				estrategiaOperacionPeriodoDAO.insertOperacionesPeriodo(param, ind, ind.getOrdenes());
			}
			conn.commit();
			logTime(param.toString(), 1);
		}
	}

	public List<Individuo> ejecutarIndividuosXPeriodo(ParametroOperacionPeriodo param) throws SQLException {
		Date fechaPeriodo = this.parametroFechaInicioProceso;
		List<Individuo> ordenesCreadas = new ArrayList<>();
		double pips = 0.0D;
		double pipsParalelas = 0.0D;
		int c = 0, cantidadParalelas = 0;
		PipsAgrupado agrupadoMinutos = new PipsAgrupado("yyyy/MM/dd HH:mm");
		PipsAgrupado agrupadoHoras = new PipsAgrupado("yyyy/MM/dd HH");
		PipsAgrupado agrupadoDias = new PipsAgrupado("yyyy/MM/dd");

		Date fechaInicial = fechaPeriodo;
		while (parametroFechaFinProceso.after(fechaInicial)) {
			Date fechaFinal = DateUtil.obtenerFechaMinima(parametroFechaFinProceso,
					DateUtil.adicionarMes(fechaInicial, 3));
			List<Individuo> ordenes = operacionesDAO.consultarOperacionesXPeriodo(fechaInicial, fechaFinal);
			for (Individuo individuo : ordenes) {
				Order order = individuo.getCurrentOrder();
				pipsParalelas += order.getPips();
				if (order.getOpenDate().after(fechaPeriodo)) {
					ordenesCreadas.add(individuo);
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
		logTime("Cantidad operaciones=" + c + ", Pips totales=" + pips + ", Pips paralelas=" + pipsParalelas, 2);
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
}
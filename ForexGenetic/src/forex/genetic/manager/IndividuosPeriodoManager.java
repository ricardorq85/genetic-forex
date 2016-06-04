/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
	private static final String[] ORDERS = { "OPER_MES.PIPS", "OPER_ANYO.PIPS", "OPER.PIPS" };
	private Random random = new Random();
	private static final int INCREMENTO_MES = 100, INCREMENTO_ANYO = 200, INCREMENTO_TOTALES = 500;
	private Date fechaInicioProceso;

	public IndividuosPeriodoManager() throws ClassNotFoundException, SQLException {
		super();
		conn = JDBCUtil.getConnection();
		parametroDAO = new ParametroDAO(conn);
		operacionesDAO = new OperacionesDAO(conn);
		estrategiaOperacionPeriodoDAO = new EstrategiaOperacionPeriodoDAO(conn);

		fechaInicioProceso = parametroDAO.getDateValorParametro("FECHA_INDIVIDUO_PERIODO");
	}

	public List<Individuo> ejecutarIndividuosXPeriodo(ParametroOperacionPeriodo param) throws SQLException {
		Date fechaPeriodo = this.fechaInicioProceso;
		List<Individuo> ordenes = operacionesDAO.consultarOperacionesXPeriodo(fechaPeriodo);
		List<Individuo> ordenesCreadas = new ArrayList<>();

		double pips = 0.0D;
		double pipsParalelas = 0.0D;
		int c = 0;
		for (Individuo individuo : ordenes) {
			Order order = individuo.getCurrentOrder();
			pipsParalelas += order.getPips();
			if (order.getOpenDate().after(fechaPeriodo)) {
				ordenesCreadas.add(individuo);
				logTime("Individuo=" + individuo.getId() + ",Orden=" + order, 2);
				pips += order.getPips();
				fechaPeriodo = order.getCloseDate();
				logTime("Fecha=" + DateUtil.getDateString(fechaPeriodo) + ",Pips=" + pips, 1);
				c++;
			}
		}
		param.setFechaFinal(fechaPeriodo);
		param.setPipsTotales(pips);
		param.setCantidad(c);
		param.setPipsParalelas(pipsParalelas);
		param.setCantidadParalelas(ordenes.size());
		logTime("Cantidad operaciones=" + c + ", Pips totales=" + pips + ", Pips paralelas=" + pipsParalelas, 1);
		return ordenesCreadas;
	}

	public void procesarIndividuosXPeriodo() throws SQLException {
		ParametroOperacionPeriodo paramBase = estrategiaOperacionPeriodoDAO
				.consultarUltimaEjecucion(this.fechaInicioProceso);
		int filtroPipsXMes = (paramBase == null) ? 0 : (paramBase.getFiltroPipsXMes());
		while (filtroPipsXMes < 5000) {
			int filtroPipsXAnyo = (paramBase == null) ? 0 : (paramBase.getFiltroPipsXAnyo());
			while (filtroPipsXAnyo < 10000) {
				int filtroPipsTotales = (paramBase == null) ? 0
						: (paramBase.getFiltroPipsTotales() + INCREMENTO_TOTALES);
				while (filtroPipsTotales < 10000) {
					ParametroOperacionPeriodo param = new ParametroOperacionPeriodo(filtroPipsXMes, filtroPipsXAnyo,
							filtroPipsTotales, ORDERS[random.nextInt(ORDERS.length)],
							ORDERS[random.nextInt(ORDERS.length)]);
					param.setFechaInicial(this.fechaInicioProceso);
					logTime(param.toString(), 1);
					this.procesarIndividuosXPeriodo(param);
					paramBase = null;
					filtroPipsTotales += INCREMENTO_TOTALES;
				}
				filtroPipsXAnyo += INCREMENTO_ANYO;
			}
			filtroPipsXMes += INCREMENTO_MES;
		}
	}

	private int getNextOrderIndex(ParametroOperacionPeriodo param) {
		if (param == null) {
			return 0;
		}
		int i = 0;
		for (; i < ORDERS.length; i++) {
			if (ORDERS[i].equals(param.getFirstOrder())) {
				break;
			}
		}

		return (i + 1);
	}

	public void procesarIndividuosXPeriodo(ParametroOperacionPeriodo param) throws SQLException {
		int borrados = operacionesDAO.cleanOperacionesPeriodo();
		conn.commit();
		logTime("Registro borrados: " + borrados, 1);

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
	}
}

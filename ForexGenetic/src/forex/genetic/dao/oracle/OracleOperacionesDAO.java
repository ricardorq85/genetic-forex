/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.IOperacionesDAO;
import forex.genetic.dao.helper.OperacionHelper;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.Estadistica;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.ParametroConsultaEstadistica;
import forex.genetic.entities.ParametroOperacionPeriodo;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class OracleOperacionesDAO extends OracleGeneticDAO<Order> implements IOperacionesDAO<Order> {

	public OracleOperacionesDAO(Connection connection) {
		super(connection);
	}

	public List<DateInterval> consultarVigencias(Date fechaPeriodo) throws GeneticDAOException {
		List<DateInterval> vigencias;
		String sql = "SELECT DISTINCT VIGENCIA1, VIGENCIA2 FROM TMP_TOFILESTRING " + " WHERE VIGENCIA1>? "
				+ " ORDER BY VIGENCIA1 ASC, VIGENCIA2 ASC ";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fechaPeriodo.getTime()));
			resultado = stmtConsulta.executeQuery();

			vigencias = OperacionHelper.vigencias(resultado);

		} catch (SQLException e) {
			throw new GeneticDAOException("Error OracleOperacionesDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return vigencias;
	}

	public List<Individuo> consultarOperacionesXPeriodo(Date fechaInicial, Date fechaFinal) throws GeneticDAOException {
		List<Individuo> ordenes;
		String sql = "SELECT OPER.* FROM OPERACION OPER "
				+ " INNER JOIN TMP_TOFILESTRING TFS ON TFS.ID_INDIVIDUO=OPER.ID_INDIVIDUO "
				+ " AND OPER.FECHA_APERTURA BETWEEN VIGENCIA1 AND VIGENCIA2 "
				+ " WHERE OPER.FECHA_APERTURA>? AND OPER.FECHA_APERTURA<=? AND OPER.FECHA_CIERRE IS NOT NULL "
				+ " ORDER BY OPER.FECHA_APERTURA ASC, " + " TFS.CRITERIO_ORDER1 DESC, " + " TFS.CRITERIO_ORDER2 DESC, "
				+ " TRUNC(TFS.FECHA_ORDER1) DESC, " + " TRUNC(TFS.FECHA_ORDER2) DESC ";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fechaInicial.getTime()));
			stmtConsulta.setTimestamp(2, new Timestamp(fechaFinal.getTime()));
			resultado = stmtConsulta.executeQuery();

			ordenes = OperacionHelper.operaciones(resultado);

		} catch (SQLException e) {
			throw new GeneticDAOException("Error OracleOperacionesDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return ordenes;
	}

	public double consultarPipsXAgrupacion(String agrupador) throws GeneticDAOException {
		double pips = 0;
		String sql = " SELECT SUM(PIPS) PIPS FROM ( " + " SELECT SUM(OPER.PIPS)/COUNT(*) PIPS FROM OPERACION OPER "
				+ " INNER JOIN TMP_TOFILESTRING TFS ON TFS.ID_INDIVIDUO=OPER.ID_INDIVIDUO "
				+ " AND OPER.FECHA_APERTURA BETWEEN VIGENCIA1 AND VIGENCIA2 " + " GROUP BY TO_CHAR(FECHA_APERTURA, '"
				+ agrupador + "'))";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			resultado = stmtConsulta.executeQuery();

			if (resultado.next()) {
				pips = resultado.getDouble("PIPS");
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("Error OracleOperacionesDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return pips;
	}

	/**
	 *
	 * @param individuo
	 * @return
	 * @throws GeneticDAOException
	 */
	@Override
	public Estadistica consultarEstadisticas(Individuo individuo, ParametroConsultaEstadistica parametroConsultaEstadistica) throws GeneticDAOException {
		Estadistica estadistica = null;
		String sql = "SELECT * FROM DETALLE_ESTADISTICAS WHERE ID_INDIVIDUO=?";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, individuo.getId());
			resultado = stmtConsulta.executeQuery();

			estadistica = OperacionHelper.createEstadistica(resultado);

		} catch (SQLException e) {
			throw new GeneticDAOException("Error OracleOperacionesDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return estadistica;
	}

	/**
	 *
	 * @param fechaBase
	 * @return
	 * @throws GeneticDAOException
	 */
	public List<Individuo> consultarIndividuoOperacionActiva(Date fechaBase) throws GeneticDAOException {
		return this.consultarIndividuoOperacionActiva(fechaBase, 500);
	}

	/**
	 *
	 * @param fechaBase
	 * @param filas
	 * @return
	 * @throws GeneticDAOException
	 */
	public List<Individuo> consultarIndividuoOperacionActiva(Date fechaBase, int filas) throws GeneticDAOException {
		return this.consultarIndividuoOperacionActiva(fechaBase, null, filas);
	}

	/**
	 *
	 * @param fechaBase
	 * @param fechaFin
	 * @param filas
	 * @return
	 * @throws GeneticDAOException
	 */
	public List<Individuo> consultarIndividuoOperacionActiva(Date fechaBase, Date fechaFin, int filas)
			throws GeneticDAOException {
		List<Individuo> list = null;
		String sql = "SELECT * FROM ( " + " SELECT OPER.ID_INDIVIDUO, OPER.FECHA_APERTURA, OPER.FECHA_CIERRE, "
				+ " OPER.OPEN_PRICE, OPER.SPREAD, OPER.LOTE, OPER.PIPS, OPER.TIPO " + " FROM OPERACION OPER "
				+ "     INNER JOIN PROCESO PROC ON PROC.ID_INDIVIDUO=OPER.ID_INDIVIDUO "
				+ "     AND (PROC.FECHA_HISTORICO>=?) "
				+ " WHERE OPER.FECHA_APERTURA<? AND (OPER.FECHA_CIERRE IS NULL OR OPER.FECHA_CIERRE>?)"
				+ " AND OPER.FECHA_APERTURA>?-30"
				// + " AND OPER.TIPO = 'SELL'"
				+ " AND OPER.ID_INDIVIDUO NOT IN (SELECT ID_INDIVIDUO FROM TENDENCIA TEND WHERE TEND.ID_INDIVIDUO=OPER.ID_INDIVIDUO "
				// + " AND OPER.ID_INDIVIDUO IN (SELECT ID_INDIVIDUO FROM
				// TENDENCIA TEND WHERE TEND.ID_INDIVIDUO=OPER.ID_INDIVIDUO "
				+ "   AND TEND.FECHA_BASE=? AND TEND.TIPO_TENDENCIA=?) " + " AND EXISTS (SELECT 1 FROM OPERACION OPER2 "
				+ "     WHERE OPER2.ID_INDIVIDUO=OPER.ID_INDIVIDUO "
				+ "     AND OPER2.FECHA_CIERRE < OPER.FECHA_APERTURA) "
				+ " AND EXISTS (SELECT 1 FROM INDICADOR_INDIVIDUO II "
				+ "     WHERE II.ID_INDIVIDUO=OPER.ID_INDIVIDUO) "
				// + " AND OPER.ID_INDIVIDUO='1341548450906.88346'"
				+ " ORDER BY OPER.FECHA_APERTURA DESC) " + " WHERE ROWNUM < ? ";
		/*
		 * sql =
		 * "SELECT OPER.ID_INDIVIDUO, OPER.FECHA_APERTURA, OPER.FECHA_CIERRE, OPER.OPEN_PRICE, OPER.SPREAD, OPER.LOTE, OPER.PIPS, OPER.TIPO "
		 * + " FROM OPERACION OPER" + " WHERE " + "  SYSDATE>=? " +
		 * " AND OPER.FECHA_APERTURA<? AND (OPER.FECHA_CIERRE IS NULL OR OPER.FECHA_CIERRE>?)"
		 * + " AND OPER.FECHA_APERTURA>?-30" //* + " AND OPER.TIPO = 'SELL'" +
		 * " AND (OPER.ID_INDIVIDUO NOT IN (SELECT ID_INDIVIDUO FROM TENDENCIA TEND WHERE TEND.ID_INDIVIDUO=OPER.ID_INDIVIDUO"
		 * + " AND TEND.FECHA_BASE=? AND (TIPO_TENDENCIA=? OR 1=1)) " + " OR 1=1)" +
		 * " AND OPER.ID_INDIVIDUO='1341461434490.61685'" + " AND ROWNUM < ?";
		 */

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fechaBase.getTime()));
			stmtConsulta.setTimestamp(2, new Timestamp(fechaBase.getTime()));
			if (fechaFin != null) {
				stmtConsulta.setTimestamp(3, new Timestamp(fechaFin.getTime()));
			} else {
				stmtConsulta.setTimestamp(3, new Timestamp(fechaBase.getTime()));
			}
			stmtConsulta.setTimestamp(4, new Timestamp(fechaBase.getTime()));
			stmtConsulta.setTimestamp(5, new Timestamp(fechaBase.getTime()));
			stmtConsulta.setString(6, Constants.TIPO_TENDENCIA);
			stmtConsulta.setInt(7, filas);
			resultado = stmtConsulta.executeQuery();

			list = OperacionHelper.individuosOperacionActiva(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("Error OracleOperacionesDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	/**
	 *
	 * @param idIndividuo
	 * @return
	 * @throws GeneticDAOException
	 */
	public int deleteOperaciones(String idIndividuo) throws GeneticDAOException {
		String sql = "DELETE FROM OPERACION WHERE ID_INDIVIDUO=?";
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, idIndividuo);
			return stmtConsulta.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error OracleOperacionesDAO", e);
		} finally {
			JDBCUtil.close(stmtConsulta);
		}
	}

	/**
	 *
	 * @param individuo
	 * @param operacion
	 * @param fechaApertura
	 * @throws GeneticDAOException
	 */
	public void update(Individuo individuo, Order operacion, Date fechaApertura) throws GeneticDAOException {
		String sql = "UPDATE OPERACION SET FECHA_CIERRE=?, PIPS=?, FECHA=? "
				+ " WHERE ID_INDIVIDUO=? AND FECHA_APERTURA=?";

		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setTimestamp(1, new Timestamp(operacion.getCloseDate().getTime()));
			statement.setDouble(2, operacion.getPips());
			statement.setTimestamp(3, new Timestamp(new java.util.Date().getTime()));
			statement.setString(4, individuo.getId());
			statement.setTimestamp(5, new Timestamp(operacion.getOpenDate().getTime()));
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error OracleOperacionesDAO", e);
		} finally {
			JDBCUtil.close(statement);
		}
	}

	/**
	 *
	 * @param individuo
	 * @param operacion
	 * @throws GeneticDAOException
	 */
	public void updateMaximosRetrocesoOperacion(Individuo individuo, Order operacion) throws GeneticDAOException {
		String sql = "UPDATE OPERACION SET MAX_PIPS_RETROCESO=?, MAX_VALUE_RETROCESO=?, MAX_FECHA_RETROCESO=?, FECHA=? "
				+ " WHERE ID_INDIVIDUO=? AND FECHA_APERTURA=?";

		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setDouble(1, operacion.getMaxPipsRetroceso());
			statement.setDouble(2, operacion.getMaxValueRetroceso());
			if (operacion.getMaxFechaRetroceso() != null) {
				statement.setTimestamp(3, new Timestamp(operacion.getMaxFechaRetroceso().getTime()));
			} else {
				statement.setNull(3, java.sql.Types.DATE);
			}
			statement.setTimestamp(4, new Timestamp(new java.util.Date().getTime()));
			statement.setString(5, individuo.getId());
			statement.setTimestamp(6, new Timestamp(operacion.getOpenDate().getTime()));
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error OracleOperacionesDAO", e);
		} finally {
			JDBCUtil.close(statement);
		}
	}

	/**
	 *
	 * @param individuo
	 * @param operaciones
	 * @throws GeneticDAOException
	 */
	public void insert(Individuo individuo, List<Order> operaciones) throws GeneticDAOException {
		String sql = "INSERT INTO OPERACION(ID_INDIVIDUO, TAKE_PROFIT, STOP_LOSS, "
				+ " FECHA_APERTURA, FECHA_CIERRE, SPREAD, OPEN_PRICE, PIPS, LOTE, TIPO, FECHA) "
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

		for (Order operacion : operaciones) {
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement(sql);
				Order order = operacion;
				statement.setString(1, individuo.getId());
				statement.setDouble(2, individuo.getTakeProfit());
				statement.setDouble(3, individuo.getStopLoss());
				statement.setTimestamp(4, new Timestamp(order.getOpenDate().getTime()));
				if (order.getCloseDate() != null) {
					statement.setTimestamp(5, new Timestamp(order.getCloseDate().getTime()));
				} else {
					statement.setNull(5, java.sql.Types.DATE);
				}
				statement.setDouble(6, order.getOpenSpread());
				statement.setDouble(7, order.getOpenOperationValue());
				statement.setDouble(8, order.getPips());
				statement.setDouble(9, order.getLot());
				statement.setString(10, order.getTipo().name());
				statement.setTimestamp(11, new Timestamp(new java.util.Date().getTime()));
				statement.executeUpdate();
			} catch (SQLException e) {
				throw new GeneticDAOException("Error OracleOperacionesDAO", e);
			} finally {
				JDBCUtil.close(statement);
			}
		}
	}

	/**
	 *
	 * @param ind
	 * @param fechaMaximo
	 * @return
	 * @throws GeneticDAOException
	 */
	public Individuo consultarOperacionesIndividuoRetroceso(Individuo ind, Date fechaMaximo)
			throws GeneticDAOException {
		List<Order> list = null;
		String sql = "SELECT OPER.ID_INDIVIDUO, OPER.TAKE_PROFIT, OPER.STOP_LOSS, "
				+ " OPER.FECHA_APERTURA, OPER.FECHA_CIERRE, OPER.SPREAD, OPER.OPEN_PRICE, OPER.LOTE, OPER.PIPS, "
				+ " OPER.TIPO, OPER.MAX_PIPS_RETROCESO, OPER.MAX_VALUE_RETROCESO, OPER.MAX_FECHA_RETROCESO "
				+ " FROM OPERACION OPER " + " WHERE "
				// + " OPER.TIPO='SELL' AND "
				+ " ID_INDIVIDUO=? AND FECHA_APERTURA<=? AND MAX_PIPS_RETROCESO IS NULL";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, ind.getId());
			stmtConsulta.setTimestamp(2, new Timestamp(fechaMaximo.getTime()));
			resultado = stmtConsulta.executeQuery();

			list = OperacionHelper.operacionesIndividuo(resultado);

		} catch (SQLException e) {
			throw new GeneticDAOException("Error OracleOperacionesDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		ind.setOrdenes(list);
		return ind;
	}

	/**
	 *
	 * @param fechaMaximo
	 * @return
	 * @throws GeneticDAOException
	 */
	public List<Individuo> consultarOperacionesIndividuoRetroceso(Date fechaMaximo) throws GeneticDAOException {
		List<Individuo> list = null;
		String sql = "SELECT OPER.ID_INDIVIDUO, OPER.TAKE_PROFIT, OPER.STOP_LOSS, "
				+ " OPER.FECHA_APERTURA, OPER.FECHA_CIERRE, OPER.SPREAD, OPER.OPEN_PRICE, OPER.LOTE, OPER.PIPS, "
				+ " OPER.TIPO, OPER.MAX_PIPS_RETROCESO, OPER.MAX_VALUE_RETROCESO, OPER.MAX_FECHA_RETROCESO "
				+ " FROM OPERACION OPER "
				+ " WHERE OPER.TIPO='BUY' AND FECHA_APERTURA<=? AND MAX_PIPS_RETROCESO IS NULL AND ROWNUM<1000";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fechaMaximo.getTime()));
			resultado = stmtConsulta.executeQuery();

			list = OperacionHelper.operaciones(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException("Error OracleOperacionesDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public int cleanOperacionesPeriodo() throws GeneticDAOException {
		String sql = "TRUNCATE TABLE TMP_TOFILESTRING";
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			return stmtConsulta.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error OracleOperacionesDAO", e);
		} finally {
			JDBCUtil.close(stmtConsulta);
		}
	}

	private String temporal(ParametroOperacionPeriodo param) throws GeneticDAOException {
		String sql = " SELECT PRE.ID_INDIVIDUO, SUM(" + param.getFirstOrder() + "), " + " SUM(" + param.getSecondOrder()
				+ "), " + "  PRE.FECHA_SEMANA, PRE.FECHA_SEMANA+7 " + " FROM PREVIO_TOFILESTRING PRE "
				+ " WHERE PRE.TIPO_OPERACION='" + param.getTipoOperacion().name() + "'"
				+ " AND (NVL(PRE.PIPS_SEMANA,0)> " + param.getFiltroPipsXSemana() + " AND PRE.PIPS_MES> "
				+ param.getFiltroPipsXMes() + " AND PRE.PIPS_ANYO> " + param.getFiltroPipsXAnyo()
				+ " AND PRE.PIPS_TOTALES>" + param.getFiltroPipsTotales() + " ) " + " AND (NVL(PRE.R2_SEMANA,0)> "
				+ param.getFiltroR2Semana() + " AND PRE.R2_MES> " + param.getFiltroR2Mes() + " AND PRE.R2_ANYO>"
				+ param.getFiltroR2Anyo() + " AND PRE.R2_CONSOL>" + param.getFiltroR2Totales() + " ) "
				+ " AND (NVL(PRE.PENDIENTE_SEMANA,0)>" + param.getFiltroPendienteSemana() + " AND PRE.PENDIENTE_MES>"
				+ param.getFiltroPendienteMes() + " AND PRE.PENDIENTE_ANYO>" + param.getFiltroPendienteAnyo()
				+ " AND PRE.PENDIENTE_CONSOL>" + param.getFiltroPendienteTotales() + ") "
				+ " AND PRE.FECHA_SEMANA > TO_DATE('" + DateUtil.getDateString(param.getFechaInicial())
				+ "','YYYY/MM/DD HH24:MI.SS')" + " AND PRE.FECHA_SEMANA+7 <= TO_DATE('"
				+ DateUtil.getDateString(param.getFechaFinal()) + "','YYYY/MM/DD HH24:MI.SS')"
				+ " GROUP BY PRE.ID_INDIVIDUO, PRE.FECHA_SEMANA;";
		return sql;
	}

	public int insertOperacionesPeriodo(ParametroOperacionPeriodo param) throws GeneticDAOException {
		String sql = "INSERT INTO TMP_TOFILESTRING (ID_INDIVIDUO, CRITERIO_ORDER1, CRITERIO_ORDER2, VIGENCIA1, VIGENCIA2, FECHA_ORDER1, FECHA_ORDER2) "
				+ " SELECT PRE.ID_INDIVIDUO, SUM(" + param.getFirstOrder() + "), " + " SUM(" + param.getSecondOrder()
				+ "), " + "  PRE.FECHA_SEMANA, PRE.FECHA_SEMANA+7, ?, ? " + " FROM PREVIO_TOFILESTRING PRE "
				+ " WHERE PRE.TIPO_OPERACION=?"
				+ " AND (NVL(PRE.PIPS_SEMANA,0)>? AND NVL(PRE.PIPS_MES,0)>? AND NVL(PRE.PIPS_ANYO,0)>? AND PRE.PIPS_TOTALES>?) "
				+ " AND (NVL(PRE.R2_SEMANA,0)>? AND NVL(PRE.R2_MES,0)>? AND NVL(PRE.R2_ANYO,0)>? AND PRE.R2_CONSOL>?) "
				+ " AND (NVL(PRE.PENDIENTE_SEMANA,0)>? AND NVL(PRE.PENDIENTE_MES,0)>? AND NVL(PRE.PENDIENTE_ANYO,0)>? AND PRE.PENDIENTE_CONSOL>?) "
				+ " AND PRE.FECHA_SEMANA > ? AND PRE.FECHA_SEMANA <= ? "
				+ " GROUP BY PRE.ID_INDIVIDUO, PRE.FECHA_SEMANA";
		PreparedStatement stmtConsulta = null;
		try {
			// String t = temporal(param);
			int index = 0;
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setDate(++index, new java.sql.Date(param.getFecha().getTime()));
			stmtConsulta.setDate(++index, new java.sql.Date(param.getFechaFinal().getTime()));

			stmtConsulta.setString(++index, param.getTipoOperacion().name());
			stmtConsulta.setInt(++index, param.getFiltroPipsXSemana());
			stmtConsulta.setInt(++index, param.getFiltroPipsXMes());
			stmtConsulta.setInt(++index, param.getFiltroPipsXAnyo());
			stmtConsulta.setInt(++index, param.getFiltroPipsTotales());

			stmtConsulta.setDouble(++index, param.getFiltroR2Semana());
			stmtConsulta.setDouble(++index, param.getFiltroR2Mes());
			stmtConsulta.setDouble(++index, param.getFiltroR2Anyo());
			stmtConsulta.setDouble(++index, param.getFiltroR2Totales());

			stmtConsulta.setDouble(++index, param.getFiltroPendienteSemana());
			stmtConsulta.setDouble(++index, param.getFiltroPendienteMes());
			stmtConsulta.setDouble(++index, param.getFiltroPendienteAnyo());
			stmtConsulta.setDouble(++index, param.getFiltroPendienteTotales());

			stmtConsulta.setDate(++index, new java.sql.Date(param.getFechaInicial().getTime()));
			stmtConsulta.setDate(++index, new java.sql.Date(param.getFechaFinal().getTime()));

			return stmtConsulta.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error OracleOperacionesDAO", e);
		} finally {
			JDBCUtil.close(stmtConsulta);
		}
	}

	public void actualizarOperacionesPositivasYNegativas() throws GeneticDAOException {
		JDBCUtil.refreshMaterializedView(this.connection, "OPERACION_POSITIVAS", "C");
		JDBCUtil.refreshMaterializedView(this.connection, "OPERACION_NEGATIVAS", "C");
		// this.dropVistaMaterializada("OPERACION_POSITIVAS");
		// this.dropVistaMaterializada("OPERACION_NEGATIVAS");
	}

	private void actualizarOperacionesNegativas() throws GeneticDAOException {
		this.crearVistaMaterializadaNegativas();
	}

	private void actualizarOperacionesPositivas() throws GeneticDAOException {
		this.crearVistaMaterializadaPositivas();
	}

	private void crearVistaMaterializadaPositivas() throws GeneticDAOException {
		String sql = "CREATE MATERIALIZED VIEW OPERACION_POSITIVAS AS "
				+ " SELECT OPER.TAKE_PROFIT, OPER.STOP_LOSS, OPER.FECHA_APERTURA, OPER.PIPS, OPER.MAX_PIPS_RETROCESO, OPER.OPEN_PRICE"
				+ " FROM OPERACION OPER" + "  WHERE OPER.PIPS>=200 AND OPER.FECHA_CIERRE IS NOT NULL"
				+ "  AND OPER.FECHA_APERTURA BETWEEN TO_DATE('20170101', 'YYYYMMDD') AND TO_DATE('21000101', 'YYYYMMDD')";
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error OracleOperacionesDAO", e);
		} finally {
			JDBCUtil.close(stmtConsulta);
		}
	}

	private void crearVistaMaterializadaNegativas() throws GeneticDAOException {
		String sql = "CREATE MATERIALIZED VIEW OPERACION_NEGATIVAS AS "
				+ " SELECT OPER.TAKE_PROFIT, OPER.STOP_LOSS, OPER.FECHA_APERTURA, OPER.PIPS, OPER.MAX_PIPS_RETROCESO, OPER.OPEN_PRICE "
				+ " FROM OPERACION OPER " + "  WHERE OPER.PIPS<=-200 AND OPER.FECHA_CIERRE IS NOT NULL "
				+ " AND OPER.FECHA_APERTURA BETWEEN TO_DATE('20170101', 'YYYYMMDD') AND TO_DATE('21000101', 'YYYYMMDD')";
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error OracleOperacionesDAO", e);
		} finally {
			JDBCUtil.close(stmtConsulta);
		}
	}
	
	public long duracionPromedioMinutos(String idIndividuo) throws GeneticDAOException {
		String sql = "SELECT ROUND(AVG(OPER.FECHA_CIERRE-OPER.FECHA_APERTURA)*24*60) DURACION FROM OPERACION OPER\n"
				+ " WHERE ID_INDIVIDUO = ? ";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		long duracion = 0;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, idIndividuo);
			resultado = stmtConsulta.executeQuery();
			if (resultado.next()) {
				if (resultado.getObject("DURACION") != null) {
					duracion = resultado.getLong("DURACION");
				}
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("OracleIndividuoDAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return duracion;
	}
	
	private void dropVistaMaterializada(String name) throws GeneticDAOException {
		String sql = "DROP MATERIALIZED VIEW " + name;
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error OracleOperacionesDAO", e);
		} finally {
			JDBCUtil.close(stmtConsulta);
		}
	}

	@Override
	public boolean exists(Order obj) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void insert(Order obj) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void update(Order obj) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void insertIfNoExists(Order obj) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Order> consultarOperacionesActivas(Date fechaBase, Date fechaFin, int filas)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}
}

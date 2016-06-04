/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.helper.OperacionHelper;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.Estadistica;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.ParametroOperacionPeriodo;
import forex.genetic.util.Constants;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class OperacionesDAO {

	protected Connection connection = null;

	public OperacionesDAO(Connection connection) {
		this.connection = connection;
	}

	public List<DateInterval> consultarVigencias(Date fechaPeriodo) throws SQLException {
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

		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return vigencias;
	}

	public List<Individuo> consultarOperacionesXPeriodo(Date fechaPeriodo) throws SQLException {
		List<Individuo> ordenes;
		String sql = "SELECT OPER.* FROM OPERACION OPER "
				+ " INNER JOIN TMP_TOFILESTRING TFS ON TFS.ID_INDIVIDUO=OPER.ID_INDIVIDUO "
				+ " AND OPER.FECHA_APERTURA BETWEEN VIGENCIA1 AND VIGENCIA2 "
				+ " WHERE OPER.FECHA_APERTURA>? AND OPER.FECHA_CIERRE IS NOT NULL "
				+ " ORDER BY OPER.FECHA_APERTURA ASC, TFS.CRITERIO_ORDER1 DESC, TFS.CRITERIO_ORDER2 DESC";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fechaPeriodo.getTime()));
			resultado = stmtConsulta.executeQuery();

			ordenes = OperacionHelper.operaciones(resultado);

		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return ordenes;
	}
	
	public double consultarPipsXAgrupacion(String agrupador) throws SQLException {
		double pips = 0;
		String sql = " SELECT SUM(PIPS) PIPS FROM ( " +
			  " SELECT SUM(OPER.PIPS)/COUNT(*) PIPS FROM OPERACION OPER "+ 
			  " INNER JOIN TMP_TOFILESTRING TFS ON TFS.ID_INDIVIDUO=OPER.ID_INDIVIDUO "+ 
			  " AND OPER.FECHA_APERTURA BETWEEN VIGENCIA1 AND VIGENCIA2 "+
			  " GROUP BY TO_CHAR(FECHA_APERTURA, '"+ agrupador + "'))";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			resultado = stmtConsulta.executeQuery();

			if (resultado.next()) {
				pips = resultado.getDouble("PIPS");
			}
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
	 * @throws SQLException
	 */
	public Estadistica consultarEstadisticasIndividuo(Individuo individuo) throws SQLException {
		Estadistica estadistica = null;
		String sql = "SELECT * FROM DETALLE_ESTADISTICAS WHERE ID_INDIVIDUO=?";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, individuo.getId());
			resultado = stmtConsulta.executeQuery();

			estadistica = OperacionHelper.createEstadistica(resultado);

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
	 * @throws SQLException
	 */
	public List<Individuo> consultarIndividuoOperacionActiva(Date fechaBase) throws SQLException {
		return this.consultarIndividuoOperacionActiva(fechaBase, 500);
	}

	/**
	 *
	 * @param fechaBase
	 * @param filas
	 * @return
	 * @throws SQLException
	 */
	public List<Individuo> consultarIndividuoOperacionActiva(Date fechaBase, int filas) throws SQLException {
		return this.consultarIndividuoOperacionActiva(fechaBase, null, filas);
	}

	/**
	 *
	 * @param fechaBase
	 * @param fechaFin
	 * @param filas
	 * @return
	 * @throws SQLException
	 */
	public List<Individuo> consultarIndividuoOperacionActiva(Date fechaBase, Date fechaFin, int filas)
			throws SQLException {
		List<Individuo> list = null;
		String sql = "SELECT * FROM ( " + " SELECT OPER.ID_INDIVIDUO, OPER.FECHA_APERTURA, OPER.FECHA_CIERRE, "
				+ " OPER.OPEN_PRICE, OPER.SPREAD, OPER.LOTE, OPER.PIPS, OPER.TIPO " + " FROM OPERACION OPER "
				+ "     INNER JOIN PROCESO PROC ON PROC.ID_INDIVIDUO=OPER.ID_INDIVIDUO "
				+ "     AND (PROC.FECHA_HISTORICO>=?) "
				+ " WHERE OPER.FECHA_APERTURA<? AND (OPER.FECHA_CIERRE IS NULL OR OPER.FECHA_CIERRE>?)"
				+ " AND OPER.FECHA_APERTURA>?-30" + " AND OPER.TIPO = 'SELL'"
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
		 * + " AND OPER.FECHA_APERTURA>?-30" + " AND OPER.TIPO = 'SELL'" +
		 * " AND (OPER.ID_INDIVIDUO NOT IN (SELECT ID_INDIVIDUO FROM TENDENCIA TEND WHERE TEND.ID_INDIVIDUO=OPER.ID_INDIVIDUO"
		 * + " AND TEND.FECHA_BASE=? AND (TIPO_TENDENCIA=? OR 1=1)) " +
		 * " OR 1=1)" + " AND OPER.ID_INDIVIDUO='1341461434490.61685'" +
		 * " AND ROWNUM < ?";
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
	 * @throws SQLException
	 */
	public int deleteOperaciones(String idIndividuo) throws SQLException {
		String sql = "DELETE FROM OPERACION WHERE ID_INDIVIDUO=?";
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, idIndividuo);
			return stmtConsulta.executeUpdate();
		} finally {
			JDBCUtil.close(stmtConsulta);
		}
	}

	/**
	 *
	 * @param individuo
	 * @param operacion
	 * @param fechaApertura
	 * @throws SQLException
	 */
	public void updateOperacion(Individuo individuo, Order operacion, Date fechaApertura) throws SQLException {
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
		} finally {
			JDBCUtil.close(statement);
		}
	}

	/**
	 *
	 * @param individuo
	 * @param operacion
	 * @throws SQLException
	 */
	public void updateMaximosReprocesoOperacion(Individuo individuo, Order operacion) throws SQLException {
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
		} finally {
			JDBCUtil.close(statement);
		}
	}

	/**
	 *
	 * @param individuo
	 * @param operaciones
	 * @throws SQLException
	 */
	public void insertOperaciones(Individuo individuo, List<Order> operaciones) throws SQLException {
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
	 * @throws SQLException
	 */
	public Individuo consultarOperacionesIndividuoRetroceso(Individuo ind, Date fechaMaximo) throws SQLException {
		List<Order> list = null;
		String sql = "SELECT OPER.ID_INDIVIDUO, OPER.TAKE_PROFIT, OPER.STOP_LOSS, "
				+ " OPER.FECHA_APERTURA, OPER.FECHA_CIERRE, OPER.SPREAD, OPER.OPEN_PRICE, OPER.LOTE, OPER.PIPS, "
				+ " OPER.TIPO, OPER.MAX_PIPS_RETROCESO, OPER.MAX_VALUE_RETROCESO, OPER.MAX_FECHA_RETROCESO "
				+ " FROM OPERACION OPER "
				+ " WHERE OPER.TIPO='SELL' AND ID_INDIVIDUO=? AND FECHA_APERTURA<=? AND MAX_PIPS_RETROCESO IS NULL";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, ind.getId());
			stmtConsulta.setTimestamp(2, new Timestamp(fechaMaximo.getTime()));
			resultado = stmtConsulta.executeQuery();

			list = OperacionHelper.operacionesIndividuo(resultado);

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
	 * @throws SQLException
	 */
	public List<Individuo> consultarOperacionesIndividuoRetroceso(Date fechaMaximo) throws SQLException {
		List<Individuo> list = null;
		String sql = "SELECT OPER.ID_INDIVIDUO, OPER.TAKE_PROFIT, OPER.STOP_LOSS, "
				+ " OPER.FECHA_APERTURA, OPER.FECHA_CIERRE, OPER.SPREAD, OPER.OPEN_PRICE, OPER.LOTE, OPER.PIPS, "
				+ " OPER.TIPO, OPER.MAX_PIPS_RETROCESO, OPER.MAX_VALUE_RETROCESO, OPER.MAX_FECHA_RETROCESO "
				+ " FROM OPERACION OPER "
				+ " WHERE OPER.TIPO='SELL' AND FECHA_APERTURA<=? AND MAX_PIPS_RETROCESO IS NULL AND ROWNUM<1000";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fechaMaximo.getTime()));
			resultado = stmtConsulta.executeQuery();

			list = OperacionHelper.operaciones(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	public int cleanOperacionesPeriodo() throws SQLException {
		String sql = "DELETE FROM TMP_TOFILESTRING";
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			return stmtConsulta.executeUpdate();
		} finally {
			JDBCUtil.close(stmtConsulta);
		}
	}

	public int insertOperacionesPeriodo(ParametroOperacionPeriodo param) throws SQLException {
		String sql = "INSERT INTO TMP_TOFILESTRING (ID_INDIVIDUO, CRITERIO_ORDER1, CRITERIO_ORDER2, VIGENCIA1, VIGENCIA2) "
				+ " SELECT OPER_MES.ID_INDIVIDUO, SUM(" + param.getFirstOrder()  + "), "
						+ " SUM(" + param.getSecondOrder() + "), "
				+ " TO_DATE(MESES.MES,'YYYYMM'), ADD_MONTHS(TO_DATE(MESES.MES,'YYYYMM'),1) "
				+ " FROM OPERACION_X_MES OPER_MES "
				+ " INNER JOIN (SELECT DISTINCT MESES.MES FROM OPERACION_X_MES MESES) MESES "
				+ " ON TO_DATE(OPER_MES.MES,'YYYYMM')=ADD_MONTHS(TO_DATE(MESES.MES,'YYYYMM'),-1) "
				+ " INNER JOIN OPERACIONES_ACUM_MES_ANYO OPER_ANYO "
				+ " ON OPER_ANYO.ID_INDIVIDUO=OPER_MES.ID_INDIVIDUO "
				+ " AND TO_DATE(OPER_MES.MES,'YYYYMM')=ADD_MONTHS(TO_DATE(OPER_ANYO.MES,'YYYYMM'),-1) "
				+ " INNER JOIN OPERACIONES_ACUM_MES_CONSOL OPER " + " ON OPER_MES.ID_INDIVIDUO=OPER.ID_INDIVIDUO "
				+ " AND TO_DATE(OPER_MES.MES,'YYYYMM')=ADD_MONTHS(TO_DATE(OPER.MES,'YYYYMM'),-1) "
				+ " WHERE EXISTS (SELECT 1 FROM INDICADOR_INDIVIDUO II WHERE OPER_MES.ID_INDIVIDUO=II.ID_INDIVIDUO AND II.INTERVALO_INFERIOR IS NOT NULL) "
				+ " AND OPER.PIPS>? AND OPER_ANYO.PIPS>? AND OPER_MES.PIPS>? "
				+ " GROUP BY OPER_MES.ID_INDIVIDUO, MESES.MES ";
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setInt(1, param.getFiltroPipsTotales());
			stmtConsulta.setInt(2, param.getFiltroPipsXAnyo());
			stmtConsulta.setInt(3, param.getFiltroPipsXMes());
			return stmtConsulta.executeUpdate();
		} finally {
			JDBCUtil.close(stmtConsulta);
		}
	}

}

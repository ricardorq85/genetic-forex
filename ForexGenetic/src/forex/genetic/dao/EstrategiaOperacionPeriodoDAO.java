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

import forex.genetic.dao.helper.EstrategiaOperacionPeriodoHelper;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.ParametroOperacionPeriodo;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class EstrategiaOperacionPeriodoDAO {

	protected Connection connection = null;

	public EstrategiaOperacionPeriodoDAO(Connection connection) {
		this.connection = connection;
	}

	public boolean existe(ParametroOperacionPeriodo param) throws SQLException {
		String sql = "SELECT 1 FROM ESTRATEGIA_OPERACION_PERIODO E "
				+ " WHERE E.FILTRO_PIPS_X_SEMANA=? AND E.FILTRO_PIPS_X_MES=? "
				+ " AND E.FILTRO_PIPS_X_ANYO=? AND E.FILTRO_PIPS_TOTALES=? "
				+ " AND E.FILTRO_R2_SEMANA=? AND E.FILTRO_R2_MES=? AND E.FILTRO_R2_ANYO=? AND E.FILTRO_R2_TOTALES=? "
				+ " AND E.FILTRO_PENDIENTE_SEMANA=? AND E.FILTRO_PENDIENTE_MES=? AND E.FILTRO_PENDIENTE_ANYO=? AND E.FILTRO_PENDIENTE_TOTALES=? "
				+ " AND E.FECHA_INICIAL=? AND E.FECHA_FINAL=?  " + " AND E.TIPO_OPERACION=?";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			int index = 0;
			stmtConsulta = this.connection.prepareStatement(sql);
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

			stmtConsulta.setTimestamp(++index, new Timestamp(param.getFechaInicial().getTime()));
			stmtConsulta.setTimestamp(++index, new Timestamp(param.getFechaFinal().getTime()));
			stmtConsulta.setString(++index, param.getTipoOperacion().name());
			resultado = stmtConsulta.executeQuery();

			return (resultado.next());
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
	}

	public ParametroOperacionPeriodo consultarUltimaEjecucion(Date fechaInicial) throws SQLException {
		ParametroOperacionPeriodo param = new ParametroOperacionPeriodo();
		String sql = "SELECT EOP.* FROM (SELECT ROWNUM, E.* FROM ESTRATEGIA_OPERACION_PERIODO E "
				+ " WHERE E.FECHA_INICIAL=? ORDER BY ID DESC) EOP WHERE ROWNUM=1";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fechaInicial.getTime()));
			resultado = stmtConsulta.executeQuery();

			param = EstrategiaOperacionPeriodoHelper.estrategiaOperacionPeriodo(resultado);

		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return param;
	}

	public List<ParametroOperacionPeriodo> consultarInclusiones() throws SQLException {
		List<ParametroOperacionPeriodo> inclusiones;
		String sql = "SELECT DISTINCT " + " FILTRO_PIPS_X_SEMANA R_SEMANA, FILTRO_PIPS_X_MES R_MES, "
				+ " FILTRO_PIPS_X_ANYO R_ANYO, FILTRO_PIPS_TOTALES R_TOTALES,"
				+ " FILTRO_R2_SEMANA R2_SEMANA, FILTRO_R2_MES R2_MES, "
				+ " FILTRO_R2_ANYO R2_ANYO, FILTRO_R2_TOTALES R2_TOTALES, "
				+ " FILTRO_PENDIENTE_SEMANA PENDIENTE_SEMANA, FILTRO_PENDIENTE_MES PENDIENTE_MES,"
				+ " FILTRO_PENDIENTE_ANYO PENDIENTE_ANYO, FILTRO_PENDIENTE_TOTALES PENDIENTE_TOTALES"
				+ " FROM ESTRATEGIA_OPERACION_PERIODO EOP"
				+ " WHERE EOP.PIPS_TOTALES>0 AND  EOP.PIPS_TOTALES_PARALELAS>0 "
				+ " AND (EOP.PIPS_AGRUPADO_MINUTOS>0 AND EOP.PIPS_AGRUPADO_HORAS>0 AND EOP.PIPS_AGRUPADO_DIAS>0)"
				+ " AND EOP.CANTIDAD>1 AND EOP.CANTIDAD_INDIVIDUOS>1";
		// + "AND ROWNUM<100"
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			resultado = stmtConsulta.executeQuery();

			inclusiones = EstrategiaOperacionPeriodoHelper.inclusiones(resultado);

		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return inclusiones;
	}

	public List<ParametroOperacionPeriodo> consultarInclusionesxIndividuos(Date fechaInicio, int cantidad)
			throws SQLException {
		List<ParametroOperacionPeriodo> inclusiones;
		String sql = "SELECT DISTINCT " + " PIPS_SEMANA R_SEMANA, PIPS_MES R_MES, "
				+ " PIPS_ANYO R_ANYO, PIPS_TOTALES R_TOTALES, " + " R2_SEMANA R2_SEMANA, R2_MES R2_MES, "
				+ " R2_ANYO R2_ANYO, R2_CONSOL R2_TOTALES, "
				+ " PENDIENTE_SEMANA PENDIENTE_SEMANA, PENDIENTE_MES PENDIENTE_MES,"
				+ " PENDIENTE_ANYO PENDIENTE_ANYO, PENDIENTE_CONSOL PENDIENTE_TOTALES"
				+ " FROM PREVIO_TOFILESTRING PTFS "
				+ " WHERE PTFS.PIPS_ANYO>0 AND PTFS.PIPS_TOTALES>0 AND PTFS.TIPO_OPERACION='SELL' "
				+ " AND ROWNUM<? "
				+ " AND PTFS.FECHA_SEMANA>=? AND PTFS.FECHA_SEMANA<?";
		// + "AND ROWNUM<100"
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setInt(1, cantidad);
			stmtConsulta.setTimestamp(2, new Timestamp(DateUtil.adicionarMes(fechaInicio, -1).getTime()));
			stmtConsulta.setTimestamp(3, new Timestamp(fechaInicio.getTime()));
			resultado = stmtConsulta.executeQuery();

			inclusiones = EstrategiaOperacionPeriodoHelper.inclusiones(resultado);

		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return inclusiones;
	}

	public List<ParametroOperacionPeriodo> consultarInclusionesPromedio() throws SQLException {
		List<ParametroOperacionPeriodo> inclusiones;
		String sql = "SELECT FLOOR(FILTRO_PIPS_X_SEMANA/ 100)*100 R_SEMANA, FLOOR(FILTRO_PIPS_X_MES/ 100)*100 R_MES,"
				+ " FLOOR(FILTRO_PIPS_X_ANYO/ 100)*100 R_ANYO, FLOOR(FILTRO_PIPS_TOTALES/ 100)*100 R_TOTALES,"
				+ " ROUND(FILTRO_R2_SEMANA,2) R2_SEMANA, ROUND(FILTRO_R2_MES,2) R2_MES, "
				+ " ROUND(FILTRO_R2_ANYO,2) R2_ANYO, ROUND(FILTRO_R2_TOTALES,2) R2_TOTALES,"
				+ " ROUND(FILTRO_PENDIENTE_SEMANA,2) PENDIENTE_SEMANA, ROUND(FILTRO_PENDIENTE_MES,2) PENDIENTE_MES,"
				+ " ROUND(FILTRO_PENDIENTE_ANYO,2) PENDIENTE_ANYO, ROUND(FILTRO_PENDIENTE_TOTALES,2) PENDIENTE_TOTALES"
				+ " FROM ESTRATEGIA_OPERACION_PERIODO EOP "
				+ " GROUP BY FLOOR(FILTRO_PIPS_X_SEMANA/ 100), FLOOR(FILTRO_PIPS_X_MES/ 100),"
				+ " FLOOR(FILTRO_PIPS_X_ANYO/ 100), FLOOR(FILTRO_PIPS_TOTALES/ 100),"
				+ " ROUND(FILTRO_R2_SEMANA,2), ROUND(FILTRO_R2_MES,2),"
				+ " ROUND(FILTRO_R2_ANYO,2), ROUND(FILTRO_R2_TOTALES,2),"
				+ " ROUND(FILTRO_PENDIENTE_SEMANA,2), ROUND(FILTRO_PENDIENTE_MES,2),"
				+ " ROUND(FILTRO_PENDIENTE_ANYO,2), ROUND(FILTRO_PENDIENTE_TOTALES,2)"
				+ " HAVING MAX(EOP.PIPS_TOTALES)>0" + " AND MAX(EOP.PIPS_TOTALES_PARALELAS)>0"
				+ " ORDER BY ROUND(FLOOR(AVG(EOP.PIPS_TOTALES)/1000))*1000 DESC, ROUND(FLOOR(AVG(EOP.PIPS_TOTALES_PARALELAS)/1000))*1000 DESC,"
				+ " ROUND(FLOOR(AVG(EOP.PIPS_AGRUPADO_DIAS)/100))*100 DESC, ROUND(FLOOR(AVG(EOP.PIPS_AGRUPADO_HORAS)/100))*100 DESC,"
				+ " ROUND(FLOOR(AVG(EOP.PIPS_AGRUPADO_MINUTOS)/100))*100 DESC";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			resultado = stmtConsulta.executeQuery();

			inclusiones = EstrategiaOperacionPeriodoHelper.inclusiones(resultado);

		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return inclusiones;
	}

	/**
	 *
	 * @param individuo
	 * @param operaciones
	 * @throws SQLException
	 */
	public int insert(ParametroOperacionPeriodo param) throws SQLException {
		String sql = "INSERT INTO ESTRATEGIA_OPERACION_PERIODO(ID, "
				+ " FILTRO_PIPS_X_SEMANA, FILTRO_PIPS_X_MES, FILTRO_PIPS_X_ANYO, FILTRO_PIPS_TOTALES, "
				+ " FILTRO_R2_SEMANA, FILTRO_R2_MES, FILTRO_R2_ANYO, FILTRO_R2_TOTALES, "
				+ " FILTRO_PENDIENTE_SEMANA, FILTRO_PENDIENTE_MES, FILTRO_PENDIENTE_ANYO, FILTRO_PENDIENTE_TOTALES, "
				+ " FIRST_ORDER, SECOND_ORDER, "
				+ " PIPS_TOTALES, CANTIDAD, PIPS_TOTALES_PARALELAS, CANTIDAD_PARALELAS, "
				+ " FECHA, FECHA_INICIAL, FECHA_FINAL,"
				+ " PIPS_AGRUPADO_MINUTOS, PIPS_AGRUPADO_HORAS, PIPS_AGRUPADO_DIAS, TIPO_OPERACION, CANTIDAD_INDIVIDUOS, MAX_FECHA_CIERRE, EOP_VERSION) "
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		PreparedStatement statement = null;
		int nextId = 0;
		try {
			nextId = this.nextId();
			int index = 1;
			statement = connection.prepareStatement(sql);
			statement.setInt(index++, nextId);
			statement.setInt(index++, param.getFiltroPipsXSemana());
			statement.setInt(index++, param.getFiltroPipsXMes());
			statement.setInt(index++, param.getFiltroPipsXAnyo());
			statement.setInt(index++, param.getFiltroPipsTotales());

			statement.setDouble(index++, param.getFiltroR2Semana());
			statement.setDouble(index++, param.getFiltroR2Mes());
			statement.setDouble(index++, param.getFiltroR2Anyo());
			statement.setDouble(index++, param.getFiltroR2Totales());
			statement.setDouble(index++, param.getFiltroPendienteSemana());
			statement.setDouble(index++, param.getFiltroPendienteMes());
			statement.setDouble(index++, param.getFiltroPendienteAnyo());
			statement.setDouble(index++, param.getFiltroPendienteTotales());

			statement.setString(index++, param.getFirstOrder());
			statement.setString(index++, param.getSecondOrder());
			statement.setDouble(index++, param.getPipsTotales());
			statement.setInt(index++, param.getCantidad());
			statement.setDouble(index++, param.getPipsParalelas());
			statement.setInt(index++, param.getCantidadParalelas());
			statement.setTimestamp(index++, new Timestamp(param.getFecha().getTime()));
			statement.setTimestamp(index++, new Timestamp(param.getFechaInicial().getTime()));
			statement.setTimestamp(index++, new Timestamp(param.getFechaFinal().getTime()));
			statement.setDouble(index++, param.getPipsAgrupadoMinutos());
			statement.setDouble(index++, param.getPipsAgrupadoHoras());
			statement.setDouble(index++, param.getPipsAgrupadoDias());
			statement.setString(index++, param.getTipoOperacion().name());
			statement.setInt(index++, param.getCantidadIndividuos());
			if (param.getMaxFechaCierre() != null) {
				statement.setTimestamp(index++, new Timestamp(param.getMaxFechaCierre().getTime()));
			} else {
				statement.setNull(index++, java.sql.Types.TIMESTAMP);
			}
			statement.setString(index++, param.getVersion());
			statement.executeUpdate();
		} finally {
			JDBCUtil.close(statement);
		}
		return nextId;
	}

	public int nextId() throws SQLException {
		String sql = "SELECT EST_OPER_PERIODO_TRG_SEQ.nextval NEXT_ID FROM dual";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		int id = 0;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			resultado = stmtConsulta.executeQuery();

			if (resultado.next()) {
				id = resultado.getInt("NEXT_ID");
			}
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return id;
	}

	public void insertOperacionesPeriodo(ParametroOperacionPeriodo param, Individuo individuo, List<Order> operaciones)
			throws SQLException {
		String sql = "INSERT INTO OPERACION_ESTRATEGIA_PERIODO(ID_INDIVIDUO, TAKE_PROFIT, STOP_LOSS, "
				+ " FECHA_APERTURA, FECHA_CIERRE, SPREAD, OPEN_PRICE, PIPS, LOTE, TIPO, FECHA, ESTRATEGIA_PERIODO) "
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

		for (Order operacion : operaciones) {
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement(sql);
				Order order = operacion;
				statement.setString(1, individuo.getId());
				statement.setDouble(2, order.getTakeProfit());
				statement.setDouble(3, order.getStopLoss());
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
				statement.setInt(12, param.getId());
				statement.executeUpdate();
			} finally {
				JDBCUtil.close(statement);
			}
		}
	}
}

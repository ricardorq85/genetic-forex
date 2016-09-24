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
				+ " AND E.FECHA_INICIAL=? AND E.FECHA_FINAL=?  "
				+ " AND E.TIPO_OPERACION=?";

		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setInt(1, param.getFiltroPipsXSemana());
			stmtConsulta.setInt(2, param.getFiltroPipsXMes());
			stmtConsulta.setInt(3, param.getFiltroPipsXAnyo());
			stmtConsulta.setInt(4, param.getFiltroPipsTotales());
			stmtConsulta.setTimestamp(5, new Timestamp(param.getFechaInicial().getTime()));
			stmtConsulta.setTimestamp(6, new Timestamp(param.getFechaFinal().getTime()));
			stmtConsulta.setString(7, param.getTipoOperacion().name());
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
		String sql = "SELECT FLOOR(FILTRO_PIPS_X_SEMANA/ 100)*100 R_SEMANA, FLOOR(FILTRO_PIPS_X_MES/ 100)*100 R_MES,"
				+ " FLOOR(FILTRO_PIPS_X_ANYO/ 100)*100 R_ANYO, FLOOR(FILTRO_PIPS_TOTALES/ 100)*100 R_TOTALES"
				+ " FROM ESTRATEGIA_OPERACION_PERIODO EOP "
				+ " GROUP BY FLOOR(FILTRO_PIPS_X_SEMANA/ 100), FLOOR(FILTRO_PIPS_X_MES/ 100), "
				+ " FLOOR(FILTRO_PIPS_X_ANYO/ 100), FLOOR(FILTRO_PIPS_TOTALES/ 100) "
				+ " HAVING MAX(EOP.PIPS_TOTALES)>=5000"
				+ " ORDER BY ROUND(FLOOR(AVG(EOP.PIPS_TOTALES)/1000))*1000 DESC, ROUND(FLOOR(AVG(EOP.PIPS_TOTALES_PARALELAS)/1000))*1000 DESC,"
				+ " ROUND(FLOOR(AVG(EOP.PIPS_AGRUPADO_DIAS)/100))*100 DESC, ROUND(FLOOR(AVG(EOP.PIPS_AGRUPADO_HORAS)/100))*100 DESC,"
				+ " ROUND(FLOOR(AVG(EOP.PIPS_AGRUPADO_MINUTOS)/100))*100 DESC";

				//+ " ORDER BY MAX(EOP.PIPS_TOTALES) DESC";

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
				+ " FILTRO_PIPS_X_SEMANA, FILTRO_PIPS_X_MES, FILTRO_PIPS_X_ANYO, "
				+ " FILTRO_PIPS_TOTALES, FIRST_ORDER, SECOND_ORDER, "
				+ " PIPS_TOTALES, CANTIDAD, PIPS_TOTALES_PARALELAS, CANTIDAD_PARALELAS, "
				+ " FECHA, FECHA_INICIAL, FECHA_FINAL,"
				+ " PIPS_AGRUPADO_MINUTOS, PIPS_AGRUPADO_HORAS, PIPS_AGRUPADO_DIAS, TIPO_OPERACION) "
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

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

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
public class OperacionSemanalDAO {

	protected Connection connection = null;

	public OperacionSemanalDAO(Connection connection) {
		this.connection = connection;
	}

	public int deleteOperacionesSemana(Individuo individuo, String tabla) throws SQLException {
		String sql = "DELETE FROM " + tabla + " OPER WHERE OPER.ID_INDIVIDUO IN ("
				+ " SELECT P.ID_INDIVIDUO FROM PROCESO P"
				+ " WHERE P.ID_INDIVIDUO=OPER.ID_INDIVIDUO AND P.FECHA_HISTORICO>OPER.FECHA_HISTORICO)"
				+ " AND ID_INDIVIDUO=?";
		PreparedStatement stmt = null;
		try {
			stmt = this.connection.prepareStatement(sql);
			stmt.setString(1, individuo.getId());
			return stmt.executeUpdate();
		} finally {
			JDBCUtil.close(stmt);
		}
	}

	public int insertOperacionesSemana(Individuo individuo) throws SQLException {
		String sql = "INSERT INTO OPERACION_X_SEMANA (ID_INDIVIDUO, TIPO_OPERACION, FECHA_HISTORICO, PIPS, CANTIDAD, FECHA_SEMANA) "
				+ " SELECT OPER.ID_INDIVIDUO, IND.TIPO_OPERACION, MAX(P.FECHA_HISTORICO) FECHA_HISTORICO,"
				+ " SUM(OPER.PIPS) PIPS, COUNT(*) CANTIDAD, TRUNC(MIN(FECHA_CIERRE),'IW')" + " FROM OPERACION OPER"
				+ " INNER JOIN INDIVIDUO IND ON IND.ID=OPER.ID_INDIVIDUO"
				+ " INNER JOIN PROCESO P ON P.ID_INDIVIDUO=OPER.ID_INDIVIDUO" + " WHERE OPER.FECHA_CIERRE IS NOT NULL"
				+ " AND NOT EXISTS (SELECT 1 FROM OPERACION_X_SEMANA OPC WHERE OPC.ID_INDIVIDUO=OPER.ID_INDIVIDUO) "
				+ " AND IND.TIPO_INDIVIDUO = 'INDICADORES' AND OPER.ID_INDIVIDUO=?"
				+ " GROUP BY OPER.ID_INDIVIDUO, IND.TIPO_OPERACION, TRUNC(FECHA_CIERRE,'IW')";

		PreparedStatement stmt = null;
		try {
			stmt = this.connection.prepareStatement(sql);
			stmt.setString(1, individuo.getId());
			return stmt.executeUpdate();
		} finally {
			JDBCUtil.close(stmt);
		}
	}

	public int insertSemanas(Individuo individuo) throws SQLException {
		String sql = "INSERT INTO SEMANAS(FECHA_SEMANA) SELECT DISTINCT SEM.FECHA_SEMANA+7"
				+ " FROM OPERACION_X_SEMANA SEM WHERE SEM.ID_INDIVIDUO=?"
				+ " AND SEM.FECHA_SEMANA+7 NOT IN(SELECT S.FECHA_SEMANA FROM SEMANAS S)";

		PreparedStatement stmt = null;
		try {
			stmt = this.connection.prepareStatement(sql);
			stmt.setString(1, individuo.getId());
			return stmt.executeUpdate();
		} finally {
			JDBCUtil.close(stmt);
		}
	}

	public int insertOperacionesSemanaAcumuladas(Individuo individuo, String tabla, int pipsMinimos,
			int semanasRetroceso) throws SQLException {
		String sql = "INSERT INTO " + tabla + " (ID_INDIVIDUO, FECHA_SEMANA, PIPS, CANTIDAD, FECHA_HISTORICO)"
				+ " SELECT TMP.ID_INDIVIDUO, SEMANAS.FECHA_SEMANA, SUM(TMP.PIPS) PIPS, SUM(TMP.CANTIDAD) CANTIDAD,"
				+ " MAX(P.FECHA_HISTORICO) FECHA_HISTORICO " + " FROM SEMANAS " + " INNER JOIN OPERACION_X_SEMANA TMP"
				+ " ON TMP.FECHA_SEMANA>=ADD_MONTHS(SEMANAS.FECHA_SEMANA,?)"
				+ " AND TMP.FECHA_SEMANA<=SEMANAS.FECHA_SEMANA"
				+ " INNER JOIN PROCESO P ON P.ID_INDIVIDUO=TMP.ID_INDIVIDUO" + " WHERE TMP.ID_INDIVIDUO=? AND"
				+ " NOT EXISTS (SELECT 1 FROM " + tabla + " OPC WHERE OPC.ID_INDIVIDUO=TMP.ID_INDIVIDUO)"
				+ " AND SEMANAS.FECHA_SEMANA>=TO_DATE('20110101','YYYYMMDD')"
				+ " GROUP BY TMP.ID_INDIVIDUO, SEMANAS.FECHA_SEMANA" + " HAVING SUM(TMP.PIPS)>=?";

		PreparedStatement stmt = null;
		try {
			stmt = this.connection.prepareStatement(sql);
			stmt.setInt(1, semanasRetroceso);
			stmt.setString(2, individuo.getId());
			stmt.setInt(3, pipsMinimos);
			return stmt.executeUpdate();
		} finally {
			JDBCUtil.close(stmt);
		}
	}

	public int insertOperacionesSemanaPrevio(Individuo individuo) throws SQLException {
		String sql = "INSERT INTO PREVIO_TOFILESTRING(ID_INDIVIDUO, FECHA_SEMANA, PIPS_SEMANA, PIPS_MES, PIPS_ANYO, PIPS_TOTALES, TIPO_OPERACION, FECHA_HISTORICO)"
				+ " SELECT OPER_SEMANA.ID_INDIVIDUO, SEMANAS.FECHA_SEMANA, "
				+ " OPER_SEMANA.PIPS PIPS_SEMANA, OPER_MES.PIPS PIPS_MES, OPER_ANYO.PIPS PIPS_ANYO, OPER.PIPS PIPS_TOTALES, OPER_SEMANA.TIPO_OPERACION, OPER_SEMANA.FECHA_HISTORICO"
				+ " FROM SEMANAS "
				+ " LEFT JOIN OPERACION_X_SEMANA OPER_SEMANA "
				+ " ON SEMANAS.FECHA_SEMANA=OPER_SEMANA.FECHA_SEMANA+7"
				+ " INNER JOIN OPERACIONES_ACUM_SEMANA_MES OPER_MES "
				+ " ON OPER_MES.ID_INDIVIDUO=OPER_SEMANA.ID_INDIVIDUO "
				+ " AND OPER_MES.FECHA_SEMANA=SEMANAS.FECHA_SEMANA-7   "
				+ " INNER JOIN OPERACIONES_ACUM_SEMANA_ANYO OPER_ANYO "
				+ " ON OPER_ANYO.ID_INDIVIDUO=OPER_SEMANA.ID_INDIVIDUO "
				+ " AND OPER_ANYO.FECHA_SEMANA=SEMANAS.FECHA_SEMANA-7 "
				+ " INNER JOIN OPERACIONES_ACUM_SEMANA_CONSOL OPER "
				+ " ON OPER.ID_INDIVIDUO=OPER_SEMANA.ID_INDIVIDUO"
				+ " AND OPER.FECHA_SEMANA=SEMANAS.FECHA_SEMANA-7"
				+ " WHERE NOT EXISTS (SELECT 1 FROM PREVIO_TOFILESTRING OPC WHERE OPC.ID_INDIVIDUO=OPER_MES.ID_INDIVIDUO) "
				+ " AND OPER_MES.ID_INDIVIDUO=?";

		PreparedStatement stmt = null;
		try {
			stmt = this.connection.prepareStatement(sql);
			stmt.setString(1, individuo.getId());
			return stmt.executeUpdate();
		} finally {
			JDBCUtil.close(stmt);
		}
	}

}

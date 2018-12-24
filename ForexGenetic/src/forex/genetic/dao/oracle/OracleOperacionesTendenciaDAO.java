package forex.genetic.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.helper.OperacionHelper;
import forex.genetic.entities.Individuo;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.Constants;
import forex.genetic.util.jdbc.JDBCUtil;

public class OracleOperacionesTendenciaDAO extends OracleOperacionesDAO {

	public OracleOperacionesTendenciaDAO(Connection connection) {
		super(connection);
	}

	private String getQuery() {
		String sql = "SELECT * FROM (SELECT OPER.ID_INDIVIDUO, OPER.FECHA_APERTURA, OPER.FECHA_CIERRE, OPER.OPEN_PRICE, OPER.SPREAD, "
				+ " OPER.LOTE, OPER.PIPS, OPER.TIPO, OPER.TIPO TIPO_OPERACION, OPER.TAKE_PROFIT, OPER.STOP_LOSS "
				+ " FROM OPERACION OPER "
				+ " INNER JOIN PROCESO PROC ON PROC.ID_INDIVIDUO=OPER.ID_INDIVIDUO AND (PROC.FECHA_HISTORICO>=?) "
				+ " INNER JOIN INDIVIDUO_INDICADORES IND ON IND.ID=OPER.ID_INDIVIDUO "
				+ " WHERE OPER.FECHA_APERTURA<?	AND (OPER.FECHA_CIERRE IS NULL	OR OPER.FECHA_CIERRE>?) "
				+ " AND OPER.FECHA_APERTURA>?-20 "
				+ " AND NOT EXISTS (SELECT 1 FROM TENDENCIA T WHERE T.ID_INDIVIDUO=OPER.ID_INDIVIDUO "
				+ " AND T.FECHA_BASE=? AND T.TIPO_TENDENCIA=?) "
//				+ "  AND OPER.ID_INDIVIDUO='1394841600000.83'"
				+ " ORDER BY DBMS_RANDOM.VALUE) "
				//+ " ORDER BY OPER.FECHA_APERTURA ASC) "
				//+ " ORDER BY (OPER.TAKE_PROFIT) DESC) "
				+ " WHERE ROWNUM<? ";
		return sql;
	}

	public Individuo consultarIndividuoOperacionActiva(String idIndividuo, Date fechaBase, int filas) throws GeneticDAOException {
		Individuo individuo = null;
		String sql = this.getQuery() + " AND ID_INDIVIDUO=?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fechaBase.getTime()));
			stmtConsulta.setTimestamp(2, new Timestamp(fechaBase.getTime()));
			stmtConsulta.setTimestamp(3, new Timestamp(fechaBase.getTime()));
			stmtConsulta.setTimestamp(4, new Timestamp(fechaBase.getTime()));
			stmtConsulta.setTimestamp(5, new Timestamp(fechaBase.getTime()));
			stmtConsulta.setString(6, Constants.TIPO_TENDENCIA);
			stmtConsulta.setInt(7, filas);
			stmtConsulta.setString(8, idIndividuo);
			resultado = stmtConsulta.executeQuery();

			individuo = OperacionHelper.individuoOperacionActiva(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException(e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return individuo;
	}

	public List<Individuo> consultarIndividuoOperacionActiva(Date fechaBase, int filas) throws GeneticDAOException {
		List<Individuo> individuos = null;
		StringBuilder sqlBuffer = new StringBuilder(this.getQuery());
		//sqlBuffer.append(" ORDER BY PROC.FECHA_PROCESO DESC");
		String sql = sqlBuffer.toString();
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(fechaBase.getTime()));
			stmtConsulta.setTimestamp(2, new Timestamp(fechaBase.getTime()));
			stmtConsulta.setTimestamp(3, new Timestamp(fechaBase.getTime()));
			stmtConsulta.setTimestamp(4, new Timestamp(fechaBase.getTime()));
			stmtConsulta.setTimestamp(5, new Timestamp(fechaBase.getTime()));
			stmtConsulta.setString(6, Constants.TIPO_TENDENCIA);
			stmtConsulta.setInt(7, filas);
			resultado = stmtConsulta.executeQuery();

			individuos = OperacionHelper.individuosOperacionActiva(resultado);
		} catch (SQLException e) {
			throw new GeneticDAOException(null, e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return individuos;
	}

}

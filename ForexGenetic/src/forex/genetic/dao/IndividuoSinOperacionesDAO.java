/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.helper.IndividuoHelper;
import forex.genetic.dao.oracle.OracleIndividuoDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class IndividuoSinOperacionesDAO extends OracleIndividuoDAO {

	public IndividuoSinOperacionesDAO(Connection connection) {
		super(connection);
	}

	@Override
	public List<Individuo> consultarIndividuosParaBorrar(Date fechaLimite) throws SQLException {
		List<Individuo> list = null;
		String sql = "SELECT IND.* FROM INDIVIDUO IND"
				+ " INNER JOIN PROCESO P ON P.ID_INDIVIDUO=IND.ID AND P.FECHA_HISTORICO>=?"
				+ " WHERE (SELECT COUNT(*) FROM OPERACION OPER WHERE OPER.ID_INDIVIDUO=IND.ID)<10"
				+ " AND ROWNUM<1000";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new java.sql.Timestamp(fechaLimite.getTime()));
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosBase(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	@Override
	public List<Individuo> consultarIndividuosParaBorrar(String idIndividuo, Date fechaLimite)
			throws SQLException {
		List<Individuo> list = null;
		String sql = "SELECT IND.* FROM INDIVIDUO IND"
				+ " INNER JOIN PROCESO P ON P.ID_INDIVIDUO=IND.ID AND P.FECHA_HISTORICO>=?"
				+ " WHERE (SELECT COUNT(*) FROM OPERACION OPER WHERE OPER.ID_INDIVIDUO=IND.ID)<10" + " AND IND.ID=?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new java.sql.Timestamp(fechaLimite.getTime()));
			stmtConsulta.setString(2, idIndividuo);
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosBase(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}
	
}

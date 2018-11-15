/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import forex.genetic.dao.helper.IndividuoHelper;
import forex.genetic.dao.oracle.OracleIndividuoDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class DuracionIndividuoDAO extends OracleIndividuoDAO {

	public DuracionIndividuoDAO(Connection connection) {
		super(connection);
	}

	@Override
	public List<Individuo> consultarIndividuosParaBorrar(int minutos) throws SQLException {
		List<Individuo> list = null;
		String sql = "SELECT OPER.ID_INDIVIDUO, "
				+ " NULL ID_INDIVIDUO_PADRE"
				+ "  FROM OPERACION OPER"
				+ " INNER JOIN PROCESO P ON OPER.ID_INDIVIDUO=P.ID_INDIVIDUO AND P.FECHA_HISTORICO>TO_DATE('20150101', 'YYYYMMDD') "
				+ " GROUP BY OPER.ID_INDIVIDUO"
				+ " HAVING AVG(OPER.FECHA_CIERRE-OPER.FECHA_APERTURA)<(?/24/60)";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setInt(1, minutos);
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosById(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}

	@Override
	public List<Individuo> consultarIndividuosParaBorrar(String idIndividuo, int minutos) throws SQLException {
		List<Individuo> list = null;
		String sql = "SELECT OPER.ID_INDIVIDUO, "
				+ " NULL ID_INDIVIDUO_PADRE"
				+ "  FROM OPERACION OPER"
				+ " WHERE OPER.ID_INDIVIDUO=?"
				+ " GROUP BY OPER.ID_INDIVIDUO"
				+ " HAVING AVG(OPER.FECHA_CIERRE-OPER.FECHA_APERTURA)<(?/24/60)";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;

		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, idIndividuo);
			stmtConsulta.setInt(2, minutos);			
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosById(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}

		return list;
	}	
	
}

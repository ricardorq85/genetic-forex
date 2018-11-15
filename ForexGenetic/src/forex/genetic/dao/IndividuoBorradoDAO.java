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
public class IndividuoBorradoDAO extends OracleIndividuoDAO {

	/**
	 *
	 * @param connection
	 */
	public IndividuoBorradoDAO(Connection connection) {
		super(connection);
	}

	public List<Individuo> consultarIndividuosRepetidos() throws SQLException {
		List<Individuo> list = null;
		String sql = "SELECT ID_INDIVIDUO2 ID_INDIVIDUO, ID_INDIVIDUO1 ID_INDIVIDUO_PADRE "
				+ " FROM INDIVIDUOS_REPETIDOS_BORRADOS WHERE ROWNUM < 300";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosById(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return list;
	}

	public List<Individuo> consultarIndividuoHijoRepetido(Individuo individuoHijo) throws SQLException {
		List<Individuo> list = null;
		String sql = "SELECT ID_INDIVIDUO2 ID_INDIVIDUO, ID_INDIVIDUO1 ID_INDIVIDUO_PADRE "
				+ " FROM INDIVIDUOS_REPETIDOS_BORRADOS " + " WHERE ID_INDIVIDUO2 = ?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, individuoHijo.getId());
			resultado = stmtConsulta.executeQuery();

			list = IndividuoHelper.createIndividuosById(resultado);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return list;
	}
}

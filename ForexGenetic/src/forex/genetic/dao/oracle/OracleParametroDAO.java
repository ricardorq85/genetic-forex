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

import forex.genetic.bo.Parametro;
import forex.genetic.dao.IParametroDAO;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class OracleParametroDAO extends OracleGeneticDAO<Parametro> implements IParametroDAO {

	public OracleParametroDAO(Connection connection) {
		super(connection);
	}

	/**
	 *
	 * @param nombre
	 * @return
	 */
	public int getIntValorParametro(String nombre) throws GeneticDAOException {
		String valor = getValorParametro(nombre);
		return (Integer.parseInt(valor));
	}

	public float getFloatValorParametro(String nombre) throws GeneticDAOException {
		String valor = getValorParametro(nombre);
		return (Float.parseFloat(valor));
	}

	public boolean getBooleanValorParametro(String nombre) throws GeneticDAOException {
		String valor = getValorParametro(nombre);
		return Boolean.parseBoolean(valor);
	}

	public String getValorParametro(String nombre) throws GeneticDAOException {
		String valor = null;
		String sql = "SELECT VALOR FROM PARAMETRO WHERE NOMBRE=?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, nombre);
			resultado = stmtConsulta.executeQuery();
			if (resultado.next()) {
				valor = resultado.getString("VALOR");
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("Error Parametro DAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
 		}
		return valor;
	}

	public String[] getArrayStringParametro(String nombre) throws GeneticDAOException {
		String valor = this.getValorParametro(nombre);
		String[] arrayString = valor.split(",", 0);
		return arrayString;
	}

	/**
	 *
	 * @param nombre
	 * @return
	 * @throws SQLException
	 */
	public Date getDateValorParametro(String nombre) throws GeneticDAOException {
		Date valor = null;
		String sql = "SELECT TO_DATE(VALOR,'YYYY/MM/DD HH24:MI') VALOR FROM PARAMETRO WHERE NOMBRE=?";
		PreparedStatement stmtConsulta = null;
		ResultSet resultado = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, nombre);
			resultado = stmtConsulta.executeQuery();
			if (resultado.next()) {
				if (!(resultado.getObject("VALOR") == null)) {
					valor = new Date(resultado.getTimestamp("VALOR").getTime());
				}
			}
		} catch (SQLException e) {
			throw new GeneticDAOException("Error Parametro DAO", e);
		} finally {
			JDBCUtil.close(resultado);
			JDBCUtil.close(stmtConsulta);
		}
		return valor;
	}

	public void updateDateValorParametro(String nombre, Date valor) throws GeneticDAOException {
		String sql = "UPDATE PARAMETRO SET VALOR=TO_CHAR(?,'YYYY/MM/DD HH24:MI') WHERE NOMBRE=?";
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setTimestamp(1, new Timestamp(valor.getTime()));
			stmtConsulta.setString(2, nombre);
			stmtConsulta.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error Parametro DAO", e);
		} finally {
			JDBCUtil.close(stmtConsulta);
		}
	}

	/**
	 *
	 * @param nombre
	 * @param valor
	 * @throws SQLException
	 */
	public void updateValorParametro(String nombre, String valor) throws GeneticDAOException {
		String sql = "UPDATE PARAMETRO SET VALOR=? WHERE NOMBRE=?";
		PreparedStatement stmtConsulta = null;
		try {
			stmtConsulta = this.connection.prepareStatement(sql);
			stmtConsulta.setString(1, valor);
			stmtConsulta.setString(2, nombre);
			stmtConsulta.executeUpdate();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error Parametro DAO", e);
		} finally {
			JDBCUtil.close(stmtConsulta);
		}
	}

	@Override
	public boolean exists(Parametro obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void insert(Parametro obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Parametro obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertOrUpdate(Parametro obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertIfNoExists(Parametro obj) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}
}

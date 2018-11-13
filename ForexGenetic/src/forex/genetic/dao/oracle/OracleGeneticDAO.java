package forex.genetic.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;

import forex.genetic.dao.GeneticDAO;
import forex.genetic.exception.GeneticDAOException;

public abstract class OracleGeneticDAO<E> extends GeneticDAO<E> {

	protected Connection connection;

	public OracleGeneticDAO(Connection connection) {
		this.connection = connection;
	}

	public void commit() throws GeneticDAOException {
		try {
			connection.commit();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error cerrando conexion", e);
		}
	}

	public void closeConnection() throws GeneticDAOException {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error cerrando conexion", e);
		}
	}
}

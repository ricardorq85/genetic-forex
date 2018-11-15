package forex.genetic.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;

import forex.genetic.dao.IGeneticDAO;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.jdbc.JDBCUtil;

public abstract class OracleGeneticDAO<E> implements IGeneticDAO<E> {

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
	
    public void rollback() throws GeneticDAOException {
        try {
			this.connection.rollback();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error isClosed()", e);
		}
    }
	

	public boolean isClosed() throws GeneticDAOException {
		try {
			return ((this.connection == null) || (this.connection.isClosed()));
		} catch (SQLException e) {
			throw new GeneticDAOException("Error isClosed()", e);
		}
	}

	public void restoreConnection() throws GeneticDAOException {
		try {
			this.connection = JDBCUtil.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			throw new GeneticDAOException("Error restoreConnection", e);
		}
	}

	public void close() throws GeneticDAOException {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error cerrando conexion", e);
		}
	}

	public void insertOrUpdate(E obj) throws GeneticDAOException {
		if (exists(obj)) {
			update(obj);
			System.out.print("*");
		} else {
			insert(obj);
			System.out.print(".");
		}
	}

}

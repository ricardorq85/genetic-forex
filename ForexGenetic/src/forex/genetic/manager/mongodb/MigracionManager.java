/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.mongodb;

import java.sql.Connection;
import java.sql.SQLException;

import forex.genetic.dao.mongodb.MongoGeneticDAO;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public abstract class MigracionManager<E> {

	protected Connection conn;
	protected MongoGeneticDAO<E> mongoDestinoDAO;

	/**
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 *
	 */
	public MigracionManager() throws ClassNotFoundException, GeneticDAOException {
		try {
			conn = JDBCUtil.getConnection();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error migracion manager", e);
		}
		mongoDestinoDAO = getDestinoDAO();
	}
	
	protected abstract MongoGeneticDAO<E> getDestinoDAO();

	public abstract void migrate() throws GeneticDAOException;
}
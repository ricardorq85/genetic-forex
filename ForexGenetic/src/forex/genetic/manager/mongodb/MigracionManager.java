/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.mongodb;

import java.sql.Connection;
import java.sql.SQLException;

import forex.genetic.dao.mongodb.MongoGeneticDAO;
import forex.genetic.exception.GeneticBusinessException;
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
	 * @throws GeneticBusinessException 
	 *
	 */
	public MigracionManager() throws ClassNotFoundException, GeneticBusinessException {
		try {
			conn = JDBCUtil.getConnection();
		} catch (SQLException e) {
			throw new GeneticBusinessException("Error migracion manager", e);
		}
		mongoDestinoDAO = getDestinoDAO();
	}
	
	protected abstract MongoGeneticDAO<E> getDestinoDAO() throws GeneticBusinessException;

	public abstract void migrate() throws GeneticBusinessException;
}
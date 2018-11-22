package forex.genetic.dao;

import forex.genetic.exception.GeneticDAOException;

public interface IGeneticDAO<E> {

	public boolean exists(E obj) throws GeneticDAOException;

	public void insert(E obj) throws GeneticDAOException;
	
	public void insertIfNoExists(E obj) throws GeneticDAOException;

	public void update(E obj) throws GeneticDAOException;

	public void insertOrUpdate(E obj) throws GeneticDAOException;

	public void close() throws GeneticDAOException;

	public void commit() throws GeneticDAOException;

	public boolean isClosed() throws GeneticDAOException;

	public void restoreConnection() throws GeneticDAOException;
	
	public void rollback() throws GeneticDAOException;
}

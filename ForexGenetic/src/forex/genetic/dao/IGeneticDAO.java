package forex.genetic.dao;

import forex.genetic.exception.GeneticDAOException;

public interface IGeneticDAO<E> {

	public boolean exists(E obj) throws GeneticDAOException;
	
	public void insert(E obj) throws GeneticDAOException;

	public void update(E obj) throws GeneticDAOException;

	public void insertOrUpdate(E obj) throws GeneticDAOException;

	public void closeConnection() throws GeneticDAOException;
	
	public void commit() throws GeneticDAOException;
}

package forex.genetic.dao;

import forex.genetic.exception.GeneticDAOException;

public abstract class GeneticDAO<E> {

	public abstract void insertOrUpdate(E obj) throws GeneticDAOException;

	public abstract void closeConnection() throws GeneticDAOException;
	
	public abstract void commit() throws GeneticDAOException;
}

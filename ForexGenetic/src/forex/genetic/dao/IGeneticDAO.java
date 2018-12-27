package forex.genetic.dao;

import java.util.Date;

import forex.genetic.entities.Individuo;
import forex.genetic.exception.GeneticDAOException;

public interface IGeneticDAO<E> {

	int deleteByIndividuo(Individuo individuo) throws GeneticDAOException;

	boolean exists(E obj) throws GeneticDAOException;

	void insert(E obj) throws GeneticDAOException;

	void insertIfNoExists(E obj) throws GeneticDAOException;

	void update(E obj) throws GeneticDAOException;

	void insertOrUpdate(E obj) throws GeneticDAOException;

	long delete(E obj, Date fechaReferencia) throws GeneticDAOException;

	void close() throws GeneticDAOException;

	void commit() throws GeneticDAOException;

	boolean isClosed() throws GeneticDAOException;

	void restoreConnection() throws GeneticDAOException;

	void rollback() throws GeneticDAOException;
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.mongodb;

import forex.genetic.dao.mongodb.MongoGeneticDAO;
import forex.genetic.exception.GeneticBusinessException;

/**
 *
 * @author ricardorq85
 */
public abstract class MongoExportManager<E> {

	protected MongoGeneticDAO<E> dao;

	public MongoExportManager() throws GeneticBusinessException {
	}

	public abstract void exportar() throws GeneticBusinessException;

}
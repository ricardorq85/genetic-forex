/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.mongodb;

import com.mongodb.client.model.IndexOptions;

import forex.genetic.exception.GeneticDAOException;

/**
 *
 * @author ricardorq85
 */
public class MongoDefaultDAO<T> extends MongoGeneticDAO<T> {

	public MongoDefaultDAO(String collectionName) {
		super(collectionName, true);
	}

	public MongoDefaultDAO(String collectionName, boolean configure) {
		super(collectionName, configure);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		//this.collection.createIndex(Indexes.ascending("moneda", "periodo", "fechaHistorico"), indexOptions);
		//this.collection.createIndex(Indexes.ascending("fechaHistorico"), indexOptions);
	}

	@Override
	public boolean exists(T obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void insert(T obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(T obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

}

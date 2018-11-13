/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.mongodb;

import com.mongodb.client.model.IndexOptions;

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

}

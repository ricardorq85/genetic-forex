/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.mongodb;

import com.mongodb.client.model.IndexOptions;

import forex.genetic.entities.DatoAdicionalTPO;

/**
 *
 * @author ricardorq85
 */
public class MongoDatoAdicionalTPODAO extends MongoGeneticDAO<DatoAdicionalTPO> {

	// public MongoTendenciaParaOperarDAO(Connection connection) {
	public MongoDatoAdicionalTPODAO() {
		super("datoAdicionalTPO", true);
		this.configureCollection();
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

//		this.collection.createIndex(Indexes.ascending("periodo", "fechaBase", "tipoOperacion", "tipoExportacion"),
	//			indexOptions);
	}

}

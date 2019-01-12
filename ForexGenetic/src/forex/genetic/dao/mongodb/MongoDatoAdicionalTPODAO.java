/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.mongodb;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;

import forex.genetic.dao.IDatoAdicionalTPODAO;
import forex.genetic.entities.DatoAdicionalTPO;
import forex.genetic.exception.GeneticDAOException;

/**
 *
 * @author ricardorq85
 */
public class MongoDatoAdicionalTPODAO extends MongoGeneticDAO<DatoAdicionalTPO> implements IDatoAdicionalTPODAO {

	// public MongoTendenciaParaOperarDAO(Connection connection) {
	public MongoDatoAdicionalTPODAO(MongoDatabase db) throws GeneticDAOException {
		super(db, "datoAdicionalTPO", true);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

//		this.collection.createIndex(Indexes.ascending("periodo", "fechaBase", "tipoOperacion", "tipoExportacion"),
	//			indexOptions);
	}
}

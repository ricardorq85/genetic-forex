package forex.genetic.dao.mongodb;

import com.mongodb.client.model.IndexOptions;

import forex.genetic.entities.Tendencia;

/**
 *
 * @author ricardorq85
 */
public class MongoTendenciaDAO extends MongoGeneticDAO<Tendencia> {

	public MongoTendenciaDAO() {
		super("tendencia", true);
	}

	public MongoTendenciaDAO(boolean configure) {
		super("tendencia", configure);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		//this.collection.createIndex(Indexes.ascending("moneda", "periodo", "fechaHistorico"), indexOptions);
		//this.collection.createIndex(Indexes.ascending("fechaHistorico"), indexOptions);
	}

}

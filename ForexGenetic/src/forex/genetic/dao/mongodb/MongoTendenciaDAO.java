package forex.genetic.dao.mongodb;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

import forex.genetic.entities.Tendencia;
import forex.genetic.exception.GeneticDAOException;

/**
 *
 * @author ricardorq85
 */
public class MongoTendenciaDAO extends MongoGeneticDAO<Tendencia> {

	public MongoTendenciaDAO() throws GeneticDAOException {
		this(true);
	}

	public MongoTendenciaDAO(boolean configure) throws GeneticDAOException {
		super("tendencia", configure);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		this.collection.createIndex(Indexes.ascending("fechaBase", "idIndividuo"), indexOptions);
		this.collection.createIndex(Indexes.ascending("fechaTendencia"));
	}
}

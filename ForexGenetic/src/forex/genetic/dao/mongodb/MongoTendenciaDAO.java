package forex.genetic.dao.mongodb;

import com.mongodb.client.model.IndexOptions;

import forex.genetic.entities.Tendencia;
import forex.genetic.exception.GeneticDAOException;

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

	@Override
	public boolean exists(Tendencia obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void insert(Tendencia obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Tendencia obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

}

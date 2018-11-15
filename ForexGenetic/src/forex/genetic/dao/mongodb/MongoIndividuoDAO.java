package forex.genetic.dao.mongodb;

import org.bson.Document;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.exception.GeneticDAOException;

public class MongoIndividuoDAO extends MongoGeneticDAO<IndividuoEstrategia> {

	public MongoIndividuoDAO() {
		super("individuo", true);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		this.collection.createIndex(Indexes.ascending("idIndividuo"), indexOptions);
	}

	public IndividuoEstrategia consultarById(String idIndividuo) {
		IndividuoEstrategia obj = null;
		MongoCursor<Document> cursor = this.collection.find(Filters.eq("idIndividuo", idIndividuo)).iterator();
		if (cursor.hasNext()) {
			obj = mapper.helpOne(cursor.next());
		}
		return obj;
	}

	@Override
	public boolean exists(IndividuoEstrategia obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void insert(IndividuoEstrategia obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(IndividuoEstrategia obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}
}

package forex.genetic.dao.mongodb;

import org.bson.Document;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

import forex.genetic.bo.Parametro;
import forex.genetic.exception.GeneticDAOException;

public class MongoParametroDAO extends MongoGeneticDAO<Parametro> {

	public MongoParametroDAO() {
		super("parametro", true);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		this.collection.createIndex(Indexes.ascending("valor"), indexOptions);
	}

	public Parametro consultarByName(String nombre) {
		Parametro obj = null;
		MongoCursor<Document> cursor = this.collection.find(Filters.eq("nombre", nombre)).iterator();
		if (cursor.hasNext()) {
			obj = mapper.helpOne(cursor.next());
		}
		return obj;
	}

	@Override
	public boolean exists(Parametro obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void insert(Parametro obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Parametro obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}
}

package forex.genetic.dao.mongodb;

import com.mongodb.client.MongoDatabase;

import forex.genetic.exception.GeneticDAOException;

public class MongoIndividuoBorradoDAO extends MongoIndividuoDAO {

	public MongoIndividuoBorradoDAO(MongoDatabase db) throws GeneticDAOException {
		super(db, "individuoBorrado", true);
	}
}

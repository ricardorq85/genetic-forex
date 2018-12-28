package forex.genetic.dao.mongodb;

import forex.genetic.exception.GeneticDAOException;

public class MongoIndividuoBorradoDAO extends MongoIndividuoDAO {

	public MongoIndividuoBorradoDAO() throws GeneticDAOException {
		super("individuoBorrado", true);
	}
}

package forex.genetic.dao.mongodb;

import com.mongodb.client.MongoDatabase;

/**
 *
 * @author ricardorq85
 */
public class MongoTendenciaUltimosDatosDAO extends MongoTendenciaDAO {

	public MongoTendenciaUltimosDatosDAO(MongoDatabase db) {
		this(db, true);
	}

	public MongoTendenciaUltimosDatosDAO(MongoDatabase db, boolean configure) {
		super(db, "tendenciaUltimosDatos", configure);
	}
}

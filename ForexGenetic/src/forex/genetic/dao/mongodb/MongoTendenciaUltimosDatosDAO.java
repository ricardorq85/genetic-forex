package forex.genetic.dao.mongodb;

/**
 *
 * @author ricardorq85
 */
public class MongoTendenciaUltimosDatosDAO extends MongoTendenciaDAO {

	public MongoTendenciaUltimosDatosDAO() {
		this(true);
	}

	public MongoTendenciaUltimosDatosDAO(boolean configure) {
		super("tendenciaUltimosDatos", configure);
	}
}

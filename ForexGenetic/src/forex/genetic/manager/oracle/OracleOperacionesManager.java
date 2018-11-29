package forex.genetic.manager.oracle;

import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.manager.OperacionesManager;
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public class OracleOperacionesManager extends OperacionesManager {

	/**
	 *
	 */
	public OracleOperacionesManager() {
	}

	protected DataClient getDataClient() throws GeneticDAOException {
		return DriverDBFactory.createDataClient("oracle");
	}
}

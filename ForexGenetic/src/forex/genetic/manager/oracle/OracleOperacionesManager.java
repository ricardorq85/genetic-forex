package forex.genetic.manager.oracle;

import forex.genetic.manager.OperacionesManager;
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public class OracleOperacionesManager extends OperacionesManager {

	public OracleOperacionesManager(DataClient dc) {
		super(dc);
	}

}

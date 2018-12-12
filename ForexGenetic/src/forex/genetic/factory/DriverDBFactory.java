package forex.genetic.factory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.IGeneticManager;
import forex.genetic.manager.mongodb.MongoProcesoIndividuoManager;
import forex.genetic.manager.oracle.OracleProcesoIndividuoManager;
import forex.genetic.tendencia.manager.mongo.MongoTendenciaManager;
import forex.genetic.tendencia.manager.oracle.OracleTendenciaBuySellManager;
import forex.genetic.util.jdbc.DataClient;
import forex.genetic.util.jdbc.JDBCUtil;
import forex.genetic.util.jdbc.OracleDataClient;
import forex.genetic.util.jdbc.mongodb.ConnectionMongoDB;

public class DriverDBFactory extends GeneticFactory {

	@SuppressWarnings("rawtypes")
	public static List<DataClient> createDataClient() throws GeneticDAOException {
		List<DataClient> dc = new ArrayList<DataClient>(drivers.length);
		for (int i = 0; i < drivers.length; i++) {
			dc.add(createDataClient(drivers[i]));
		}
		return dc;
	}

	@SuppressWarnings("rawtypes")
	public static DataClient createDataClient(String name) throws GeneticDAOException {
		DataClient dc = null;
		if ("oracle".equals(name)) {
			dc = JDBCUtil.getDataClient();
		} else if ("mongodb".equals(name)) {
			dc = ConnectionMongoDB.getMongoDataClient();
		}
		return dc;
	}

	@SuppressWarnings("rawtypes")
	public static DataClient createOracleDataClient(Connection c) throws GeneticDAOException {
		OracleDataClient dc = JDBCUtil.getDataClient(c);
		return dc;
	}

	public static IGeneticManager[] createManager(String entidad) throws GeneticBusinessException, GeneticDAOException {
		IGeneticManager[] instances = new IGeneticManager[drivers.length];
		for (int i = 0; i < drivers.length; i++) {
			if ("oracle".equals(drivers[i])) {
				instances[i] = createOracleManager(entidad);
			} else if ("mongodb".equals(drivers[i])) {
				instances[i] = createMongoManager(entidad);
			}
		}
		return instances;
	}

	private static IGeneticManager createOracleManager(String entidad) throws GeneticBusinessException {
		IGeneticManager instance = null;
		try {
			if ("procesoIndividuo".equals(entidad)) {
				instance = new OracleProcesoIndividuoManager(createDataClient("oracle"));
			} else if ("tendencia".equals(entidad)) {
				instance = new OracleTendenciaBuySellManager(createDataClient("oracle"));
			} else {
				throw new IllegalArgumentException(
						new StringBuilder("Entidad no soportada para crear MANAGER: ").append(entidad).toString());
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
		return instance;
	}

	private static IGeneticManager createMongoManager(String entidad)
			throws GeneticBusinessException, GeneticDAOException {
		IGeneticManager instance = null;
		if ("procesoIndividuo".equals(entidad)) {
			instance = new MongoProcesoIndividuoManager(createDataClient("mongodb"));
		} else if ("tendencia".equals(entidad)) {
			instance = new MongoTendenciaManager(createDataClient("mongodb"));
		} else {
			throw new IllegalArgumentException(
					new StringBuilder("Entidad no soportada para crear MANAGER: ").append(entidad).toString());
		}
		return instance;
	}
}

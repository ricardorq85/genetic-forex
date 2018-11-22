package forex.genetic.factory;

import java.sql.SQLException;

import forex.genetic.dao.IGeneticDAO;
import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.dao.mongodb.MongoGeneticDAO;
import forex.genetic.dao.mongodb.MongoParametroDAO;
import forex.genetic.dao.mongodb.MongoTendenciaDAO;
import forex.genetic.dao.oracle.OracleDatoHistoricoDAO;
import forex.genetic.dao.oracle.OracleGeneticDAO;
import forex.genetic.dao.oracle.OracleParametroDAO;
import forex.genetic.dao.oracle.OracleTendenciaDAO;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.IGeneticManager;
import forex.genetic.manager.mongodb.MongoProcesoIndividuoManager;
import forex.genetic.manager.oracle.OracleProcesoIndividuoManager;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.DataClient;
import forex.genetic.util.jdbc.JDBCUtil;
import forex.genetic.util.jdbc.OracleDataClient;
import forex.genetic.util.jdbc.mongodb.ConnectionMongoDB;

public class DriverDBFactory extends GeneticFactory {

	public static DataClient<?>[] createDataClient() throws GeneticDAOException {
		DataClient<?>[] dc = new DataClient[drivers.length];
		for (int i = 0; i < drivers.length; i++) {
			if ("oracle".equals(drivers[i])) {
				dc[i] = JDBCUtil.getDataClient();
			} else if ("mongodb".equals(drivers[i])) {
				dc[i] = ConnectionMongoDB.getMongoDataClient();
			}
		}
		return dc;
	}

	public static IGeneticDAO<? extends Object>[] createDAO(String entidad, DataClient<?>[] dataClient) throws GeneticDAOException {
		IGeneticDAO<?>[] daos = new IGeneticDAO[drivers.length];
		for (int i = 0; i < drivers.length; i++) {
			if ("oracle".equals(drivers[i])) {
				try {
					daos[i] = createOracleDAO(entidad, dataClient[i]);
				} catch (ClassNotFoundException | SQLException e) {
					LogUtil.logTime("Error al crear conexion con BD Oracle. Se continua el proceso con drivers.", 1);
					e.printStackTrace();
				}
			} else if ("mongodb".equals(drivers[i])) {
				daos[i] = createMongoDAO(entidad, dataClient[i]);
			}
		}
		return daos;
	}

	public static IGeneticManager[] createManager(String entidad) throws GeneticBusinessException {
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

	private static IGeneticManager createOracleManager(String entidad) {
		IGeneticManager instance = null;
		if ("procesoIndividuo".equals(entidad)) {
			instance = new OracleProcesoIndividuoManager();
		} else {
			throw new IllegalArgumentException(
					new StringBuilder("Entidad no soportada para crear MANAGER: ").append(entidad).toString());
		}
		return instance;
	}

	private static IGeneticManager createMongoManager(String entidad) throws GeneticBusinessException {
		IGeneticManager instance = null;
		if ("procesoIndividuo".equals(entidad)) {
			instance = new MongoProcesoIndividuoManager();
		} else {
			throw new IllegalArgumentException(
					new StringBuilder("Entidad no soportada para crear MANAGER: ").append(entidad).toString());
		}
		return instance;
	}

	private static OracleGeneticDAO<? extends Object> createOracleDAO(String entidad, DataClient<?> dataClient)
			throws ClassNotFoundException, SQLException {
		OracleGeneticDAO<? extends Object> dao = null;
		OracleDataClient oracleDataClient = (OracleDataClient) dataClient;
		if (dataClient == null) {
			oracleDataClient = new OracleDataClient(JDBCUtil.getConnection());
		}
		if ("datoHistorico".equals(entidad)) {
			dao = new OracleDatoHistoricoDAO(oracleDataClient.getClient());
		} else if ("tendencia".equals(entidad)) {
			dao = new OracleTendenciaDAO(oracleDataClient.getClient());
		} else if ("parametro".equals(entidad)) {
			dao = new OracleParametroDAO(oracleDataClient.getClient());
		} else {
			throw new IllegalArgumentException(
					new StringBuilder("Entidad no soportada para crear DAO: ").append(entidad).toString());
		}
		return dao;
	}

	private static MongoGeneticDAO<? extends Object> createMongoDAO(String entidad, DataClient<?> dataClient) throws GeneticDAOException {
		MongoGeneticDAO<? extends Object> dao = null;
		if ("datoHistorico".equals(entidad)) {
			dao = new MongoDatoHistoricoDAO();
		} else if ("tendencia".equals(entidad)) {
			dao = new MongoTendenciaDAO();
		} else if ("parametro".equals(entidad)) {
			dao = new MongoParametroDAO();
		} else {
			throw new IllegalArgumentException(
					new StringBuilder("Entidad no soportada para crear DAO: ").append(entidad).toString());
		}
		return dao;
	}
}

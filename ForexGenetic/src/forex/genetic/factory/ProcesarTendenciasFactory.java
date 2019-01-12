package forex.genetic.factory;

import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.tendencia.manager.ProcesarTendenciasBuySellManager;
import forex.genetic.tendencia.manager.mongo.MongoProcesarTendenciasGrupalManager;
import forex.genetic.tendencia.manager.oracle.OracleProcesarTendenciasGrupalManager;
import forex.genetic.util.jdbc.DataClient;
import forex.genetic.util.jdbc.OracleDataClient;
import forex.genetic.util.jdbc.mongodb.MongoDataClient;

public final class ProcesarTendenciasFactory {

	public static ProcesarTendenciasBuySellManager createManager() throws GeneticDAOException, GeneticBusinessException {
		DataClient dc = DriverDBFactory.createDataClient("oracle");
		return createManager(dc);
	}

	public static ProcesarTendenciasBuySellManager createManager(DataClient dc) throws GeneticDAOException, GeneticBusinessException {
		ProcesarTendenciasBuySellManager manager = null;
		if (dc instanceof OracleDataClient) {
			manager = new OracleProcesarTendenciasGrupalManager();
			manager.setDataClient(dc);
		} else if (dc instanceof MongoDataClient) {
			manager = new MongoProcesarTendenciasGrupalManager();
			manager.setDataClient(dc);
		} else {
			manager = null;
		}
		return manager;
	}

//	private static final ProcesarTendenciasBuySellManager create(String tipoExportacion) throws ClassNotFoundException,
//			NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
//		Class<?> exporterClass = Class.forName(tipoExportacion);
//		return (ProcesarTendenciasBuySellManager) exporterClass.newInstance();
//	}
}

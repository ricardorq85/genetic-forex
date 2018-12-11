package forex.genetic.factory;

import java.lang.reflect.InvocationTargetException;

import forex.genetic.exception.GeneticDAOException;
import forex.genetic.tendencia.manager.ProcesarTendenciasBuySellManager;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.DataClient;

public final class ProcesarTendenciasFactory {

	public static ProcesarTendenciasBuySellManager createManager() throws GeneticDAOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		DataClient dc = DriverDBFactory.createDataClient("oracle");
		return createManager(dc);
	}

	public static ProcesarTendenciasBuySellManager createManager(DataClient dc) throws GeneticDAOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		String parametroTipoExportacion = dc.getDaoParametro().getValorParametro("TIPO_EXPORTACION_TENDENCIA");
		LogUtil.logTime(parametroTipoExportacion, 1);
		ProcesarTendenciasBuySellManager manager = create(parametroTipoExportacion);
		manager.setDataClient(dc);
		return manager;
	}

	private static final ProcesarTendenciasBuySellManager create(String tipoExportacion) throws ClassNotFoundException,
			NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Class<?> exporterClass = Class.forName(tipoExportacion);
		return (ProcesarTendenciasBuySellManager) exporterClass.newInstance();
	}
}

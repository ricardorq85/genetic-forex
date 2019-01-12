package forex.genetic.factory;

import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.facade.IGeneticFacade;
import forex.genetic.facade.MongoTendenciaProcesoFacade;
import forex.genetic.facade.MultipleDatoHistoricoFacadeImpl;
import forex.genetic.facade.OracleTendenciaProcesoFacade;
import forex.genetic.facade.PoblacionFacade;
import forex.genetic.util.jdbc.DataClient;

public class FacadeFactory extends GeneticFactory {

	public static IGeneticFacade create(String entidad) throws GeneticBusinessException {
		return create(null, entidad);
	}

	public static IGeneticFacade create(DataClient dc, String entidad) throws GeneticBusinessException {
		IGeneticFacade facade = null;
		if ("datoHistorico".equals(entidad)) {
			facade = new MultipleDatoHistoricoFacadeImpl();
		} else if ("individuo".equals(entidad)) {
			facade = new PoblacionFacade();
		} else if ("tendenciaProceso".equals(entidad)) {
			if (DataClient.DriverType.ORACLE.equals(dc.getDriverType())) {
				facade = new OracleTendenciaProcesoFacade(dc);
			} else if (DataClient.DriverType.ORACLE.equals(dc.getDriverType())) {
				facade = new MongoTendenciaProcesoFacade(dc);
			} else {
				throw new IllegalArgumentException(new StringBuilder("DataClient no soportado para crear facade: ")
						.append(dc.getDriverType()).toString());
			}
		} else {
			throw new IllegalArgumentException(
					new StringBuilder("Entidad no soportado para crear facade: ").append(entidad).toString());
		}
		return facade;
	}

}

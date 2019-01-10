package forex.genetic.facade;

import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.util.jdbc.DataClient;

public class OracleTendenciaProcesoFacade extends TendenciaProcesoFacade {

	public OracleTendenciaProcesoFacade(DataClient dc) throws GeneticBusinessException {
		super(dc);
	}

}

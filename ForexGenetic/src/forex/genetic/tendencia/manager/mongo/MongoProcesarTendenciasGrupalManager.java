package forex.genetic.tendencia.manager.mongo;

import java.util.Date;

import forex.genetic.exception.GeneticDAOException;
import forex.genetic.tendencia.manager.ExportarTendenciaManager;
import forex.genetic.tendencia.manager.ProcesarTendenciasGrupalManager;

public class MongoProcesarTendenciasGrupalManager extends ProcesarTendenciasGrupalManager {

	public MongoProcesarTendenciasGrupalManager() throws GeneticDAOException {
		super();
	}

	@Override
	protected ExportarTendenciaManager getExporter(Date fechaBase) {
		return new MongoExportarTendenciaGrupalManager(this.getDataClient(), fechaBase);
	}

}

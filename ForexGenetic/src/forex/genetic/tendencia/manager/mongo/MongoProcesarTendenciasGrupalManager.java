package forex.genetic.tendencia.manager.mongo;

import java.util.Date;

import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.tendencia.manager.ExportarTendenciaManager;
import forex.genetic.tendencia.manager.ProcesarTendenciasGrupalManager;

public class MongoProcesarTendenciasGrupalManager extends ProcesarTendenciasGrupalManager {

	public MongoProcesarTendenciasGrupalManager() throws GeneticBusinessException  {
		super();
	}

	@Override
	protected ExportarTendenciaManager getExporter(Date fechaBase) throws GeneticBusinessException {
		return new MongoExportarTendenciaGrupalManager(this.getDataClient(), fechaBase);
	}

}

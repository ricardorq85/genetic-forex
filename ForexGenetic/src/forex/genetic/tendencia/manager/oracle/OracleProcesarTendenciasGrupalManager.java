package forex.genetic.tendencia.manager.oracle;

import java.util.Date;

import forex.genetic.exception.GeneticDAOException;
import forex.genetic.tendencia.manager.ExportarTendenciaManager;
import forex.genetic.tendencia.manager.ProcesarTendenciasGrupalManager;

public class OracleProcesarTendenciasGrupalManager extends ProcesarTendenciasGrupalManager {

	public OracleProcesarTendenciasGrupalManager() throws ClassNotFoundException, GeneticDAOException {
		super();
	}

	@Override
	protected ExportarTendenciaManager getExporter(Date fechaBase) {
		return new OracleExportarTendenciaGrupalManager(this.getDataClient(), fechaBase);
	}

}
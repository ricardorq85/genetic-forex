package forex.genetic.tendencia.manager;

import java.sql.Connection;

import forex.genetic.dao.TendenciaProcesoFiltroFechaTendenciaFechaBaseDAO;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.tendencia.manager.oracle.OracleExportarTendenciaManager;

public class ExportarTendenciaMaximosMinimosManager extends OracleExportarTendenciaManager {

	public ExportarTendenciaMaximosMinimosManager() throws GeneticBusinessException {
		super(null);
	}

	public ExportarTendenciaMaximosMinimosManager(Connection c) throws GeneticBusinessException {
		super(c);
		super.tendenciaProcesoDAO = new TendenciaProcesoFiltroFechaTendenciaFechaBaseDAO(c);
	}

}

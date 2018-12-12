package forex.genetic.tendencia.manager;

import java.sql.Connection;

import forex.genetic.dao.TendenciaProcesoFiltroFechaTendenciaFechaCierreDAO;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.tendencia.manager.oracle.OracleExportarTendenciaManager;

public class ExportarTendenciaFiltroFechaTendenciaFechaCierreManager extends OracleExportarTendenciaManager {

	public ExportarTendenciaFiltroFechaTendenciaFechaCierreManager() throws GeneticBusinessException {
		super(null);
	}

	public ExportarTendenciaFiltroFechaTendenciaFechaCierreManager(Connection c) throws GeneticBusinessException {
		super();
		super.tendenciaProcesoDAO = new TendenciaProcesoFiltroFechaTendenciaFechaCierreDAO(c);
	}
}

package forex.genetic.tendencia.manager;

import java.sql.Connection;

import forex.genetic.dao.TendenciaProcesoFiltroFechaTendenciaFechaBaseDAO;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.tendencia.manager.oracle.OracleExportarTendenciaManager;

public class ExportarTendenciaFiltroFechaTendenciaFechaBaseManager extends OracleExportarTendenciaManager {

	public ExportarTendenciaFiltroFechaTendenciaFechaBaseManager() throws GeneticBusinessException {
		this(null);
	}

	public ExportarTendenciaFiltroFechaTendenciaFechaBaseManager(Connection c) throws GeneticBusinessException {
		super(c);
		super.tendenciaProcesoDAO = new TendenciaProcesoFiltroFechaTendenciaFechaBaseDAO(c);
	}

	@Override
	protected void procesarRegresion() throws GeneticBusinessException {
		super.procesarRegresion();
	}

}

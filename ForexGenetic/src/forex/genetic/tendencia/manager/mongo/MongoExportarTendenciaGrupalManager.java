package forex.genetic.tendencia.manager.mongo;

import java.util.Date;
import java.util.List;

import forex.genetic.dao.mongodb.MongoTendenciaDAO;
import forex.genetic.dao.mongodb.MongoTendenciaUltimosDatosDAO;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.tendencia.manager.ExportarTendenciaGrupalManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.DataClient;

public class MongoExportarTendenciaGrupalManager extends ExportarTendenciaGrupalManager {

	private MongoTendenciaDAO tendenciaProcesoUltimosDatosDAO;
	private MongoTendenciaDAO tendenciaProcesoCompletaDAO;

	public MongoExportarTendenciaGrupalManager(DataClient dc, Date fechaBase) {
		super(dc);
		if (DateUtil.cumpleFechaParaTendenciaUltimosDatos(fechaBase)) {
			this.tendenciaProcesoUltimosDatosDAO = new MongoTendenciaUltimosDatosDAO();
			this.tendenciaProcesoCompletaDAO = new MongoTendenciaDAO();
		} else {
			// TODO rrojasq Hacer filtrada
			this.tendenciaProcesoUltimosDatosDAO = new MongoTendenciaUltimosDatosDAO();
			this.tendenciaProcesoCompletaDAO = new MongoTendenciaDAO();
		}
	}

	@Override
	protected void procesarRegresion() throws GeneticBusinessException {
		this.procesarRegresionParaCalculoJava();
	}

	@Override
	protected List<TendenciaParaOperar> consultarTendenciasSinFiltrar() {
		return this.tendenciaProcesoCompletaDAO.consultarTendencias(procesoTendencia);
	}

	@Override
	protected List<TendenciaParaOperar> consultarTendenciasFiltradas() {
		return this.tendenciaProcesoUltimosDatosDAO.consultarTendencias(procesoTendencia);
	}

	@Override
	protected List<TendenciaParaOperar> consultarTendencias() {
		return tendenciaProcesoUltimosDatosDAO.consultarTendencias(procesoTendencia);
	}
}

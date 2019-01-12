package forex.genetic.tendencia.manager;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.entities.TendenciaParaOperarMaxMin;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.IndividuoManager;
import forex.genetic.tendencia.manager.oracle.OracleProcesarTendenciasGrupalManager;
import forex.genetic.util.jdbc.DataClient;

public class ExportarTendenciaParaOperarManager {

	private DataClient dataClient;
	private Date fechaInicio;
	private static String sourceEstrategiasPath;

	public ExportarTendenciaParaOperarManager(DataClient dc) throws GeneticBusinessException {
		this.dataClient = dc;
		try {
			ExportarTendenciaParaOperarManager.sourceEstrategiasPath = dataClient.getDaoParametro()
					.getValorParametro("SOURCE_ESTRATEGIAS_PATH");
			this.fechaInicio = dataClient.getDaoParametro().getDateValorParametro("FECHA_INICIO_EXPORTACION");
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(e);
		}
	}

	public void exportar() throws SQLException, ClassNotFoundException, IOException, GeneticBusinessException {
		String indId = IndividuoManager.nextId();

		List<? extends TendenciaParaOperar> list;
		try {
			list = this.dataClient.getDaoTendenciaParaOperar().consultar(this.fechaInicio);
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(e);
		}
		this.exportar(list, indId);

//		List<? extends TendenciaParaOperar> listMongo = this.mongoTendenciaParaOperarDAO
		// .consultarTendenciasParaOperar(this.fechaInicio);
		// this.exportar(listMongo, "Mongo_" + indId);
	}

	private void exportar(List<? extends TendenciaParaOperar> list, String name)
			throws IOException, ClassNotFoundException, GeneticBusinessException {
		ProcesarTendenciasGrupalManager grupalManager = new OracleProcesarTendenciasGrupalManager();
		grupalManager.setDataClient(dataClient);
		grupalManager.setTendenciasResultado((List<TendenciaParaOperarMaxMin>) list);

		String fileName = sourceEstrategiasPath + "\\Tendencia" + name + ".csv";
		Path filePath = FileSystems.getDefault().getPath(fileName);

		grupalManager.export(filePath);
	}

}

package forex.genetic.tendencia.manager;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.entities.TendenciaParaOperarMaxMin;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.IndividuoManager;
import forex.genetic.util.jdbc.DataClient;

public class ExportarTendenciaParaOperarManager {

	private DataClient dataClient;
	private Date fechaInicio;
	private static String sourceEstrategiasPath;

	public ExportarTendenciaParaOperarManager(DataClient dc)
			throws SQLException, ClassNotFoundException, GeneticDAOException {
		this.dataClient = dc;
		ExportarTendenciaParaOperarManager.sourceEstrategiasPath = dataClient.getDaoParametro()
				.getValorParametro("SOURCE_ESTRATEGIAS_PATH");
		this.fechaInicio = dataClient.getDaoParametro().getDateValorParametro("FECHA_INICIO_EXPORTACION");
	}

	public void exportar() throws SQLException, ClassNotFoundException, IOException, GeneticDAOException {
		String indId = IndividuoManager.nextId();

		List<? extends TendenciaParaOperar> list = this.dataClient.getDaoTendenciaParaOperar().consultar(this.fechaInicio);
		this.exportar(list, indId);

//		List<? extends TendenciaParaOperar> listMongo = this.mongoTendenciaParaOperarDAO
		// .consultarTendenciasParaOperar(this.fechaInicio);
		// this.exportar(listMongo, "Mongo_" + indId);
	}

	private void exportar(List<? extends TendenciaParaOperar> list, String name)
			throws IOException, ClassNotFoundException, GeneticDAOException {
		ProcesarTendenciasGrupalManager grupalManager = new ProcesarTendenciasGrupalManager();
		grupalManager.setDataClient(dataClient);
		grupalManager.setTendenciasResultado(list);

		String fileName = sourceEstrategiasPath + "\\Tendencia" + name + ".csv";
		Path filePath = FileSystems.getDefault().getPath(fileName);

		grupalManager.export(filePath);
	}

}

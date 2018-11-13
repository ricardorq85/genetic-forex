package forex.genetic.tendencia.manager;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.ParametroDAO;
import forex.genetic.dao.TendenciaParaOperarDAO;
import forex.genetic.dao.mongodb.MongoTendenciaParaOperarDAO;
import forex.genetic.entities.TendenciaParaOperarMaxMin;
import forex.genetic.manager.IndividuoManager;
import forex.genetic.util.jdbc.JDBCUtil;

public class ExportarTendenciaParaOperarManager {
	private Connection connection = null;

	private MongoTendenciaParaOperarDAO mongoTendenciaParaOperarDAO;

	private TendenciaParaOperarDAO tendenciaParaOperarDAO;
	private ParametroDAO parametroDAO;

	private Date fechaInicio;
	private static String sourceEstrategiasPath;

	public ExportarTendenciaParaOperarManager() throws SQLException, ClassNotFoundException {
		connection = JDBCUtil.getConnection();
		this.tendenciaParaOperarDAO = new TendenciaParaOperarDAO(connection);
		this.mongoTendenciaParaOperarDAO = new MongoTendenciaParaOperarDAO();
		this.parametroDAO = new ParametroDAO(connection);
		ExportarTendenciaParaOperarManager.sourceEstrategiasPath = parametroDAO
				.getValorParametro("SOURCE_ESTRATEGIAS_PATH");
		this.fechaInicio = parametroDAO.getDateValorParametro("FECHA_INICIO_EXPORTACION");
	}

	public void exportar() throws SQLException, ClassNotFoundException, IOException {
		String indId = IndividuoManager.nextId();

		List<TendenciaParaOperarMaxMin> list = this.tendenciaParaOperarDAO
				.consultarTendenciasParaOperar(this.fechaInicio);
		this.exportar(list, indId);

//		List<? extends TendenciaParaOperar> listMongo = this.mongoTendenciaParaOperarDAO
	//			.consultarTendenciasParaOperar(this.fechaInicio);
		//this.exportar(listMongo, "Mongo_" + indId);
	}

	private void exportar(List<TendenciaParaOperarMaxMin> list, String name)
			throws IOException, ClassNotFoundException, SQLException {
		ProcesarTendenciasGrupalManager grupalManager = new ProcesarTendenciasGrupalManager();
		grupalManager.setTendenciasResultado(list);

		String fileName = sourceEstrategiasPath + "\\Tendencia" + name + ".csv";
		Path filePath = FileSystems.getDefault().getPath(fileName);

		grupalManager.export(filePath);

	}

}

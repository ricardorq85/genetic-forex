package forex.genetic.tendencia.manager;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import forex.genetic.dao.ParametroDAO;
import forex.genetic.dao.TendenciaParaOperarDAO;
import forex.genetic.entities.TendenciaParaOperarMaxMin;
import forex.genetic.manager.IndividuoManager;
import forex.genetic.util.jdbc.JDBCUtil;

public class ExportarTendenciaParaOperarManager {
	private Connection connection = null;

	private TendenciaParaOperarDAO tendenciaParaOperarDAO;
	private ParametroDAO parametroDAO;
	private static String sourceEstrategiasPath;

	public ExportarTendenciaParaOperarManager() throws SQLException, ClassNotFoundException {
		connection = JDBCUtil.getConnection();
		this.tendenciaParaOperarDAO = new TendenciaParaOperarDAO(connection);
		this.parametroDAO = new ParametroDAO(connection);
		ExportarTendenciaParaOperarManager.sourceEstrategiasPath = parametroDAO
				.getValorParametro("SOURCE_ESTRATEGIAS_PATH");
	}

	public void exportar() throws SQLException, ClassNotFoundException, IOException {
		List<TendenciaParaOperarMaxMin> list = this.tendenciaParaOperarDAO.consultarTendenciasParaOperar();
		ProcesarTendenciasGrupalManager grupalManager = new ProcesarTendenciasGrupalManager();
		grupalManager.setTendenciasResultado(list);

		String fileName = sourceEstrategiasPath + "\\Tendencia" + IndividuoManager.nextId() + ".csv";
		Path filePath = FileSystems.getDefault().getPath(fileName);

		grupalManager.export(filePath);
	}

}

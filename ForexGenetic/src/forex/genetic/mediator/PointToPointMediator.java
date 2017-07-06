package forex.genetic.mediator;

import static forex.genetic.util.LogUtil.logTime;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.ParametroDAO;
import forex.genetic.dao.TendenciaDAO;
import forex.genetic.delegate.GeneticDelegateBD;
import forex.genetic.delegate.PoblacionDelegate;
import forex.genetic.exception.GeneticException;
import forex.genetic.factory.ProcesarTendenciasFactory;
import forex.genetic.manager.IndividuoManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.manager.io.CopyFileVisitor;
import forex.genetic.tendencia.manager.ProcesarTendenciasBuySellManager;
import forex.genetic.tendencia.manager.TendenciaBuySellManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.FileUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

public class PointToPointMediator extends GeneticMediator {

	private int count = 1;
	private Connection connection;
	private Date fechaHistoricaMaximaAnterior, fechaHistoricaMaximaNueva, ultimaFechaBaseTendencia;
	private DatoHistoricoDAO datoHistoricoDAO;
	private TendenciaDAO tendenciaDAO;
	private ParametroDAO parametroDAO;
	private static String sourceExportedHistoryDataPath;// =
														// "c:\\Users\\USER\\AppData\\Roaming\\MetaQuotes\\Terminal\\Common\\Files\\export\\exported";
	private static String processedExportedHistoryDataPath;// =
															// "c:\\Users\\USER\\AppData\\Roaming\\MetaQuotes\\Terminal\\Common\\Files\\export\\processed";
	private static String exportedPropertyFileName;// =
													// "c:\\Users\\USER\\AppData\\Roaming\\MetaQuotes\\Terminal\\Common\\Files\\export\\Export.properties";
	private static String sourceEstrategiasPath;// =
												// "c:\\Users\\USER\\AppData\\Roaming\\MetaQuotes\\Terminal\\Common\\Files\\estrategias\\live";

	@Override
	public void init() throws ClassNotFoundException, SQLException {
		this.connection = JDBCUtil.getConnection();
		this.datoHistoricoDAO = new DatoHistoricoDAO(connection);
		this.tendenciaDAO = new TendenciaDAO(connection);
		this.parametroDAO = new ParametroDAO(connection);

		PointToPointMediator.sourceExportedHistoryDataPath = parametroDAO
				.getValorParametro("SOURCE_EXPORTED_HISTORY_DATA_PATH");
		PointToPointMediator.processedExportedHistoryDataPath = parametroDAO
				.getValorParametro("PROCESSED_EXPORTED_HISTORY_DATA_PATH");
		PointToPointMediator.exportedPropertyFileName = parametroDAO.getValorParametro("EXPORTED_PROPERTY_FILE_NAME");
		PointToPointMediator.sourceEstrategiasPath = parametroDAO.getValorParametro("SOURCE_ESTRATEGIAS_PATH");
	}

	@Override
	public void start()
			throws SQLException, IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException, ParseException, GeneticException {
		while (true) {
			this.fechaHistoricaMaximaAnterior = datoHistoricoDAO.getFechaHistoricaMaxima();
			int imported = this.importarDatosHistoricos();
			this.fechaHistoricaMaximaNueva = datoHistoricoDAO.getFechaHistoricaMaxima();
			this.exportarDatosHistoricos();
			this.setUltimaFechaTendencia(count);
			LogUtil.logTime("ultimaFechaBaseTendencia=" + DateUtil.getDateString(this.ultimaFechaBaseTendencia)
					+ ",fechaHistoricaMaximaAnterior=" + DateUtil.getDateString(this.fechaHistoricaMaximaAnterior)
					+ ",fechaHistoricaMaximaNueva=" + DateUtil.getDateString(this.fechaHistoricaMaximaNueva), 1);
			this.procesarIndividuos();
			this.procesarTendencias();
			this.exportarIndividuos();
			this.crearOrdenes();
			if (imported == 0) {
				count++;
			} else {
				count = 1;
			}
		}
	}

	protected void setUltimaFechaTendencia(int count) throws SQLException {
		this.ultimaFechaBaseTendencia = tendenciaDAO.maxFechaBaseTendencia();
		if (fechaHistoricaMaximaNueva.equals(ultimaFechaBaseTendencia)) {
			int minutos = (int) (-(1440 * 0.5 * count));
			this.ultimaFechaBaseTendencia = DateUtil.adicionarMinutos(fechaHistoricaMaximaNueva, minutos);
		}
	}

	private void exportarDatosHistoricos() throws SQLException, IOException {
		logTime("Init Exportar Datos Historicos", 1);
		Date fechaExport = DateUtil.adicionarMinutos(this.fechaHistoricaMaximaNueva, 1);
		String fechaExportString = DateUtil.getDateString("yyyy.MM.dd HH:mm", fechaExport);
		FileUtil.save(exportedPropertyFileName, "FECHA_INICIO=" + fechaExportString + ",FECHA_FIN=");
		logTime("End Exportar Datos Historicos=" + fechaExportString, 1);
	}

	private int importarDatosHistoricos() throws IOException, SQLException {
		logTime("Init Importar Datos Historicos", 1);
		List<Path> files = this.copiarArchivosARuta();
		this.ejecutarCarga(files);
		logTime("End Importar Datos Historicos. fechaMaximaNueva=" + DateUtil.getDateString(fechaHistoricaMaximaNueva),
				1);
		return files.size();
	}

	private String getFileId(String fileName) {
		String fileId = null;
		String[] spt = fileName.split("-");
		fileId = spt[1];
		return fileId;
	}

	private void ejecutarCarga(List<Path> files) throws FileNotFoundException, IOException {
		for (Path file : files) {
			this.actualizarProperty(file);
			PoblacionDelegate delegate = new PoblacionDelegate();
			logTime("Init Insert Datos Historicos", 1);
			delegate.cargarDatosHistoricos();
			logTime("End Insert Datos Historicos", 1);
		}
	}

	private void actualizarProperty(Path file) {
		String fileId = getFileId(file.getFileName().toFile().getName());
		PropertiesManager.setFileId(fileId);
	}

	private List<Path> copiarArchivosARuta() throws IOException {
		String targetPathName = System.getProperty("user.dir") + "\\files\\";
		Path sourcePath = FileSystems.getDefault().getPath(sourceExportedHistoryDataPath);
		CopyFileVisitor fileVisitor = new CopyFileVisitor(sourceExportedHistoryDataPath, targetPathName,
				processedExportedHistoryDataPath);
		Files.walkFileTree(sourcePath, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE, fileVisitor);
		return fileVisitor.getCopiedFiles();
	}

	private void procesarIndividuos() throws FileNotFoundException {
		logTime("Init Procesar Individuos", 1);
		GeneticDelegateBD delegate = new GeneticDelegateBD();
		delegate.process(true);
		logTime("End Procesar Individuos", 1);
	}

	private void procesarTendencias() throws SQLException, ClassNotFoundException {
		logTime("Init Procesar Tendencias", 1);
		ParametroDAO parametroDAO = new ParametroDAO(connection);
		int parametroStepTendencia = parametroDAO.getIntValorParametro("STEP_TENDENCIA");
		int parametroFilasTendencia = parametroDAO.getIntValorParametro("INDIVIDUOS_X_TENDENCIA");
		Date fechaBaseFinal = fechaHistoricaMaximaNueva;
		TendenciaBuySellManager tendenciaManager = new TendenciaBuySellManager();
		if (count == 1) {
			tendenciaManager.calcularTendencias(fechaBaseFinal, parametroFilasTendencia * 2);
		}
		int diasDiferencia = (int) (DateUtil.calcularDuracionMillis(ultimaFechaBaseTendencia, fechaBaseFinal) / 1000 / 60
				/ 60 / 24) + 1;
		int factorStep = (int) ((DateUtil.calcularDuracionMillis(ultimaFechaBaseTendencia, fechaBaseFinal)) / 1000 / 60
				/ diasDiferencia / count);
		parametroStepTendencia = Math.max(factorStep, parametroStepTendencia);
		parametroFilasTendencia = Math.max((1440 / 2000 / diasDiferencia / count), parametroFilasTendencia);
		while (fechaBaseFinal.after(ultimaFechaBaseTendencia)) {
			fechaBaseFinal = DateUtil.adicionarMinutos(fechaBaseFinal, -1);
			Date fechaBaseInicial = DateUtil.adicionarMinutos(fechaBaseFinal, -parametroStepTendencia);
			LogUtil.logTime("Fecha base inicial=" + DateUtil.getDateString(fechaBaseInicial) + ", Fecha base final="
					+ DateUtil.getDateString(fechaBaseFinal), 1);
			tendenciaManager.calcularTendencias(fechaBaseInicial, fechaBaseFinal, parametroFilasTendencia);
			fechaBaseFinal = fechaBaseInicial;
		}
		logTime("End Procesar Tendencias", 1);
	}

	private void exportarIndividuos()
			throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException,
			InvocationTargetException, SQLException, ParseException, GeneticException, IOException {
		logTime("Init Exportar Individuos", 1);
		boolean existNewData = this.existenNuevosDatosHistoricos();
		if (existNewData) {
			String fileName = sourceEstrategiasPath + "\\Tendencia" + IndividuoManager.nextId() + ".csv";
			Path filePath = FileSystems.getDefault().getPath(fileName);
			ProcesarTendenciasBuySellManager manager = ProcesarTendenciasFactory.createManager();
			manager.setParametroFechaInicio(ultimaFechaBaseTendencia);
			manager.setParametroFechaFin(fechaHistoricaMaximaNueva);
			manager.procesarTendencias();
			manager.export(filePath);
		} else {
			logTime("No existen nuevos datos. No se procesara la exportacion", 1);
		}
		logTime("End Exportar Individuos", 1);
	}

	private boolean existenNuevosDatosHistoricos() throws IOException {
		Path path = FileSystems.getDefault().getPath(PointToPointMediator.sourceExportedHistoryDataPath);
		long countFile = Files.list(path).count();
		return (countFile > 0);
	}

	private void crearOrdenes() {

	}

}

package forex.genetic.mediator;

import static forex.genetic.util.LogUtil.logTime;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.IDatoHistoricoDAO;
import forex.genetic.dao.ITendenciaDAO;
import forex.genetic.dao.ParametroDAO;
import forex.genetic.dao.mongodb.MongoGeneticDAO;
import forex.genetic.delegate.GeneticDelegateBD;
import forex.genetic.delegate.PoblacionDelegate;
import forex.genetic.entities.Tendencia;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.exception.GeneticException;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.factory.ProcesarTendenciasFactory;
import forex.genetic.manager.IndividuoManager;
import forex.genetic.manager.IndividuoXIndicadorManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.tendencia.manager.ProcesarTendenciasBuySellManager;
import forex.genetic.tendencia.manager.TendenciaBuySellManager;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;
import forex.genetic.util.FileUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.DataClient;
import forex.genetic.util.jdbc.JDBCUtil;

public class MultiplePointToPointMediator extends PointToPointMediator {

	private int count = 1;
	private Connection connection;
	private Date fechaHistoricaMaximaAnterior, fechaHistoricaMaximaNueva, ultimaFechaBaseTendencia;
	private IDatoHistoricoDAO[] daosDatoHistorico;
	private ITendenciaDAO[] daosTendencia;
//	private ParametroGeneticDAO[] daosParametro;
	private MongoGeneticDAO<Tendencia> tendenciaDAO;

	@Override
	public void init() throws GeneticDAOException {
		DataClient<?>[] dataClients = DriverDBFactory.createDataClient();
		this.daosDatoHistorico = (IDatoHistoricoDAO[]) DriverDBFactory.createDAO("datoHistorico", dataClients);
		this.daosTendencia = (ITendenciaDAO[]) DriverDBFactory.createDAO("tendencia", dataClients);
		// this.daosParametro = (IGeneticDAO<Parametro>[])
		// DriverDBFactory.createDAO("parametro", dataClients);

//		sourceExportedHistoryDataPath = parametroDAO.consultarByName("SOURCE_EXPORTED_HISTORY_DATA_PATH")
		// .getParametroString();
		// processedExportedHistoryDataPath =
		// parametroDAO.consultarByName("PROCESSED_EXPORTED_HISTORY_DATA_PATH")
//				.getParametroString();
		// exportedPropertyFileName =
		// parametroDAO.consultarByName("EXPORTED_PROPERTY_FILE_NAME").getParametroString();
		// sourceEstrategiasPath =
		// parametroDAO.consultarByName("SOURCE_ESTRATEGIAS_PATH").getParametroString();
	}

	@Override
	public void start() throws IOException, SQLException {
		while (true) {
			this.fechaHistoricaMaximaAnterior = daosDatoHistorico[i].getFechaHistoricaMaxima();
			int imported = importarDatosHistoricos();
			this.fechaHistoricaMaximaNueva = daosDatoHistorico[i].getFechaHistoricaMaxima();
			this.exportarDatosHistoricos();
			this.setUltimaFechaTendencia(count);
			LogUtil.logTime("ultimaFechaBaseTendencia=" + DateUtil.getDateString(this.ultimaFechaBaseTendencia)
					+ ",fechaHistoricaMaximaAnterior=" + DateUtil.getDateString(this.fechaHistoricaMaximaAnterior)
					+ ",fechaHistoricaMaximaNueva=" + DateUtil.getDateString(this.fechaHistoricaMaximaNueva) + ",count="
					+ count, 1);
			this.procesarIndividuos();
			this.procesarTendencias();
			this.exportarIndividuos();
			this.crearNuevosIndividuos();
			if (imported == 0) {
				count++;
			} else {
				count = 1;
			}
		}
	}

	public int importarDatosHistoricos() throws GeneticDAOException, IOException {
		logTime("Init Importar Datos Historicos", 1);
		List<Path> files = copiarArchivosARuta();
		ejecutarCarga(files);
		logTime("End Importar Datos Historicos. fechaMaximaNueva=" + DateUtil.getDateString(fechaHistoricaMaximaNueva),
				1);
		return files.size();
	}

	protected void setUltimaFechaTendencia(int count) throws SQLException {
		this.ultimaFechaBaseTendencia = tendenciaDAO.maxFechaBaseTendencia();
		if ((this.ultimaFechaBaseTendencia == null) || (fechaHistoricaMaximaNueva.equals(ultimaFechaBaseTendencia))) {
			int minutos = (int) (-(1440 * 0.7 * count + calcularFactorCount()));
			this.ultimaFechaBaseTendencia = DateUtil.adicionarMinutos(fechaHistoricaMaximaNueva, minutos);
		}
	}

	private int calcularFactorCount() {
		return (count + 1);
	}

	private void exportarDatosHistoricos() throws SQLException, IOException {
		logTime("Init Exportar Datos Historicos", 1);
		Date fechaExport = DateUtil.adicionarMinutos(this.fechaHistoricaMaximaNueva, 1);
		String fechaExportString = DateUtil.getDateString("yyyy.MM.dd HH:mm", fechaExport);
		FileUtil.save(exportedPropertyFileName, "FECHA_INICIO=" + fechaExportString + ",FECHA_FIN=");
		logTime("End Exportar Datos Historicos=" + fechaExportString, 1);
	}

	private String[] getFileParameters(String fileName) {
		String[] spt = fileName.split("-");
		return spt;
	}

	protected void ejecutarCarga(List<Path> files) throws GeneticDAOException {
		for (Path file : files) {
			this.actualizarProperty(file);
			PoblacionDelegate delegate = new PoblacionDelegate();
			logTime("Init Insert Datos Historicos", 1);
			delegate.cargarDatosHistoricos();
			logTime("End Insert Datos Historicos", 1);
		}
	}

	private void actualizarProperty(Path file) {
		String fileName = file.getFileName().toFile().getName();
		logTime("File:" + fileName, 1);
		String[] fileParameters = getFileParameters(fileName);
		String fileId = fileParameters[1];
		PropertiesManager.setFileId(fileId);
		String fileNumber = fileParameters[2].split("\\.")[0];
		PropertiesManager.setProperty(Constants.INITIAL_POBLACION, fileNumber);
		PropertiesManager.setProperty(Constants.END_POBLACION, fileNumber);
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
			tendenciaManager.calcularTendencias(fechaBaseFinal, parametroFilasTendencia / 2);
		}
		long durMillis = DateUtil.calcularDuracionMillis(ultimaFechaBaseTendencia, fechaBaseFinal);
		int diasDiferencia = (int) ((durMillis / (1000 * 60 * 60 * 24)) + 1);
		int minutosFactorStep = (int) ((diasDiferencia / 6) * 24 * 60);
		parametroStepTendencia = Math.max(minutosFactorStep, parametroStepTendencia);
		parametroFilasTendencia = Math.max((1440 / 2000 / (diasDiferencia / count + 1)), parametroFilasTendencia);
		LogUtil.logTime("ultimaFechaBaseTendencia:" + DateUtil.getDateString(ultimaFechaBaseTendencia), 3);
		LogUtil.logTime("fechaBaseFinal:" + DateUtil.getDateString(fechaBaseFinal), 3);
		LogUtil.logTime("durMillis:" + durMillis, 3);
		LogUtil.logTime("count:" + count, 3);
		LogUtil.logTime("diasDiferencia:" + diasDiferencia, 3);
		LogUtil.logTime("factorStep:" + minutosFactorStep, 3);
		LogUtil.logTime("parametroStepTendencia:" + parametroStepTendencia, 1);
		LogUtil.logTime("parametroFilasTendencia:" + parametroFilasTendencia, 1);
		while (fechaBaseFinal.after(ultimaFechaBaseTendencia)) {
			fechaBaseFinal = DateUtil.adicionarMinutos(fechaBaseFinal, -1 * (calcularFactorCount()));
			int currentStep = -(parametroStepTendencia - count);
			Date fechaBaseInicial = DateUtil.adicionarMinutos(fechaBaseFinal, currentStep);
			// LogUtil.logEnter(1);
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
		// this.refrescarDatosTendencia();
		logTime("Init Exportar Individuos", 1);
		boolean existNewData = this.existenNuevosDatosHistoricos();
		if (existNewData) {
			String fileName = sourceEstrategiasPath + "\\Tendencia" + IndividuoManager.nextId() + ".csv";
			Path filePath = FileSystems.getDefault().getPath(fileName);
			ProcesarTendenciasBuySellManager manager = ProcesarTendenciasFactory.createManager();
			manager.setParametroFechaInicio(ultimaFechaBaseTendencia);
			manager.setParametroFechaFin(fechaHistoricaMaximaNueva);
			ExportThread exportThread = new ExportThread(filePath, manager);
			logTime("Lanzando exportacion", 1);
			exportThread.runExport();
			// logTime("Lanzando hilo para exportacion", 1);
			// exportThread.start();
			// manager.procesarTendencias();
			// manager.export(filePath);
		} else {
			logTime("No existen nuevos datos. No se procesara la exportacion", 1);
		}
		logTime("End Exportar Individuos", 1);
	}

	private void refrescarDatosTendencia() throws SQLException {
		String mv = "TENDENCIA_ULTIMOMES";
		logTime("Refrescando vista materializada: " + mv, 1);
		JDBCUtil.refreshMaterializedView(this.connection, mv, "C");
	}

	private boolean existenNuevosDatosHistoricos() throws IOException {
		Path path = FileSystems.getDefault().getPath(sourceExportedHistoryDataPath);
		long countFile = Files.list(path).count();
		return (countFile > 0);
	}

	private void crearNuevosIndividuos() throws ClassNotFoundException, SQLException {
		logTime("Init Crear individuos x indicador", 1);
		IndividuoXIndicadorManager manager = new IndividuoXIndicadorManager(ultimaFechaBaseTendencia,
				fechaHistoricaMaximaNueva, 12);
		manager.crearIndividuos();
		logTime("End Crear individuos x indicador", 1);
	}

	class ExportThread extends Thread {
		Path path;
		ProcesarTendenciasBuySellManager manager;

		ExportThread(Path path, ProcesarTendenciasBuySellManager manager) {
			this.path = path;
			this.manager = manager;
		}

		public void run() {
			try {
				runExport();
			} catch (IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException
					| IllegalAccessException | InvocationTargetException | SQLException | ParseException
					| GeneticException e) {
				e.printStackTrace();
			}
		}

		public void runExport()
				throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException,
				InvocationTargetException, SQLException, ParseException, GeneticException, IOException {
			manager.procesarTendencias();
			manager.export(path);
		}
	}

}

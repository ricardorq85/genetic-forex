package forex.genetic.mediator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import forex.genetic.dao.IDatoHistoricoDAO;
import forex.genetic.dao.IParametroDAO;
import forex.genetic.dao.ITendenciaDAO;
import forex.genetic.dao.mongodb.MongoGeneticDAO;
import forex.genetic.entities.Tendencia;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.exception.GeneticException;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.DataClient;

public class MultiplePointToPointMediator extends PointToPointMediator {

	private int count = 1;
	private Connection connection;
	private Date fechaHistoricaMaximaAnterior, fechaHistoricaMaximaNueva, ultimaFechaBaseTendencia;
	private IDatoHistoricoDAO[] daosDatoHistorico;
	private ITendenciaDAO[] daosTendencia;
	private IParametroDAO[] daosParametro;
	private MongoGeneticDAO<Tendencia> tendenciaDAO;

	@Override
	public void init() throws GeneticDAOException {
		DataClient<?>[] dataClients = DriverDBFactory.createDataClient();
		this.daosDatoHistorico = (IDatoHistoricoDAO[]) DriverDBFactory.createDAO("datoHistorico", dataClients);
		this.daosTendencia = (ITendenciaDAO[]) DriverDBFactory.createDAO("tendencia", dataClients);
		this.daosParametro = (IParametroDAO[]) DriverDBFactory.createDAO("parametro", dataClients);

		sourceExportedHistoryDataPath = daosParametro[0].getValorParametro("SOURCE_EXPORTED_HISTORY_DATA_PATH");
		processedExportedHistoryDataPath = daosParametro[0].getValorParametro("PROCESSED_EXPORTED_HISTORY_DATA_PATH");
		exportedPropertyFileName = daosParametro[0].getValorParametro("EXPORTED_PROPERTY_FILE_NAME");
		sourceEstrategiasPath = daosParametro[0].getValorParametro("SOURCE_ESTRATEGIAS_PATH");
	}

	@Override
	public void start() throws GeneticDAOException {
		try {
			while (true) {
				int imported = 0;
				for (int j = 0; j < daosDatoHistorico.length; j++) {
					this.fechaHistoricaMaximaAnterior = DateUtil.obtenerFechaMinima(
							daosDatoHistorico[j].getFechaHistoricaMaxima(), fechaHistoricaMaximaAnterior);
					imported = importarDatosHistoricos();
					this.fechaHistoricaMaximaNueva = DateUtil.obtenerFechaMinima(
							daosDatoHistorico[j].getFechaHistoricaMaxima(), fechaHistoricaMaximaNueva);
				}
				this.exportarDatosHistoricos();
				this.setUltimaFechaTendencia(count);
				LogUtil.logTime("ultimaFechaBaseTendencia=" + DateUtil.getDateString(this.ultimaFechaBaseTendencia)
						+ ",fechaHistoricaMaximaAnterior=" + DateUtil.getDateString(this.fechaHistoricaMaximaAnterior)
						+ ",fechaHistoricaMaximaNueva=" + DateUtil.getDateString(this.fechaHistoricaMaximaNueva)
						+ ",count=" + count, 1);
				procesarIndividuos();
				procesarTendencias();
				exportarIndividuos();
				crearNuevosIndividuos();
				if (imported == 0) {
					count++;
				} else {
					count = 1;
				}
			}
		} catch (SQLException | IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException
				| IllegalAccessException | InvocationTargetException | ParseException | GeneticException e) {
			throw new GeneticDAOException("Error start", e);
		}
	}

/*	private void procesarIndividuos() throws FileNotFoundException {
		logTime("Init Procesar Individuos", 1);
		GeneticDelegateBD delegate = new GeneticDelegateBD();
		delegate.process(true);
		logTime("End Procesar Individuos", 1);
	}

	private void procesarTendencias() throws SQLException, ClassNotFoundException {
		logTime("Init Procesar Tendencias", 1);
		OracleParametroDAO parametroDAO = new OracleParametroDAO(connection);
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
*/
}

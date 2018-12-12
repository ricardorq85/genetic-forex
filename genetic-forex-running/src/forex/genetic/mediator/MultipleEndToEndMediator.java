package forex.genetic.mediator;

import static forex.genetic.util.LogUtil.logTime;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import forex.genetic.dao.IDatoHistoricoDAO;
import forex.genetic.dao.IParametroDAO;
import forex.genetic.delegate.GeneticDelegateBD;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.factory.ProcesarTendenciasFactory;
import forex.genetic.manager.IGeneticManager;
import forex.genetic.manager.IndividuoManager;
import forex.genetic.tendencia.manager.ProcesarTendenciasBuySellManager;
import forex.genetic.tendencia.manager.TendenciaProcesoManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.DataClient;

public class MultipleEndToEndMediator extends EndToEndMediator {

	private List<DataClient> dataClients;

	@Override
	public void init() throws GeneticDAOException {
		dataClients = DriverDBFactory.createDataClients();

		IParametroDAO parametroDAO = dataClients.get(0).getDaoParametro();
		sourceExportedHistoryDataPath = parametroDAO.getValorParametro("SOURCE_EXPORTED_HISTORY_DATA_PATH");
		processedExportedHistoryDataPath = parametroDAO.getValorParametro("PROCESSED_EXPORTED_HISTORY_DATA_PATH");
		exportedPropertyFileName = parametroDAO.getValorParametro("EXPORTED_PROPERTY_FILE_NAME");
		sourceEstrategiasPath = parametroDAO.getValorParametro("SOURCE_ESTRATEGIAS_PATH");
	}

	@Override
	public void start() throws GeneticBusinessException {
		try {
			while (true) {
				int imported = 0;
				for (int j = 0; j < dataClients.size(); j++) {
					IDatoHistoricoDAO daoDatoHistorico = ((IDatoHistoricoDAO) dataClients.get(j).getDaoDatoHistorico());
					fechaHistoricaMaximaAnterior = DateUtil.obtenerFechaMinima(
							daoDatoHistorico.getFechaHistoricaMaxima(), fechaHistoricaMaximaAnterior);
					// TODO ricardorq85
					// imported = importarDatosHistoricos();
					this.fechaHistoricaMaximaNueva = DateUtil
							.obtenerFechaMinima(daoDatoHistorico.getFechaHistoricaMaxima(), fechaHistoricaMaximaNueva);
				}
				// TODO ricardorq85
				// this.exportarDatosHistoricos();
				// this.setUltimaFechaTendencia(count);
				LogUtil.logTime("ultimaFechaBaseTendencia=" + DateUtil.getDateString(this.ultimaFechaBaseTendencia)
						+ ",fechaHistoricaMaximaAnterior=" + DateUtil.getDateString(this.fechaHistoricaMaximaAnterior)
						+ ",fechaHistoricaMaximaNueva=" + DateUtil.getDateString(this.fechaHistoricaMaximaNueva)
						+ ",count=" + count, 1);
				//TODO rrojasq Hacer con hilos para cada driver
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
		} catch (SQLException | IOException | ClassNotFoundException | GeneticDAOException e) {
			throw new GeneticBusinessException("Error start", e);
		}
	}

	protected void procesarIndividuos() throws FileNotFoundException {
		logTime("Init Procesar Individuos", 1);
		GeneticDelegateBD delegate = new GeneticDelegateBD();
		delegate.multipleProcess(true);
		logTime("End Procesar Individuos", 1);
	}

	public void procesarTendencias() throws GeneticBusinessException {
		try {
			IGeneticManager[] managers = DriverDBFactory.createManagers("tendencia");
			for (int i = 0; i < dataClients.size(); i++) {
				procesarTendencias(dataClients.get(i), (TendenciaProcesoManager) managers[i]);
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException("procesarTendencias", e);
		}
	}

	protected void exportarIndividuos() throws GeneticBusinessException {
		try {
			for (int i = 0; i < dataClients.size(); i++) {
				LogUtil.logTime("Init Exportar Individuos", 1);
				boolean existNewData;
				existNewData = this.existenNuevosDatosHistoricos();
				if (existNewData) {
					String fileName = sourceEstrategiasPath + "\\Tendencia" + IndividuoManager.nextId() + ".csv";
					Path filePath = FileSystems.getDefault().getPath(fileName);
					ProcesarTendenciasBuySellManager manager = ProcesarTendenciasFactory
							.createManager(dataClients.get(i));
					manager.setParametroFechaInicio(ultimaFechaBaseTendencia);
					manager.setParametroFechaFin(fechaHistoricaMaximaNueva);
					ExportThread exportThread = new ExportThread(filePath, manager);
					LogUtil.logTime("Lanzando exportacion", 1);
					exportThread.runExport();
					// LogUtil.logTime("Lanzando hilo para exportacion", 1);
					// exportThread.start();
					// manager.procesarTendencias();
					// manager.export(filePath);
				} else {
					LogUtil.logTime("No existen nuevos datos. No se procesara la exportacion", 1);
				}
				LogUtil.logTime("End Exportar Individuos", 1);
			}
		} catch (IOException | GeneticDAOException | ClassNotFoundException | NoSuchMethodException
				| InstantiationException | IllegalAccessException | InvocationTargetException | SQLException
				| ParseException e) {
			throw new GeneticBusinessException(e);
		}
	}

//	private void refrescarDatosTendencia() throws SQLException {
//		String mv = "TENDENCIA_ULTIMOMES";
//		LogUtil.logTime("Refrescando vista materializada: " + mv, 1);
//		JDBCUtil.refreshMaterializedView(this.connection, mv, "C");
//	}
//
//	private boolean existenNuevosDatosHistoricos() throws IOException {
//		Path path = FileSystems.getDefault().getPath(sourceExportedHistoryDataPath);
//		long countFile = Files.list(path).count();
//		return (countFile > 0);
//	}
//
//	private void crearNuevosIndividuos() throws ClassNotFoundException, SQLException {
//		LogUtil.logTime("Init Crear individuos x indicador", 1);
//		IndividuoXIndicadorManager manager = new IndividuoXIndicadorManager(ultimaFechaBaseTendencia,
//				fechaHistoricaMaximaNueva, 12);
//		manager.crearIndividuos();
//		LogUtil.logTime("End Crear individuos x indicador", 1);
//	}
//
//	class ExportThread extends Thread {
//		Path path;
//		ProcesarTendenciasBuySellManager manager;
//
//		ExportThread(Path path, ProcesarTendenciasBuySellManager manager) {
//			this.path = path;
//			this.manager = manager;
//		}
//
//		public void run() {
//			try {
//				runExport();
//			} catch (IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException
//					| IllegalAccessException | InvocationTargetException | SQLException | ParseException
//					| GeneticException e) {
//				e.printStackTrace();
//			}
//		}
//
//		public void runExport()
//				throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException,
//				InvocationTargetException, SQLException, ParseException, GeneticException, IOException {
//			manager.procesarTendencias();
//			manager.export(path);
//		}
//	}
//
}

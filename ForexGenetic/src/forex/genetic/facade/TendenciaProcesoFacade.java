package forex.genetic.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import forex.genetic.entities.Individuo;
import forex.genetic.entities.Point;
import forex.genetic.entities.TendenciaEstadistica;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.tendencia.manager.TendenciaProcesoManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.RandomUtil;
import forex.genetic.util.ThreadUtil;
import forex.genetic.util.jdbc.DataClient;

public class TendenciaProcesoFacade implements IGeneticFacade {

	protected static List<Thread> threads = new Vector<>();

	protected DataClient dataClient;
	private static final double FACTOR_NUMERO_RANDOM_TENDENCIAS = 0.3;
	protected Date fechaComparacion;

	public TendenciaProcesoFacade(DataClient dc) throws GeneticBusinessException {
		this.dataClient = dc;
		setup();
	}

	public void setup() throws GeneticBusinessException {
		this.fechaComparacion = DateUtil.calcularFechaComparacionParaTendenciaUltimosDatos();
		LogUtil.logTime("Borrando tendencias ultimos datos anteriores a " + DateUtil.getDateString(fechaComparacion),
				1);
		try {
			int affected = dataClient.getDaoTendenciaUltimosDatos().deleteTendenciaMenorQue(fechaComparacion);
			dataClient.commit();
			LogUtil.logTime("Tendencias borradas: " + affected, 1);
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
	}

	public List<TendenciaEstadistica> calcularTendencias(Date fechaBaseInicial, Date fechaBaseFinal, int filas)
			throws GeneticBusinessException {
		return this.calcularTendencias(1, fechaBaseInicial, fechaBaseFinal, filas);
	}

	public List<TendenciaEstadistica> calcularTendencias(int cantidadVeces, Date fechaBaseInicial, Date fechaBaseFinal,
			int filas) throws GeneticBusinessException {
		List<TendenciaEstadistica> listaTendencias = new ArrayList<TendenciaEstadistica>();
		try {
			List<? extends Point> pointsFechaTendencia;
			pointsFechaTendencia = dataClient.getDaoDatoHistorico().consultarHistoricoOrderByPrecio(fechaBaseInicial,
					fechaBaseFinal);
			int c = cantidadVeces + 1;
			if ((pointsFechaTendencia != null) && (!pointsFechaTendencia.isEmpty())) {
				int size = pointsFechaTendencia.size();
				int sizeLimit = (int) (size * FACTOR_NUMERO_RANDOM_TENDENCIAS);
				for (int i = 1; i < c; i++) {
					int randomIndex = RandomUtil.nextInt(sizeLimit + 1);
					if (threads.size() > 5) {
						ThreadUtil.joinThread(threads.get(0));
						threads.remove(0);
					}
					Runnable runner = new Runnable() {
						@Override
						public void run() {
							try {
								TendenciaProcesoManager tpm = (TendenciaProcesoManager) DriverDBFactory
										.createManager(dataClient, "tendenciaProceso");
								tpm.setFechaComparacion(fechaComparacion);
								listaTendencias
										.addAll(tpm.calcularTendencias(pointsFechaTendencia.get(randomIndex), filas));
								tpm = (TendenciaProcesoManager) DriverDBFactory.createManager(dataClient,
										"tendenciaProceso");
								tpm.setFechaComparacion(fechaComparacion);
								listaTendencias.addAll(tpm
										.calcularTendencias(pointsFechaTendencia.get(size - randomIndex - 1), filas));
							} catch (GeneticBusinessException | GeneticDAOException e) {
								e.printStackTrace();
							}
						}
					};
					Thread thread = new Thread(runner);
					threads.add(thread);
					thread.start();
				}
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
		return listaTendencias;
	}

	public List<TendenciaEstadistica> calcularTendencias(Date fechaBase, int filas) throws GeneticBusinessException {
		List<TendenciaEstadistica> listaTendencias = new ArrayList<TendenciaEstadistica>();
		try {
			Point p = dataClient.getDaoDatoHistorico().consultarXFecha(fechaBase);
			if (p != null) {
				TendenciaProcesoManager tpm = (TendenciaProcesoManager) DriverDBFactory.createManager(dataClient,
						"tendenciaProceso");
				tpm.setFechaComparacion(fechaComparacion);
				listaTendencias.addAll(tpm.calcularTendencias(p, filas));
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
		return listaTendencias;
	}

	public TendenciaEstadistica calcularTendencia(Point currentPoint, Date fechaBase, String idIndividuo)
			throws GeneticBusinessException {
		Individuo individuo;
		try {
			individuo = dataClient.getDaoOperaciones().consultarIndividuoOperacionActiva(idIndividuo, fechaBase, 2);
			TendenciaProcesoManager tpm = (TendenciaProcesoManager) DriverDBFactory.createManager(dataClient,
					"tendenciaProceso");
			tpm.setFechaComparacion(fechaComparacion);

			TendenciaEstadistica tendenciaEstadistica = tpm.calcularTendencia(currentPoint, individuo);
			return tendenciaEstadistica;
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
	}

	public TendenciaEstadistica calcularTendencia(Date fechaBase, String idIndividuo) throws GeneticBusinessException {
		try {
			Individuo individuo;
			individuo = dataClient.getDaoOperaciones().consultarIndividuoOperacionActiva(idIndividuo, fechaBase, 2);
			return this.calcularTendencia(fechaBase, individuo);
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
	}

	public TendenciaEstadistica calcularTendencia(Date fechaBase, Individuo individuo) throws GeneticBusinessException {
		TendenciaEstadistica tendenciaEstadistica = null;
		try {
			List<? extends Point> pointsFechaTendencia;
			pointsFechaTendencia = dataClient.getDaoDatoHistorico().consultarHistorico(fechaBase, fechaBase);
			if ((pointsFechaTendencia != null) && (!pointsFechaTendencia.isEmpty())) {
				TendenciaProcesoManager tpm = (TendenciaProcesoManager) DriverDBFactory.createManager(dataClient,
						"tendenciaProceso");
				tpm.setFechaComparacion(fechaComparacion);
				tendenciaEstadistica = tpm.calcularTendencia(pointsFechaTendencia.get(0), individuo);
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
		return tendenciaEstadistica;
	}
}

package forex.genetic.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.entities.Point;
import forex.genetic.entities.TendenciaEstadistica;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.tendencia.manager.TendenciaProcesoManager;
import forex.genetic.util.ThreadUtil;
import forex.genetic.util.jdbc.DataClient;

public class MongoTendenciaProcesoFacade extends TendenciaProcesoFacade {

	public MongoTendenciaProcesoFacade(DataClient dc) throws GeneticBusinessException {
		super(dc);
	}

	public List<TendenciaEstadistica> calcularTendencias(int cantidadVeces, Date fechaBaseInicial, Date fechaBaseFinal,
			int filas) throws GeneticBusinessException {
		List<TendenciaEstadistica> listaTendencias = new ArrayList<TendenciaEstadistica>();
		List<? extends Point> pointsFechaTendencia;
		try {
			pointsFechaTendencia = dataClient.getDaoDatoHistorico().consultarHistoricoOrderByPrecio(fechaBaseInicial,
					fechaBaseFinal);
			List<Thread> threads = new ArrayList<>();
			for (Point point : pointsFechaTendencia) {
				if (threads.size() > 5) {
					ThreadUtil.joinThreads(threads);
					threads = new ArrayList<>();
				}
				Runnable runner = new Runnable() {
					@Override
					public void run() {
						TendenciaProcesoManager tpm;
						try {
							tpm = (TendenciaProcesoManager) DriverDBFactory.createOracleManager("tendenciaProceso");
							tpm.setFechaComparacion(fechaComparacion);
							listaTendencias.addAll(tpm.calcularTendencias(point, filas));
						} catch (GeneticBusinessException e) {
							e.printStackTrace();
						}
					}
				};
				Thread thread = new Thread(runner);
				threads.add(thread);
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
		return listaTendencias;
	}
}

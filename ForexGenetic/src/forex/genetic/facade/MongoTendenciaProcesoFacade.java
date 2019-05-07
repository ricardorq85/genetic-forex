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
			for (Point point : pointsFechaTendencia) {
				if (threads.size() > 3) {
					ThreadUtil.joinThread(threads.get(0));
					threads.remove(0);
				}
				Runnable runner = new Runnable() {
					@Override
					public void run() {
						TendenciaProcesoManager tpm;
						try {
							tpm = (TendenciaProcesoManager) DriverDBFactory.createManager(dataClient,
									"tendenciaProceso");
							tpm.setFechaComparacion(fechaComparacion);
							List<TendenciaEstadistica> l = tpm.calcularTendencias(point, filas);
							if (!l.isEmpty()) {
								listaTendencias.addAll(l);
							}
						} catch (GeneticBusinessException | GeneticDAOException e) {
							e.printStackTrace();
						}
					}
				};
				Thread thread = new Thread(runner);
				threads.add(thread);
				thread.start();
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
		return listaTendencias;
	}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.mongodb;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.ProcesoIndividuoManager;
import forex.genetic.thread.mongo.MongoProcesarIndividuoThread;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.RandomUtil;
import forex.genetic.util.ThreadUtil;
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public class MongoProcesoIndividuoManager extends ProcesoIndividuoManager {

	public MongoProcesoIndividuoManager(DataClient dc) throws GeneticBusinessException {
		super(dc);
	}

	public void process(boolean onlyOne) {
		boolean any;
		try {
			// minFechaHistorico = DateUtil.obtenerFecha("2008/05/06 00:00");
			Date minFechaHistorico = dataClient.getDaoDatoHistorico().getFechaHistoricaMinima();
			Date maxFechaHistorico = dataClient.getDaoDatoHistorico().getFechaHistoricaMaxima();
			List<Thread> threads = new Vector<>();
			do {
				any = false;
				int numeroDelFiltroAdicional = 0;
				int maxHilos = 5;
				while ((threads.size() <= maxHilos) && (numeroDelFiltroAdicional < 10)) {
					String threadName = "Mongo_" + numeroDelFiltroAdicional;
					// if (!threads.contains(new MongoProcesarIndividuoThread(threadName, null))) {
					if ((RandomUtil.nextBoolean()) || (numeroDelFiltroAdicional - threads.size() >= 10 - maxHilos)) {
						LogUtil.logTime("Obteniendo individuos para el hilo " + threadName, 1);
						@SuppressWarnings("unchecked")
						List<MongoIndividuo> individuos = (List<MongoIndividuo>) dataClient.getDaoIndividuo()
								.getListByProcesoEjecucion("" + numeroDelFiltroAdicional, maxFechaHistorico);
						LogUtil.logTime("Individuos consultados para el hilo " + threadName + ": " + individuos.size(),
								1);
						if ((individuos != null) && (!individuos.isEmpty())) {
							MongoProcesarIndividuoThread procesarIndividuoThread = new MongoProcesarIndividuoThread(
									threadName, individuos);
							procesarIndividuoThread.setMaxFechaHistorico(maxFechaHistorico);
							procesarIndividuoThread.setMinFechaHistorico(minFechaHistorico);
							procesarIndividuoThread.start();
							// procesarIndividuoThread.run();
							threads.add(procesarIndividuoThread);
							any = true;
						} else {
							LogUtil.logTime("No existen individuos", 1);
						}
					}
					// }
					numeroDelFiltroAdicional++;
				}
//				if (!threads.isEmpty()) {
//					ThreadUtil.joinThread(threads.get(0));
//				}
				ThreadUtil.joinThreads(threads);
			} while (any && !onlyOne);
			//ThreadUtil.joinThreads(threads);
		} catch (GeneticDAOException ex) {
			ex.printStackTrace();
		}
	}
}

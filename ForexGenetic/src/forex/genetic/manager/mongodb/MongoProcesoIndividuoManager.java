/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.mongodb;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
			//TODO ricardorq85 Quitar, es solo para agilizar el proceso para verificar tendencias
			maxFechaHistorico = DateUtil.obtenerFecha("2016/01/01 00:00");
			do {
				any = false;
				List<Thread> threads = new ArrayList<>();
				int numeroHilos = 0;
				int numeroDelFiltroAdicional = 0;
				while ((numeroHilos < 6) && (numeroDelFiltroAdicional < 10)) {
					if (RandomUtil.nextBoolean()) {
						numeroHilos++;
						LogUtil.logTime("Obteniendo individuos para el filtro " + numeroDelFiltroAdicional, 1);
						@SuppressWarnings("unchecked")
						List<MongoIndividuo> individuos = (List<MongoIndividuo>) dataClient.getDaoIndividuo()
								.getListByProcesoEjecucion("" + numeroDelFiltroAdicional, maxFechaHistorico);
						if ((individuos != null) && (!individuos.isEmpty())) {
							MongoProcesarIndividuoThread procesarIndividuoThread = new MongoProcesarIndividuoThread(
									"Mongo_" + numeroDelFiltroAdicional, individuos);
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
					numeroDelFiltroAdicional++;
				}
				ThreadUtil.joinThreads(threads);
			} while (any && !onlyOne);
		} catch (GeneticDAOException | ParseException ex) {
			ex.printStackTrace();
		}
	}

}

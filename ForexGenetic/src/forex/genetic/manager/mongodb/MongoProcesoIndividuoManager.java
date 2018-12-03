/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.ProcesoIndividuoManager;
import forex.genetic.thread.mongo.MongoProcesarIndividuoThread;
import forex.genetic.util.LogUtil;
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
			// TODO rrojasq: cambiar para que no sea consultado cada vez que se necesita. Puede ser desde el DAO.
			//maxFechaHistorico = DateUtil.obtenerFecha("2018/11/12 00:00");
			//minFechaHistorico = DateUtil.obtenerFecha("2008/05/06 00:00");
			Date minFechaHistorico = dataClient.getDaoDatoHistorico().getFechaHistoricaMinima();
			Date maxFechaHistorico = dataClient.getDaoDatoHistorico().getFechaHistoricaMaxima();
			do {
				any = false;
				List<Thread> threads = new ArrayList<>();
				int countFiltro = 1;
				while (countFiltro == 1) {
					LogUtil.logTime("Obteniendo individuos para el filtro " + countFiltro, 1);
					@SuppressWarnings("unchecked")
					List<MongoIndividuo> individuos = (List<MongoIndividuo>) dataClient.getDaoIndividuo()
							.getListByProcesoEjecucion(null, maxFechaHistorico);
					if ((individuos != null) && (!individuos.isEmpty())) {
						MongoProcesarIndividuoThread procesarIndividuoThread = new MongoProcesarIndividuoThread(
								"Mongo_" + countFiltro, individuos);
						procesarIndividuoThread.setMaxFechaHistorico(maxFechaHistorico);
						procesarIndividuoThread.setMinFechaHistorico(minFechaHistorico);
						// procesarIndividuoThread.start();
						procesarIndividuoThread.run();
						threads.add(procesarIndividuoThread);
						any = true;
					} else {
						LogUtil.logTime("No existen individuos", 1);
					}
					countFiltro++;
				}
				for (Thread thread : threads) {
					thread.join();
				}
			} while (any && !onlyOne);
		} catch (InterruptedException | GeneticDAOException ex) {
			ex.printStackTrace();
		}
	}

}

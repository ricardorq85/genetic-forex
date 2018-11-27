/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.mongodb;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.dao.mongodb.MongoIndividuoDAO;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.ProcesoIndividuoManager;
import forex.genetic.thread.mongo.MongoProcesarIndividuoThread;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class MongoProcesoIndividuoManager extends ProcesoIndividuoManager {

	public MongoProcesoIndividuoManager() throws GeneticBusinessException {
		super();
		try {
			individuoDAO = new MongoIndividuoDAO();
			dhDAO = new MongoDatoHistoricoDAO();
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException("", e);
		}
	}

	public void process() {
		this.process(false);
	}

	public void process(boolean onlyOne) {
		boolean any;
		try {
			// TODO rrojasq: cambiar para que no sea consultado cada vez que se necesita. Puede ser desde el DAO.
			maxFechaHistorico = DateUtil.obtenerFecha("2018/11/12 00:00");
			minFechaHistorico = DateUtil.obtenerFecha("2008/05/06 00:00");
//			maxFechaHistorico = dhDAO.getFechaHistoricaMaxima();
//			minFechaHistorico = dhDAO.getFechaHistoricaMinima();
			do {
				any = false;
				List<Thread> threads = new ArrayList<>();
				int countFiltro = 1;
				while (countFiltro == 1) {
					LogUtil.logTime("Obteniendo individuos para el filtro " + countFiltro, 1);
					@SuppressWarnings("unchecked")
					List<MongoIndividuo> individuos = (List<MongoIndividuo>) individuoDAO
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
		} catch (InterruptedException | GeneticDAOException | ParseException ex) {
			ex.printStackTrace();
		}
	}

}

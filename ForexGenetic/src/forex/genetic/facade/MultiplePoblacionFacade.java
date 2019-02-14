/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.facade;

import java.sql.SQLException;

import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.manager.IGeneticManager;
import forex.genetic.manager.ProcesoIndividuoManager;

/**
 *
 * @author ricardorq85
 */
public class MultiplePoblacionFacade extends PoblacionFacade {

	/**
	 * @throws GeneticDAOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 *
	 */
	public void process() throws GeneticBusinessException {
		this.process(false);
	}

	public void process(boolean onlyOne) throws GeneticBusinessException {
		IGeneticManager[] managers;
		try {
			managers = DriverDBFactory.createManagers("procesoIndividuo");
			Thread[] threads = new Thread[managers.length];
			for (int i = 0; i < managers.length; i++) {
				ProcesoIndividuoManager manager = (ProcesoIndividuoManager) managers[i];
				try {
					// TODO rrojasq: onlyOne como parametro
					manager.process(true);
				} catch (GeneticBusinessException e) {
					e.printStackTrace();
				}

//			Runnable runner = new Runnable() {
//				@Override
//				public void run() {
//					try {
//						manager.process(onlyOne);
//					} catch (GeneticBusinessException e) {
//						e.printStackTrace();
//					}
//				}
//			};
//			threads[i] = new Thread(runner);
//			threads[i].start();
			}
//		ThreadUtil.joinThreads(threads);
		} catch (GeneticDAOException e1) {
			throw new GeneticBusinessException(null, e1);
		}

	}

}

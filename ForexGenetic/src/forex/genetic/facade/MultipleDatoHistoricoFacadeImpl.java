/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.facade;

import java.util.List;

import forex.genetic.dao.IDatoHistoricoDAO;
import forex.genetic.entities.Point;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public class MultipleDatoHistoricoFacadeImpl extends DatoHistoricoFacade {

	/**
	 *
	 * @param points
	 * @throws GeneticDAOException
	 */
	public void cargarDatoHistorico(List<Point> points) throws GeneticDAOException {
		DataClient<?>[] dataClients = DriverDBFactory.createDataClient();
		IDatoHistoricoDAO[] daos = (IDatoHistoricoDAO[]) DriverDBFactory.createDAO("datoHistorico", dataClients);
		int countError = 0;
		for (int k = 0; k < daos.length; k++) {
			LogUtil.logTime(new StringBuilder("DAO:").append(daos[k].getClass()).toString(), 1);
			try {
				for (int i = 0; i < points.size(); i++) {
					Point point = points.get(i);
					daos[k].insertOrUpdate(point);
					if (((i % 3000) == 0) || (i == points.size() - 1)) {
						daos[k].commit();
						System.out.println("");
					}
				}
			} catch (GeneticDAOException e) {
				e.printStackTrace();
			}
			LogUtil.logTime("Datos cargados=" + (points.size() - countError) + ";Datos con error=" + countError, 1);
		}
	}
}

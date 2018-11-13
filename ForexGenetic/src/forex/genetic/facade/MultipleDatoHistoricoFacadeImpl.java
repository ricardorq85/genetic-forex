/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.facade;

import java.util.List;

import forex.genetic.dao.GeneticDAO;
import forex.genetic.entities.Point;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class MultipleDatoHistoricoFacadeImpl extends DatoHistoricoFacade {

	/**
	 *
	 * @param points
	 */
	public void cargarDatoHistorico(List<Point> points) {
		GeneticDAO<Point>[] daos = (GeneticDAO<Point>[]) DriverDBFactory.createDAO("datoHistorico");
		int countError = 0;
		for (int k = 0; k < daos.length; k++) {
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
		}
		LogUtil.logTime("Datos cargados=" + (points.size() - countError) + ";Datos con error=" + countError, 1);
	}
}

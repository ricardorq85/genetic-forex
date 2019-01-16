/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.facade;

import java.util.List;

import forex.genetic.dao.IDatoHistoricoDAO;
import forex.genetic.entities.Point;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public class DatoHistoricoFacade implements IGeneticFacade {

	private DataClient dataClient;

	/**
	 *
	 * @param points
	 */
	public void cargarDatoHistorico(List<Point> points) throws GeneticDAOException {
		IDatoHistoricoDAO dao = dataClient.getDaoDatoHistorico();
		int countError = 0;

		for (int i = 0; i < points.size(); i++) {
			Point point = points.get(i);
			dao.insertOrUpdate(point);
			if ((i % 3000) == 0) {
				dataClient.commit();
			}
		}
		dataClient.commit();
		LogUtil.logTime("Datos cargados=" + (points.size() - countError) + ";Datos con error=" + countError, 1);
	}

	public void setDataClient(DataClient dataClient) {
		this.dataClient = dataClient;
	}
}

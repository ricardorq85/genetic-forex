/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.mongodb;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.Point;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class MongoExportDatoHistoricoManager extends MongoExportManager<Point> {

	public MongoExportDatoHistoricoManager() throws GeneticBusinessException {
		try {
			dao = (MongoDatoHistoricoDAO) DriverDBFactory.createDataClient("mongodb").getDaoDatoHistorico();
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(e);
		}
	}

	public void exportar() throws GeneticBusinessException {
		Date d1, d2;
		try {
			d1 = DateUtil.obtenerFecha("2016/01/01 00:00");
			d2 = DateUtil.obtenerFecha("2016/01/04 23:59");
		} catch (ParseException e) {
			throw new GeneticBusinessException(e);
		}

		DateInterval interval = new DateInterval(d1, d2);

		List<String> findJsonList = dao.findJson(interval);
		for (String doc : findJsonList) {
			LogUtil.logTime(doc, 1);
		}
	}

}
package forex.genetic.delegate;

import static forex.genetic.util.LogUtil.logTime;

import java.util.Date;
import java.util.List;

import forex.genetic.entities.Point;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.manager.mongodb.RandomFillDatoHistoricoManager;
import forex.genetic.manager.mongodb.RandomFillIndividuoManager;

public class FillDatabaseDelegate {

	public void fill() {
		try {
			List<Point> dhs = null;
			logTime("Filling Datos Historicos", 1);
			RandomFillDatoHistoricoManager dhManager = new RandomFillDatoHistoricoManager();
			dhs = dhManager.fill();
			logTime("Filling Individuos", 1);
			RandomFillIndividuoManager indManager;
			indManager = new RandomFillIndividuoManager();
			List<MongoIndividuo> individuos = indManager.fill(dhs);

			logTime("Consultando fechas individuos", 1);
			List<Date> fechas = indManager.consultarPuntosApertura(individuos);
			fechas.toString();
		} catch (GeneticBusinessException e) {
			e.printStackTrace();
		}
	}

}

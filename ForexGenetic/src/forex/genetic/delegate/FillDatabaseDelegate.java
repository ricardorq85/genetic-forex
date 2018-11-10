package forex.genetic.delegate;

import static forex.genetic.util.LogUtil.logTime;

import java.util.Date;
import java.util.List;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Point;
import forex.genetic.manager.mongodb.RandomFillDatoHistoricoManager;
import forex.genetic.manager.mongodb.RandomFillIndividuoManager;

public class FillDatabaseDelegate {

	public void fill() {
		List<Point> dhs = null;
		logTime("Filling Datos Historicos", 1);
		RandomFillDatoHistoricoManager dhManager = new RandomFillDatoHistoricoManager();
		dhs = dhManager.fill();
		logTime("Filling Individuos", 1);
		RandomFillIndividuoManager indManager = new RandomFillIndividuoManager();
		List<IndividuoEstrategia> individuos = indManager.fill(dhs);
		
		logTime("Consultando fechas individuos", 1);
		List<Date> fechas = indManager.consultarPuntosApertura(individuos);
		fechas.toString();
	}

}

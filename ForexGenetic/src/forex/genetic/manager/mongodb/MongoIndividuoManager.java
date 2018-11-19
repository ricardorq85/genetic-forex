package forex.genetic.manager.mongodb;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.dao.mongodb.MongoIndividuoDAO;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Point;
import forex.genetic.util.DateUtil;

public class MongoIndividuoManager {

	private MongoIndividuoDAO dao;
	private MongoDatoHistoricoDAO dhDAO;

	public MongoIndividuoManager() {
		dao = new MongoIndividuoDAO();
		dhDAO = new MongoDatoHistoricoDAO(true);
	}

	public List<? extends Point> consultarPuntosApertura(List<? extends IndividuoEstrategia> individuos) {
		final DateInterval rango = new DateInterval();
		List<Point> puntosTotales = new ArrayList<Point>();
		try {
			rango.setLowInterval(DateUtil.obtenerFecha("2009/01/01 00:00"));
			rango.setHighInterval(DateUtil.obtenerFecha("2009/06/01 00:00"));

			IndividuoEstrategia ind = dao.consultarById("1394755200000.14");
			// individuos.stream().forEach(ind -> {
			Point punto = dhDAO.consultarProximoPuntoApertura(ind, rango);
			if (punto != null) {
				System.out.println("Individuo con fechas consultadas:" + ind.getId());
				System.out.println(DateUtil.getDateString(punto.getDate()));
				puntosTotales.add(punto);
			}
			// });
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return puntosTotales;
	}

}

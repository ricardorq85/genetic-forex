package forex.genetic.manager.mongodb;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.dao.mongodb.MongoIndividuoDAO;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Point;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.DateUtil;

public class MongoIndividuoManager {

	private MongoIndividuoDAO dao;
	private MongoDatoHistoricoDAO dhDAO;

	public MongoIndividuoManager() throws GeneticBusinessException {
		try {
			dao = new MongoIndividuoDAO();
			dhDAO = new MongoDatoHistoricoDAO(true);
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException("", e);
		}
	}

	public List<? extends Point> consultarPuntosApertura(List<? extends IndividuoEstrategia> individuos) {
		final DateInterval rango = new DateInterval();
		List<Point> puntosTotales = new ArrayList<Point>();
		try {
			rango.setLowInterval(DateUtil.obtenerFecha("2009/01/01 00:00"));
			rango.setHighInterval(DateUtil.obtenerFecha("2009/06/01 00:00"));

			IndividuoEstrategia ind = dao.consultarById("1394755200000.14");
			return dhDAO.consultarProximosPuntosApertura(ind, rango);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return puntosTotales;
	}

}

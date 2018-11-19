package forex.genetic.manager.mongodb;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.helper.mongodb.MongoIndividuoMapper;
import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.dao.mongodb.MongoIndividuoDAO;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Point;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.manager.PoblacionManager;
import forex.genetic.util.DateUtil;

public class RandomFillIndividuoManager {

	private MongoIndividuoDAO dao;
	private MongoDatoHistoricoDAO dhDAO;

	public RandomFillIndividuoManager() {
		dao = new MongoIndividuoDAO();
		dhDAO = new MongoDatoHistoricoDAO(true);
	}

	public List<Date> consultarPuntosApertura(List<MongoIndividuo> individuos) {
		final DateInterval rango = new DateInterval();
		List<Date> fechas = new ArrayList<Date>();

		try {
			rango.setLowInterval(DateUtil.obtenerFecha("2018/01/01 00:00"));
			rango.setHighInterval(DateUtil.obtenerFecha("2018/11/10 00:00"));

			IndividuoEstrategia ind = dao.consultarById("1541772969789.20");
			// individuos.stream().forEach(ind -> {
			List<Date> fechas2 = dhDAO.consultarPuntosApertura(rango, ind);
			if ((fechas2 != null) && (!fechas.isEmpty())) {
				System.out.println("Individuo con fechas consultadas:" + ind.getId());
				System.out.println("Fechas consultadas:" + fechas2.toString());
				fechas.addAll(fechas2);
			}
			// });
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fechas;
	}

	public List<MongoIndividuo> fill(List<Point> points) {
		List<IndividuoEstrategia> datos = this.generate(points);
		List<MongoIndividuo> mongoDatos = ((MongoIndividuoMapper)dao.getMapper()).toMongoIndividuo(datos);
		dao.insertMany(mongoDatos);
		return mongoDatos;
	}

	private List<IndividuoEstrategia> generate(List<Point> points) {
		PoblacionManager pobManager = new PoblacionManager();
		pobManager.setPoints(points);
		pobManager.configureDateInterval();
		pobManager.generatePoblacionInicial();

		return pobManager.getPoblacion().getIndividuos();
	}
}

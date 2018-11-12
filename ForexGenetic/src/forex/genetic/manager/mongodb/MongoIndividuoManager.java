package forex.genetic.manager.mongodb;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.dao.mongodb.MongoIndividuoDAO;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.util.DateUtil;

public class MongoIndividuoManager {

	private MongoIndividuoDAO dao;
	private MongoDatoHistoricoDAO dhDAO;

	public MongoIndividuoManager() {
		dao = new MongoIndividuoDAO();
		dhDAO = new MongoDatoHistoricoDAO(true);
	}

	public List<Date> consultarPuntosApertura(List<? extends IndividuoEstrategia> individuos) {
		final DateInterval rango = new DateInterval();
		List<Date> fechas = new ArrayList<Date>();
		try {
			rango.setLowInterval(DateUtil.obtenerFecha("2009/01/01 00:00"));
			rango.setHighInterval(DateUtil.obtenerFecha("2009/06/01 00:00"));

			IndividuoEstrategia ind = dao.consultarById("1394755200000.14");
			// individuos.stream().forEach(ind -> {
			List<Date> fechas2 = dhDAO.consultarPuntosApertura(rango, ind);
			if ((fechas2 != null) && (!fechas2.isEmpty())) {
				System.out.println("Individuo con fechas consultadas:" + ind.getId());
				fechas2.stream().forEach(fe -> {
					System.out.println(DateUtil.getDateString(fe));
				});
				fechas.addAll(fechas2);
			}
			// });
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fechas;
	}

}

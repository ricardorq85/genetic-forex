/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.borrado;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.entities.Individuo;
import forex.genetic.entities.dto.ProcesoEjecucionDTO;
import forex.genetic.entities.mongo.MongoEstadistica;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public class MongoBorradoIndividuoSinOperacionesManager extends MongoBorradoManager {

	public MongoBorradoIndividuoSinOperacionesManager(DataClient dc, MongoEstadistica ea) {
		super(dc, "SIN_OPERACIONES", ea);
	}

	@Override
	protected List<Individuo> consultarIndividuos(Individuo individuo) throws GeneticBusinessException {
		List<Individuo> list = new ArrayList<>();
		Date fechaMinima = null;
		try {
			fechaMinima = DateUtil.obtenerFecha("2008/12/31 23:58");
		} catch (ParseException e) {
			fechaMinima = new Date();
		}

		ProcesoEjecucionDTO procesoEjecucion = ((MongoIndividuo) individuo).getProcesoEjecucion();
		if ((procesoEjecucion != null) && (procesoEjecucion.getMaxFechaHistorico() != null)
				&& (procesoEjecucion.getMaxFechaHistorico().after(fechaMinima))) {

			float diffMonths = DateUtil.diffMonths(procesoEjecucion.getMaxFechaHistorico(), fechaMinima);
			float minByMonth = 0.5F;
			float cantidadMinima = diffMonths * minByMonth;
			if ((estadisticaAnterior == null) || (estadisticaAnterior.getCantidadTotal() < (cantidadMinima))) {
				list.add(individuo);
			}
		}
		return list;
	}
}
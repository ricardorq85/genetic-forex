/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.borrado;

import java.util.Collections;
import java.util.List;

import forex.genetic.entities.Individuo;
import forex.genetic.entities.mongo.MongoEstadistica;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public class MongoBorradoDuplicadoConBorradoIndividuoManager extends MongoBorradoManager {

	public MongoBorradoDuplicadoConBorradoIndividuoManager(DataClient dc, MongoEstadistica ea) {
		super(dc, "DUPLICADO_INDIVIDUO", ea);
	}

	@Override
	protected List<Individuo> consultarIndividuos(Individuo individuo) throws GeneticBusinessException {
		try {
			List<Individuo> list = dataClient.getDaoIndividuoBorrado().consultarIndividuoHijoRepetido(individuo);
			if ((list != null) && (!list.isEmpty())) {
				list = Collections.singletonList(individuo);
			}
			return list;
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException("consultarIndividuo", e);
		}
	}
}

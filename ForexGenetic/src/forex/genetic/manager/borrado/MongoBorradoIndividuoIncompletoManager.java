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
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public class MongoBorradoIndividuoIncompletoManager extends MongoBorradoManager {

	public MongoBorradoIndividuoIncompletoManager(DataClient dc, MongoEstadistica ea) {
		super(dc, "INDIVIDUO_INCOMPLETO", ea);
	}

	@Override
	protected List<Individuo> consultarIndividuos(Individuo individuo) throws GeneticBusinessException {
		List<Individuo> list = Collections.emptyList();
//		List<? extends Indicator> indicadores = individuo.getOpenIndicators();
//		for (int i = 0; i < indicadores.size() && list.isEmpty(); i++) {
//			if (indicadores.get(i) == null) {
//				list = Collections.singletonList(individuo);
//			}
//		}
		return list;
	}
}

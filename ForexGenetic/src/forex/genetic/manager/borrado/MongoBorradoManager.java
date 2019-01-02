/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.borrado;

import java.util.List;

import forex.genetic.entities.Individuo;
import forex.genetic.entities.mongo.MongoEstadistica;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.manager.mongodb.MongoIndividuoManager;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public abstract class MongoBorradoManager extends BorradoManager {

	protected MongoEstadistica estadisticaAnterior;

	public MongoBorradoManager(DataClient dc, String tipoProceso, MongoEstadistica ea) {
		super(dc, tipoProceso);
		estadisticaAnterior = ea;
	}

	@Override
	public boolean validarYBorrarIndividuo(Individuo individuo) throws GeneticBusinessException {
		int count = 0;
		boolean deleted = false;
		List<Individuo> individuos = consultarIndividuos(individuo);
		if ((individuos != null) && (individuos.size() > 0)) {
			individuos.forEach((ind) -> {
				((MongoIndividuo) ind).setIdParentBorrado(individuo.getId());
				((MongoIndividuo) ind).setCausaBorrado(tipoProceso);
			});

			smartDelete(individuos);
			count += individuos.size();
			deleted = true;
		}
		if (count > 0) {
			LogUtil.logTime("Individuos borrados: " + count, 1);
		}
		return deleted;
	}

	@Override
	protected void smartDelete(List<Individuo> individuos) throws GeneticBusinessException {
		try {
			if (individuos.size() > 0) {
				MongoIndividuoManager individuoManager = new MongoIndividuoManager();
				individuoManager.delete(individuos);
				LogUtil.logTime("Individuos consultados: " + individuos.size(), 1);
			}
		} catch (GeneticBusinessException e) {
			throw new GeneticBusinessException("smartDelete", e);
		}
	}
}

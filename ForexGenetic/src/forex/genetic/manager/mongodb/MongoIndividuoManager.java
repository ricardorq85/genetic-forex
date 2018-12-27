package forex.genetic.manager.mongodb;

import static forex.genetic.util.LogUtil.logTime;

import java.util.Arrays;
import java.util.List;

import forex.genetic.entities.Individuo;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.manager.GeneticManager;
import forex.genetic.util.jdbc.mongodb.MongoDataClient;

public class MongoIndividuoManager extends GeneticManager {

	private MongoDataClient dataClient;

	public MongoIndividuoManager() throws GeneticBusinessException {
		try {
			dataClient = (MongoDataClient) DriverDBFactory.createDataClient("mongodb");
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException("MongoIndividuoManager:constructor", e);
		}
	}

	public void delete(Individuo individuo) throws GeneticBusinessException {
		this.delete(Arrays.asList(individuo));
	}

	public void delete(List<Individuo> individuos) throws GeneticBusinessException {
		try {
			for (Individuo individuo : individuos) {
				int r_operaciones = dataClient.getDaoOperaciones().deleteByIndividuo(individuo);
				logTime("Individuo: " + individuo.getId() + ". Borrados OPERACIONES = " + r_operaciones, 1);
				int r_estadisticas = dataClient.getDaoEstadistica().deleteByIndividuo(individuo);
				logTime("Individuo: " + individuo.getId() + ". Borrados ESTADISTICAS = " + r_estadisticas, 1);
				int r_tendencia = dataClient.getDaoTendencia().deleteByIndividuo(individuo);
				logTime("Individuo: " + individuo.getId() + ". Borrados TENDENCIA = " + r_tendencia, 1);
				
				//TODO RROJASQ Insertar en individuoBorrado
				//individuoDAO.smartDelete(individuo.getId(), tipoProceso, individuo.getIdParent1());
				int r_individuo = dataClient.getDaoIndividuo().deleteByIndividuo(individuo);
				logTime("Individuo: " + individuo.getId() + ". Borrados INDIVIDUO = " + r_individuo, 1);
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException("smartDelete", e);
		}
	}

}

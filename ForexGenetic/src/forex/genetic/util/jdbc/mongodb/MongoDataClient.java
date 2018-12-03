package forex.genetic.util.jdbc.mongodb;

import com.mongodb.MongoClient;

import forex.genetic.dao.IDatoHistoricoDAO;
import forex.genetic.dao.IGeneticDAO;
import forex.genetic.dao.IIndividuoDAO;
import forex.genetic.dao.IOperacionesDAO;
import forex.genetic.dao.IParametroDAO;
import forex.genetic.dao.IProcesoEjecucionDAO;
import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.dao.mongodb.MongoIndividuoDAO;
import forex.genetic.dao.mongodb.MongoOperacionesDAO;
import forex.genetic.dao.mongodb.MongoParametroDAO;
import forex.genetic.dao.mongodb.MongoTendenciaDAO;
import forex.genetic.entities.Tendencia;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.entities.mongo.MongoOrder;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.jdbc.DataClient;

public class MongoDataClient extends DataClient<MongoClient, MongoIndividuo, MongoOrder> {

	public MongoDataClient(MongoClient client) {
		super(client);
	}

	@Override
	public IDatoHistoricoDAO getDaoDatoHistorico() throws GeneticDAOException {
		if (daoDatoHistorico == null) {
			daoDatoHistorico = new MongoDatoHistoricoDAO();
		}
		return daoDatoHistorico;
	}

	@Override
	public IGeneticDAO<Tendencia> getDaoTendencia() throws GeneticDAOException {
		if (daoTendencia == null) {
			daoTendencia = new MongoTendenciaDAO();
		}
		return daoTendencia;
	}

	@Override
	public IParametroDAO getDaoParametro() throws GeneticDAOException {
		if (daoParametro == null) {
			daoParametro = new MongoParametroDAO();
		}
		return daoParametro;
	}

	@Override
	public IIndividuoDAO<MongoIndividuo> getDaoIndividuo() throws GeneticDAOException {
		if (daoIndividuo == null) {
			daoIndividuo = new MongoIndividuoDAO();
		}
		return daoIndividuo;
	}

	@Override
	public IOperacionesDAO<MongoOrder> getDaoOperaciones() throws GeneticDAOException {
		if (daoOperacion == null) {
			daoOperacion = new MongoOperacionesDAO();
		}
		return daoOperacion;
	}

	@Override
	public void close() throws GeneticDAOException {
	}

	@Override
	public void commit() throws GeneticDAOException {
	}

	@Override
	public IProcesoEjecucionDAO getDaoProcesoEjecucion() throws GeneticDAOException {
		throw new UnsupportedOperationException(
				"Mongo no tiene Proceso Ejecucion, se maneja directamente en el Individuo");
	}

}

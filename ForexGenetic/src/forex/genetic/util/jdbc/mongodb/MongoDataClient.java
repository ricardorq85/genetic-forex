package forex.genetic.util.jdbc.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import forex.genetic.dao.IDatoAdicionalTPODAO;
import forex.genetic.dao.IDatoHistoricoDAO;
import forex.genetic.dao.IEstadisticaDAO;
import forex.genetic.dao.IIndividuoDAO;
import forex.genetic.dao.IOperacionesDAO;
import forex.genetic.dao.IParametroDAO;
import forex.genetic.dao.IProcesoEjecucionDAO;
import forex.genetic.dao.ITendenciaDAO;
import forex.genetic.dao.ITendenciaParaOperarDAO;
import forex.genetic.dao.mongodb.MongoDatoAdicionalTPODAO;
import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.dao.mongodb.MongoEstadisticasIndividuoDAO;
import forex.genetic.dao.mongodb.MongoIndividuoBorradoDAO;
import forex.genetic.dao.mongodb.MongoIndividuoDAO;
import forex.genetic.dao.mongodb.MongoOperacionesDAO;
import forex.genetic.dao.mongodb.MongoParametroDAO;
import forex.genetic.dao.mongodb.MongoTendenciaDAO;
import forex.genetic.dao.mongodb.MongoTendenciaParaOperarDAO;
import forex.genetic.dao.mongodb.MongoTendenciaUltimosDatosDAO;
import forex.genetic.entities.mongo.MongoEstadistica;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.entities.mongo.MongoOrder;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.jdbc.DataClient;

public class MongoDataClient extends DataClient<MongoClient, MongoIndividuo, MongoOrder, MongoEstadistica> {

	public MongoDataClient(MongoClient client) {
		super(client, DataClient.DriverType.MONGODB);
	}
	
	private MongoDatabase getDatabase() {
		return client.getDatabase(PropertiesManager.getPair().toLowerCase());
	}

	@Override
	public void close() {
		client.close();
	}

	@Override
	public void commit() throws GeneticDAOException {
	}

	@Override
	public IDatoHistoricoDAO getDaoDatoHistorico() throws GeneticDAOException {
		if (daoDatoHistorico == null) {
			daoDatoHistorico = new MongoDatoHistoricoDAO(getDatabase());
		}
		return daoDatoHistorico;
	}

	@Override
	public ITendenciaDAO getDaoTendencia() throws GeneticDAOException {
		if (daoTendencia == null) {
			daoTendencia = new MongoTendenciaDAO(getDatabase());
		}
		return daoTendencia;
	}

	@Override
	public ITendenciaDAO getDaoTendenciaUltimosDatos() throws GeneticDAOException {
		if (daoTendenciaUltimosDatos == null) {
			daoTendenciaUltimosDatos = new MongoTendenciaUltimosDatosDAO(getDatabase());
		}
		return daoTendenciaUltimosDatos;
	}

	@Override
	public IParametroDAO getDaoParametro() throws GeneticDAOException {
		if (daoParametro == null) {
			daoParametro = new MongoParametroDAO(getDatabase());
		}
		return daoParametro;
	}

	@Override
	public IIndividuoDAO<MongoIndividuo> getDaoIndividuo() throws GeneticDAOException {
		if (daoIndividuo == null) {
			daoIndividuo = new MongoIndividuoDAO(getDatabase());
		}
		return daoIndividuo;
	}

	@Override
	public IOperacionesDAO<MongoOrder> getDaoOperaciones() throws GeneticDAOException {
		if (daoOperacion == null) {
			daoOperacion = new MongoOperacionesDAO(getDatabase());
		}
		return daoOperacion;
	}

	@Override
	public IProcesoEjecucionDAO getDaoProcesoEjecucion() throws GeneticDAOException {
		throw new UnsupportedOperationException(
				"Mongo no tiene Proceso Ejecucion, se maneja directamente en el Individuo");
	}

	@Override
	public IEstadisticaDAO<MongoEstadistica> getDaoEstadistica() throws GeneticDAOException {
		if (daoEstadistica == null) {
			daoEstadistica = new MongoEstadisticasIndividuoDAO(getDatabase());
		}
		return daoEstadistica;
	}

	@Override
	public ITendenciaParaOperarDAO getDaoTendenciaParaOperar() throws GeneticDAOException {
		if (daoTendenciaParaOperar == null) {
			daoTendenciaParaOperar = new MongoTendenciaParaOperarDAO(getDatabase());
		}
		return daoTendenciaParaOperar;
	}

	@Override
	public IDatoAdicionalTPODAO getDaoDatoAdicionalTPO() throws GeneticDAOException {
		if (daoDatoAdicionalTPO == null) {
			daoDatoAdicionalTPO= new MongoDatoAdicionalTPODAO(getDatabase());
		}
		return daoDatoAdicionalTPO;
	}

	@Override
	public IIndividuoDAO<MongoIndividuo> getDaoIndividuoBorrado() throws GeneticDAOException {
		if (daoIndividuoBorrado == null) {
			daoIndividuoBorrado = new MongoIndividuoBorradoDAO(getDatabase());
		}
		return daoIndividuoBorrado;
	}

}

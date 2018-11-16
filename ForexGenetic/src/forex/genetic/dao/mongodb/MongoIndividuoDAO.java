package forex.genetic.dao.mongodb;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;

import forex.genetic.dao.IIndividuoDAO;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.IndividuoOptimo;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.controller.IndicadorController;

public class MongoIndividuoDAO extends MongoGeneticDAO<MongoIndividuo> implements IIndividuoDAO<MongoIndividuo> {

	public MongoIndividuoDAO() {
		super("individuo", true);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		this.collection.createIndex(Indexes.ascending("idIndividuo"), indexOptions);
	}

	public MongoIndividuo consultarById(String idIndividuo) {
		MongoIndividuo obj = null;
		MongoCursor<Document> cursor = this.collection.find(Filters.eq("idIndividuo", idIndividuo)).iterator();
		if (cursor.hasNext()) {
			obj = mapper.helpOne(cursor.next());
		}
		return obj;
	}

	@Override
	public List<? extends IndividuoEstrategia> getListByProcesoEjecucion(String filtroAdicional, Date fechaHistorico) {
		Bson filtro = Filters.ne("procesoEjecucion.maxfechaHistorico", fechaHistorico);
		Bson ordenador = Sorts.orderBy(Sorts.ascending("procesoEjecucion.maxFechaHistorico"),
				Sorts.descending("idIndividuo"));
		MongoCursor<Document> cursor = collection.find(filtro).sort(ordenador).limit(10).iterator();
		return mapper.helpList(cursor);
	}

	@Override
	public int duracionPromedioMinutos(String idIndividuo) throws GeneticDAOException {
		// TODO Auto-generated method stub
		//Accumulators.avg(fieldName, expression);
		Aggregates.group(id, fieldAccumulators);
		//Projections
		
		return 0;
	}

	@Override
	public List<Date> consultarPuntosApertura(Date fechaMayorQue, String idIndividuo) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Date> consultarPuntosApertura(DateInterval rango, String idIndividuo) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void crearVistaIndicadoresIndividuo(String viewName, String idIndividuo) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertarIndividuoIndicadoresColumnas(String idIndividuo) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Individuo> consultarIndividuosPadreRepetidos(String tipoProceso) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuosStopLossInconsistente(int sl) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuosStopLossInconsistente(int sl, String idIndividuo)
			throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuosCantidadLimite(double porcentajeLimite) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuosCantidadLimite(double porcentajeLimite, String idIndividuo)
			throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void smartDelete(String idIndividuo, String causaBorrado, String idPadre) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Individuo> consultarIndividuosRepetidosOperaciones(Individuo individuoPadre)
			throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuosRepetidos() throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Individuo consultarIndividuo(String idIndividuo) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuoHijoRepetidoOperaciones(Individuo individuoHijo)
			throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuoHijoRepetido(Individuo individuoHijo) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void consultarDetalleIndividuoProceso(Individuo individuo, Date fechaHistorico) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void consultarDetalleIndividuo(IndicadorController indicadorController, Individuo individuo)
			throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertIndividuo(IndividuoEstrategia individuo) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertIndicadorIndividuo(IndicadorController indicadorController, IndividuoEstrategia individuo)
			throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<IndividuoOptimo> consultarIndividuosOptimos() throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCountIndicadoresOpen(Individuo individuo) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Individuo> consultarIndividuosResumenSemanal(Date fechaInicial, Date fechaFinal)
			throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuosRandom(Date fechaInicial, Date fechaFinal, int cantidad)
			throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuosRandom(int cantidad) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuosIndicadoresCloseMinimos(int minimo) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuosIndicadoresCloseMinimos(int minimo, String id)
			throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuosIntervaloIndicadores() throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuosIntervaloIndicadores(String idIndividuo) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}
}

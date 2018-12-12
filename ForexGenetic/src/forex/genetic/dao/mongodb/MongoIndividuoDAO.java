package forex.genetic.dao.mongodb;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;

import forex.genetic.dao.IIndividuoDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.IndividuoOptimo;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.controller.IndicadorController;

public class MongoIndividuoDAO extends MongoGeneticDAO<MongoIndividuo> implements IIndividuoDAO<MongoIndividuo> {

	public MongoIndividuoDAO() throws GeneticDAOException {
		super("individuo", true);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		this.collection.createIndex(Indexes.ascending("idIndividuo"), indexOptions);
	}

	@Override
	public MongoIndividuo consultarById(String idIndividuo) {
		MongoIndividuo obj = null;
		MongoCursor<Document> cursor = this.collection.find(Filters.eq("idIndividuo", idIndividuo)).iterator();
		if (cursor.hasNext()) {
			obj = getMapper().helpOne(cursor.next());
		}
		return obj;
	}

	@Override
	public List<? extends IndividuoEstrategia> getListByProcesoEjecucion(String filtroAdicional, Date fechaHistorico) {
		Bson filtroProcesoEjecucionNull = Filters.exists("procesoEjecucion.maxFechaHistorico", false);
		Bson filtroFechaHistorica = Filters.ne("procesoEjecucion.maxFechaHistorico", fechaHistorico);
		Bson filtroOr = Filters.or(filtroProcesoEjecucionNull, filtroFechaHistorica);
		Bson ordenador = Sorts.orderBy(Sorts.ascending("procesoEjecucion.maxFechaHistorico"),
				Sorts.descending("idIndividuo"));

		Bson filtroIndividuo = Filters.eq("idIndividuo", "1394841600000.83");
		Bson filtroCompleto = Filters.and(filtroIndividuo, filtroOr);

		MongoCursor<Document> cursor = collection.find(filtroCompleto).sort(ordenador).limit(10).iterator();
		return getMapper().helpList(cursor);
	}

	@Override
	public void crearVistaIndicadoresIndividuo(String viewName, String idIndividuo) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void insertarIndividuoIndicadoresColumnas(String idIndividuo) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuosPadreRepetidos(String tipoProceso) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuosStopLossInconsistente(int sl) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuosStopLossInconsistente(int sl, String idIndividuo)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuosCantidadLimite(double porcentajeLimite) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuosCantidadLimite(double porcentajeLimite, String idIndividuo)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void smartDelete(String idIndividuo, String causaBorrado, String idPadre) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuosRepetidosOperaciones(Individuo individuoPadre)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuosRepetidos() throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public Individuo consultarIndividuo(String idIndividuo) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuoHijoRepetidoOperaciones(Individuo individuoHijo)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuoHijoRepetido(Individuo individuoHijo) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void consultarDetalleIndividuoProceso(Individuo individuo, Date fechaHistorico) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void consultarDetalleIndividuo(IndicadorController indicadorController, Individuo individuo)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void insertIndividuo(IndividuoEstrategia individuo) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void insertIndicadorIndividuo(IndicadorController indicadorController, IndividuoEstrategia individuo)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<IndividuoOptimo> consultarIndividuosOptimos() throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public int getCountIndicadoresOpen(Individuo individuo) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuosResumenSemanal(Date fechaInicial, Date fechaFinal)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<MongoIndividuo> consultarIndividuosRandom(int cantidad) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuosIndicadoresCloseMinimos(int minimo) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuosIndicadoresCloseMinimos(int minimo, String id)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuosIntervaloIndicadores() throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuosIntervaloIndicadores(String idIndividuo) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuosParaBorrar(Date fechaLimite) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuosParaBorrar(String idIndividuo, Date fechaLimite)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuosParaBorrar(int minutos) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuosParaBorrar(String idIndividuo, int minutos) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<MongoIndividuo> consultarIndividuosRandom(Date fechaInicial, Date fechaFinal, int cantidad)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}
}

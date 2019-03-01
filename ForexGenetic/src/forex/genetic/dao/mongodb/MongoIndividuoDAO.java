package forex.genetic.dao.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;

import forex.genetic.dao.IIndividuoDAO;
import forex.genetic.dao.helper.mongodb.MongoIndividuoMapper;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.IndividuoOptimo;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.util.RandomUtil;

public class MongoIndividuoDAO extends MongoGeneticDAO<MongoIndividuo> implements IIndividuoDAO<MongoIndividuo> {

	public MongoIndividuoDAO(MongoDatabase db) throws GeneticDAOException {
		super(db, "individuo", true);
	}

	public MongoIndividuoDAO(MongoDatabase db, String name, boolean configure) {
		super(db, name, configure);
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
	public void insertIndicadorIndividuo(IndicadorController indicadorController, IndividuoEstrategia individuo)
			throws GeneticDAOException {
		return;
	}

	@Override
	public void insertarIndividuoIndicadoresColumnas(String idIndividuo) throws GeneticDAOException {
		return;
	}

	private void addRandomSort(List<Bson> sorts, String field) {
		if (RandomUtil.nextBoolean()) {
			if (RandomUtil.nextBoolean()) {
				sorts.add(Sorts.ascending(field));
			} else {
				sorts.add(Sorts.descending(field));
			}
		}
	}

	@Override
	public List<? extends IndividuoEstrategia> getListByProcesoEjecucion(String filtroAdicional, Date fechaHistorico,
			int cantidadIndividuos) {
		Bson filtroProcesoEjecucionNull = Filters.exists("procesoEjecucion.maxFechaHistorico", false);
		Bson filtroFechaHistorica = Filters.lt("procesoEjecucion.maxFechaHistorico", fechaHistorico);
		Bson filtroOr = Filters.or(filtroProcesoEjecucionNull, filtroFechaHistorica);
		Bson bsonFiltroAdicional = Filters.regex("idIndividuo", filtroAdicional + "$");

		List<Bson> sorts = new ArrayList<>();
		sorts.add(Sorts.ascending("procesoEjecucion.maxFechaHistorico"));
		this.addRandomSort(sorts, "takeProfit");
		this.addRandomSort(sorts, "stopLoss");
		this.addRandomSort(sorts, "creationDate");

		Bson ordenador = Sorts.orderBy(sorts);
		// bsonFiltroAdicional = Filters.and(Filters.regex("idIndividuo",
		// "1547761146791.139"));
		// Filters.eq("tipoIndividuo",
		// Constants.IndividuoType.INDICADOR_GANADOR.name()));
//		Bson filtroIndividuo = Filters.regex("idIndividuo", "1544908361588.*");
//		Bson filtroCompleto = Filters.and(filtroIndividuo, filtroOr);

		Bson filtroCompleto = Filters.and(filtroOr, bsonFiltroAdicional);
		MongoCursor<Document> cursor = collection.find(filtroCompleto).sort(ordenador).limit(cantidadIndividuos)
				.iterator();
		return getMapper().helpList(cursor);
	}

	@Override
	public List<Individuo> consultarIndividuoHijoRepetido(Individuo individuo) throws GeneticDAOException {
		List<Individuo> list = new ArrayList<>();
		int cantidad = 10;

		List<Bson> filtros = new ArrayList<>();
		filtros.add(Filters.ne("idIndividuo", individuo.getId()));
		//filtros.add(Filters.lt("creationDate", individuo.getCreationDate()));
		filtros.add(Filters.lt("idIndividuo", individuo.getId()));
		filtros.add(Filters.eq("takeProfit", individuo.getTakeProfit()));
		filtros.add(Filters.eq("stopLoss", individuo.getStopLoss()));

		this.adicionarFiltroIndicadores(individuo.getOpenIndicators(), filtros);

		Bson bsonFiltrosCompletos = Filters.and(filtros);

		MongoCursor<Document> cursor = collection.find(bsonFiltrosCompletos).limit(cantidad).iterator();

		list.addAll(getMapper().helpList(cursor));

		return list;
	}

	private void adicionarFiltroIndicadores(List<? extends Indicator> indicadores, List<Bson> filtros) {
		IndicadorController indicadorController = ControllerFactory
				.createIndicadorController(ControllerFactory.ControllerType.Individuo);
		for (int i = 0; i < indicadorController.getIndicatorNumber(); i++) {
			// for (int i = 0; i < 7; i++) {
//			IndicadorManager<?> managerInstance = indicadorController.getManagerInstance(i);
			StringBuilder nombreIndicador = new StringBuilder("openIndicadores").append(".")
					.append(indicadorController.getIndicatorName(i));
			if (indicadores.get(i) != null) {
				IntervalIndicator intervalIndicator = ((IntervalIndicator) indicadores.get(i));
				if (intervalIndicator.getInterval() != null) {
					StringBuilder nombreIndicadorLow = new StringBuilder(nombreIndicador).append(".low");
					StringBuilder nombreIndicadorHigh = new StringBuilder(nombreIndicador).append(".high");
					filtros.add(Filters.eq(nombreIndicadorLow.toString(),
							intervalIndicator.getInterval().getLowInterval()));
					filtros.add(Filters.eq(nombreIndicadorHigh.toString(),
							intervalIndicator.getInterval().getHighInterval()));
				} else {
					filtros.add(Filters.eq(nombreIndicador.toString(), null));
				}
			}
		}
	}

	@Override
	public void insertIndividuoEstrategia(IndividuoEstrategia obj) throws GeneticDAOException {
		Document doc = new Document(((MongoIndividuoMapper) getMapper()).toMapIndividuoEstrategia(obj));
		this.collection.insertOne(doc);
	}

	@Override
	public void crearVistaIndicadoresIndividuo(String viewName, String idIndividuo) throws GeneticDAOException {
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
	public void consultarDetalleIndividuoProceso(Individuo individuo, Date fechaHistorico) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void consultarDetalleIndividuo(IndicadorController indicadorController, Individuo individuo)
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

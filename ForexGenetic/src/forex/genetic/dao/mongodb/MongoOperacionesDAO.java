package forex.genetic.dao.mongodb;

import static forex.genetic.dao.helper.mongodb.MongoOperacioneHelper.getAccumulators;
import static forex.genetic.dao.helper.mongodb.MongoOperacioneHelper.getFiltrosParaNegativos;
import static forex.genetic.dao.helper.mongodb.MongoOperacioneHelper.getFiltrosParaPositivos;
import static forex.genetic.dao.helper.mongodb.MongoOperacioneHelper.getFiltrosParaTotales;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.BsonField;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;

import forex.genetic.dao.IOperacionesDAO;
import forex.genetic.dao.helper.mongodb.MongoEstadisticaIndividuoMapper;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.ParametroConsultaEstadistica;
import forex.genetic.entities.ParametroOperacionPeriodo;
import forex.genetic.entities.mongo.MongoEstadistica;
import forex.genetic.entities.mongo.MongoOrder;
import forex.genetic.exception.GeneticDAOException;

/**
 *
 * @author ricardorq85
 */
public class MongoOperacionesDAO extends MongoGeneticDAO<MongoOrder> implements IOperacionesDAO<MongoOrder> {

	public MongoOperacionesDAO() throws GeneticDAOException {
		this(true);
	}

	@Override
	public int deleteByIndividuo(Individuo individuo) {
		setCollection("operacionesPositivas", false);
		super.deleteByIndividuo(individuo);
		setCollection("operacionesNegativas", false);
		super.deleteByIndividuo(individuo);
		setCollection("operacion", false);
		return super.deleteByIndividuo(individuo);
	}

	public MongoOperacionesDAO(boolean configure) {
		super("operacion", configure);
		setCollection("operacionesPositivas", true);
		setCollection("operacionesNegativas", true);
		setCollection("operacion", true);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		this.collection.createIndex(Indexes.ascending("idIndividuo", "fechaApertura"), indexOptions);
	}

	@SuppressWarnings("unchecked")
	@Override
	public MongoEstadistica consultarEstadisticas(Individuo individuo,
			ParametroConsultaEstadistica parametroConsultaEstadistica) throws GeneticDAOException {

		setCollection("operacion", false);
		List<Bson> filtrosTotales = getFiltrosParaTotales(individuo, parametroConsultaEstadistica);
		MongoEstadistica estadisticaTotales = consultarEstadisticasIntern(filtrosTotales, null, "");
		estadisticaTotales.setPipsModa(getModa(filtrosTotales, "$pips"));
		estadisticaTotales.setDuracionModa(getModa(filtrosTotales, "$duracionMinutos"));
		estadisticaTotales.setPipsModaRetroceso(getModa(filtrosTotales, "$maxPipsRetroceso"));

		setCollection("operacionesPositivas", false);
		List<Bson> filtrosPositivos = getFiltrosParaPositivos(individuo, parametroConsultaEstadistica);
		MongoEstadistica estadisticaPositivos = consultarEstadisticasIntern(filtrosPositivos, estadisticaTotales,
				"Positivos");
		estadisticaPositivos.setPipsModaPositivos(getModa(filtrosPositivos, "$pips"));
		estadisticaPositivos.setDuracionModaPositivos(getModa(filtrosPositivos, "$duracionMinutos"));
		estadisticaPositivos.setPipsModaRetrocesoPositivos(getModa(filtrosPositivos, "$maxPipsRetroceso"));

		setCollection("operacionesNegativas", false);
		List<Bson> filtrosNegativos = getFiltrosParaNegativos(individuo, parametroConsultaEstadistica);
		MongoEstadistica estadisticaCompleta = consultarEstadisticasIntern(filtrosNegativos, estadisticaPositivos,
				"Negativos");
		estadisticaCompleta.setPipsModaNegativos(getModa(filtrosNegativos, "$pips"));
		estadisticaCompleta.setDuracionModaNegativos(getModa(filtrosNegativos, "$duracionMinutos"));
		estadisticaCompleta.setPipsModaRetrocesoNegativos(getModa(filtrosNegativos, "$maxPipsRetroceso"));

		setCollection("operacion", false);
		estadisticaCompleta.setIdIndividuo(individuo.getId());

		return estadisticaCompleta;
	}

	private MongoEstadistica consultarEstadisticasIntern(List<Bson> filtros, MongoEstadistica estadisticaPrevia,
			String suffix) throws GeneticDAOException {
		List<BsonField> datosAcumulados = getAccumulators(suffix);

		Document doc = this.collection.aggregate(
				Arrays.asList(Aggregates.match(Filters.and(filtros)), Aggregates.group("estadistica", datosAcumulados)))
				.first();
		MongoEstadisticaIndividuoMapper mapper = new MongoEstadisticaIndividuoMapper();
		MongoEstadistica obj = estadisticaPrevia;
		if (estadisticaPrevia == null) {
			obj = new MongoEstadistica();
		}
		if (doc != null) {
			mapper.helpOne(doc, obj);
		}
		return obj;
	}

	private double getModa(List<Bson> filtros, String fieldName) throws GeneticDAOException {
		double value = 0.0D;
		Document doc = this.collection.aggregate(Arrays.asList(Aggregates.match(Filters.and(filtros)),
				Aggregates.group(fieldName, Accumulators.sum("count", 1)), Aggregates.sort(Sorts.descending("count")),
				Aggregates.limit(1))).first();
		if (doc != null) {
			if (doc.get("_id") != null) {
				value = new Double(String.valueOf(doc.get("_id")));
			}
		}
		return value;
	}

	@Override
	public long duracionPromedioMinutos(String idIndividuo) throws GeneticDAOException {
		long avgDuracionMinutos = 0;
		Document doc = this.collection
				.aggregate(Arrays
						.asList(Aggregates.group(null, Accumulators.avg("avgDuracionMinutos", "$duracionMinutos"))))
				.first();
		if (doc != null) {
			if (doc.get("avgDuracionMinutos") != null) {
				avgDuracionMinutos = doc.getLong("avgDuracionMinutos");
			}
		}
		return avgDuracionMinutos;
	}

	private boolean setCollectionInternByPips(MongoOrder obj) {
		if (obj.getCloseDate() != null) {
			if (obj.getPips() > 0) {
				setCollection("operacionesPositivas", false);
				return true;
			} else {
				setCollection("operacionesNegativas", false);
				return true;
			}
		}
		return false;
	}

	public void insertIfNoExists(MongoOrder obj) {
		setCollection("operacion", false);
		super.insertIfNoExists(obj);
		if (setCollectionInternByPips(obj)) {
			super.insertIfNoExists(obj);
			setCollection("operacion", false);
		}
	}

	@Override
	public void insertOrUpdate(MongoOrder obj) {
		setCollection("operacion", false);
		super.insertOrUpdate(obj);
		if (setCollectionInternByPips(obj)) {
			super.insertOrUpdate(obj);
			setCollection("operacion", false);
		}
	}

	@Override
	public void insert(MongoOrder obj) {
		setCollection("operacion", false);
		super.insert(obj);
		if (setCollectionInternByPips(obj)) {
			super.insert(obj);
			setCollection("operacion", false);
		}
	}

	@Override
	public void update(MongoOrder obj) {
		setCollection("operacion", false);
		super.update(obj);
		if (setCollectionInternByPips(obj)) {
			super.update(obj);
			setCollection("operacion", false);
		}
	}

	@Override
	public List<DateInterval> consultarVigencias(Date fechaPeriodo) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarOperacionesXPeriodo(Date fechaInicial, Date fechaFinal) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public double consultarPipsXAgrupacion(String agrupador) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuoOperacionActiva(Date fechaBase) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuoOperacionActiva(Date fechaBase, int filas) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarIndividuoOperacionActiva(Date fechaBase, Date fechaFin, int filas)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public Individuo consultarIndividuoOperacionActiva(String idIndividuo, Date fechaBase, int filas)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void update(Individuo individuo, MongoOrder operacion, Date fechaApertura) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void updateMaximosRetrocesoOperacion(Individuo individuo, MongoOrder currentOrder)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void insert(Individuo individuo, List<MongoOrder> operaciones) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public Individuo consultarOperacionesIndividuoRetroceso(Individuo ind, Date fechaMaximo)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<Individuo> consultarOperacionesIndividuoRetroceso(Date fechaMaximo) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public int cleanOperacionesPeriodo() throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public int insertOperacionesPeriodo(ParametroOperacionPeriodo param) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void actualizarOperacionesPositivasYNegativas() throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public List<MongoOrder> consultarOperacionesActivas(Date fechaBase, Date fechaFin, int filas)
			throws GeneticDAOException {
		int cantidad = 10;
		Bson filtroFechaApertura = Filters.lt("fechaApertura", fechaBase);
		Bson filtroFechaCierre = Filters.or(Filters.exists("fechaCierre", false), Filters.eq("fechaCierre", null),
				Filters.gt("fechaCierre", fechaBase));
		Bson filtroIndividuo = Filters.eq("idIndividuo", "1394755200000.32");

		Bson bsonFiltrosCompletos = Filters.and(filtroFechaApertura, filtroFechaCierre, filtroIndividuo);
		MongoCursor<Document> cursor = this.collection
				.aggregate(Arrays.asList(Aggregates.match(Filters.and(bsonFiltrosCompletos)),
						Aggregates.sample(cantidad), Aggregates.sort(Sorts.orderBy(Sorts.ascending("fechaApertura")))))
				.iterator();

		List<MongoOrder> list = getMapper().helpList(cursor);
		return list;
	}
}

package forex.genetic.dao.mongodb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
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
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class MongoOperacionesDAO extends MongoGeneticDAO<MongoOrder> implements IOperacionesDAO<MongoOrder> {

	public MongoOperacionesDAO() throws GeneticDAOException {
		this(true);
	}

	public MongoOperacionesDAO(boolean configure) throws GeneticDAOException {
		super("operacion", configure);
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

		List<Bson> filtrosTotales = getFiltrosParaTotales(individuo, parametroConsultaEstadistica);

		//LogUtil.logTime(Filters.and(filtrosTotales)
			//	.toBsonDocument(Document.class, MongoClient.getDefaultCodecRegistry()).toJson(), 1);

		MongoEstadistica estadisticaTotales = consultarEstadisticasIntern(filtrosTotales, null, "");
		estadisticaTotales.setPipsModa(getModa(filtrosTotales, "$pips"));
		estadisticaTotales.setDuracionModa(getModa(filtrosTotales, "$duracionMinutos"));
		estadisticaTotales.setPipsModaRetroceso(getModa(filtrosTotales, "$maxPipsRetroceso"));

		List<Bson> filtrosPositivos = getFiltrosParaPositivos(individuo, parametroConsultaEstadistica);
		MongoEstadistica estadisticaPositivos = consultarEstadisticasIntern(filtrosPositivos, estadisticaTotales,
				"Positivos");
		estadisticaPositivos.setPipsModaPositivos(getModa(filtrosPositivos, "$pips"));
		estadisticaPositivos.setDuracionModaPositivos(getModa(filtrosPositivos, "$duracionMinutos"));
		estadisticaPositivos.setPipsModaRetrocesoPositivos(getModa(filtrosPositivos, "$maxPipsRetroceso"));

		List<Bson> filtrosNegativos = getFiltrosParaNegativos(individuo, parametroConsultaEstadistica);

//		LogUtil.logTime(Filters.and(filtrosNegativos)
	//			.toBsonDocument(Document.class, MongoClient.getDefaultCodecRegistry()).toJson(), 1);

		MongoEstadistica estadisticaCompleta = consultarEstadisticasIntern(filtrosNegativos, estadisticaPositivos,
				"Negativos");
		
		estadisticaCompleta.setPipsModaNegativos(getModa(filtrosNegativos, "$pips"));
		estadisticaCompleta.setDuracionModaNegativos(getModa(filtrosNegativos, "$duracionMinutos"));
		estadisticaCompleta.setPipsModaRetrocesoNegativos(getModa(filtrosNegativos, "$maxPipsRetroceso"));

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

	private List<BsonField> getAccumulators(String suffix) {
		List<BsonField> datosAcumulados = new ArrayList<BsonField>();
		datosAcumulados.add(Accumulators.sum(new StringBuilder("cantidad").append(suffix).toString(), 1));
		datosAcumulados.add(
				Accumulators.sum(new StringBuilder("duracionTotal").append(suffix).toString(), "$duracionMinutos"));

		datosAcumulados.add(Accumulators.sum(new StringBuilder("pips").append(suffix).toString(), "$pips"));
		datosAcumulados.add(Accumulators.min(new StringBuilder("pipsMinimos").append(suffix).toString(), "$pips"));
		datosAcumulados.add(Accumulators.max(new StringBuilder("pipsMaximos").append(suffix).toString(), "$pips"));
		datosAcumulados.add(Accumulators.avg(new StringBuilder("pipsPromedio").append(suffix).toString(), "$pips"));

		datosAcumulados.add(
				Accumulators.min(new StringBuilder("duracionMinima").append(suffix).toString(), "$duracionMinutos"));
		datosAcumulados.add(
				Accumulators.max(new StringBuilder("duracionMaxima").append(suffix).toString(), "$duracionMinutos"));
		datosAcumulados.add(
				Accumulators.avg(new StringBuilder("duracionPromedio").append(suffix).toString(), "$duracionMinutos"));
		datosAcumulados.add(Accumulators.stdDevPop(new StringBuilder("duracionDesvEstandard").append(suffix).toString(),
				"$duracionMinutos"));

		datosAcumulados.add(Accumulators.min(new StringBuilder("pipsMinimosRetroceso").append(suffix).toString(),
				"$maxPipsRetroceso"));
		datosAcumulados.add(Accumulators.max(new StringBuilder("pipsMaximosRetroceso").append(suffix).toString(),
				"$maxPipsRetroceso"));
		datosAcumulados.add(Accumulators.avg(new StringBuilder("pipsPromedioRetroceso").append(suffix).toString(),
				"$maxPipsRetroceso"));

		return datosAcumulados;
	}

	private List<Bson> getFiltrosParaTotales(Individuo individuo,
			ParametroConsultaEstadistica parametroConsultaEstadistica) {
		List<Bson> filtros = getFiltrosParaEstadistica(individuo, parametroConsultaEstadistica);
		List<Bson> filtrosOr = new ArrayList<Bson>();
		if (parametroConsultaEstadistica.getRetroceso() != null) {
			if (parametroConsultaEstadistica.getRetroceso() > 0.0D) {
				filtrosOr.add(Filters.and(Filters.lte("pips", 0),
						Filters.gte("maxPipsRetroceso", parametroConsultaEstadistica.getRetroceso())));
				filtrosOr.add(Filters.and(Filters.gt("pips", 0),
						Filters.gte("pips", parametroConsultaEstadistica.getRetroceso())));
			} else {
				filtrosOr.add(Filters.and(Filters.gt("pips", 0),
						Filters.lte("maxPipsRetroceso", parametroConsultaEstadistica.getRetroceso())));
				filtrosOr.add(Filters.and(Filters.lte("pips", 0),
						Filters.lte("pips", parametroConsultaEstadistica.getRetroceso())));
			}
			filtros.add(Filters.or(filtrosOr));
		}
		return filtros;
	}

	private List<Bson> getFiltrosParaPositivos(Individuo individuo,
			ParametroConsultaEstadistica parametroConsultaEstadistica) {
		List<Bson> filtros = getFiltrosParaEstadistica(individuo, parametroConsultaEstadistica);
		filtros.add(Filters.gt("pips", 0.0D));
		if (parametroConsultaEstadistica.getRetroceso() != null) {
			if (parametroConsultaEstadistica.getRetroceso() > 0.0D) {
				filtros.add(Filters.gte("pips", parametroConsultaEstadistica.getRetroceso()));
			} else {
				filtros.add(Filters.lte("maxPipsRetroceso", parametroConsultaEstadistica.getRetroceso()));
			}
		}
		return filtros;
	}

	private List<Bson> getFiltrosParaNegativos(Individuo individuo,
			ParametroConsultaEstadistica parametroConsultaEstadistica) {
		List<Bson> filtros = getFiltrosParaEstadistica(individuo, parametroConsultaEstadistica);
		filtros.add(Filters.lte("pips", 0.0D));
		if (parametroConsultaEstadistica.getRetroceso() != null) {
			if (parametroConsultaEstadistica.getRetroceso() < 0.0D) {
				filtros.add(Filters.lte("pips", parametroConsultaEstadistica.getRetroceso()));
			} else {
				filtros.add(Filters.gte("maxPipsRetroceso", parametroConsultaEstadistica.getRetroceso()));
			}
		}
		return filtros;
	}

	private List<Bson> getFiltrosParaEstadistica(Individuo individuo,
			ParametroConsultaEstadistica parametroConsultaEstadistica) {
		List<Bson> filtros = new ArrayList<Bson>();
		filtros.add(Filters.eq("idIndividuo", individuo.getId()));
		filtros.add(Filters.and(Filters.exists("fechaCierre", true),
				Filters.lt("fechaCierre", parametroConsultaEstadistica.getFecha())));
		if (parametroConsultaEstadistica.getDuracion() == null) {
			filtros.add(Filters.gte("duracionMinutos", 0L));
		} else {
			filtros.add(Filters.gte("duracionMinutos", parametroConsultaEstadistica.getDuracion()));
		}
		return filtros;
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

	@Override
	public void insert(Individuo individuo, List<MongoOrder> operaciones) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void update(Individuo individuo, MongoOrder operacion, Date fechaApertura) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
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
	public List<MongoOrder> consultarOperacionesActivas(Date fechaBase, Date fechaFin, int filas)
			throws GeneticDAOException {
		Bson filtros = Filters.and(Filters.lt("fechaApertura", fechaBase),
				Filters.gt("fechaApertura", DateUtil.adicionarDias(fechaBase, -30)),
				Filters.or(Filters.exists("fechaCierre", false), Filters.gt("fechaCierre", fechaBase)));
		Bson sorts = Sorts.orderBy(Sorts.ascending("fechaApertura"));
		MongoCursor<Document> cursor = this.collection.find(filtros).sort(sorts).limit(filas).iterator();
		return getMapper().helpList(cursor);
	}

	@Override
	public int deleteOperaciones(String idIndividuo) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void updateMaximosRetrocesoOperacion(Individuo individuo, MongoOrder operacion) throws GeneticDAOException {
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
	public List<Individuo> consultarIndividuoOperacionActiva(Date fechaBase, Date fechaFin, int filas)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

}

package forex.genetic.dao.mongodb;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.IndexOptions;

import forex.genetic.dao.IOperacionesDAO;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.Estadistica;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.ParametroOperacionPeriodo;
import forex.genetic.exception.GeneticDAOException;

/**
 *
 * @author ricardorq85
 */
public class MongoOperacionesDAO extends MongoGeneticDAO<Order> implements IOperacionesDAO<Order> {

	public MongoOperacionesDAO() {
		this(true);
	}

	public MongoOperacionesDAO(boolean configure) {
		super("operacion", configure);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		// this.collection.createIndex(Indexes.ascending("moneda", "periodo",
		// "fechaHistorico"), indexOptions);
		// this.collection.createIndex(Indexes.ascending("fechaHistorico"),
		// indexOptions);
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
	public void insert(Individuo individuo, List<? extends Order> operaciones) throws GeneticDAOException {
		List<Document> docs = getMapper().toMap(operaciones);
		for (Document document : docs) {
			document.append("idIndividuo", individuo.getId());
		}
		insertManyDocuments(docs);
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
	public Estadistica consultarEstadisticasIndividuo(Individuo individuo) throws GeneticDAOException {
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
	public int deleteOperaciones(String idIndividuo) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void updateOperacion(Individuo individuo, Order operacion, Date fechaApertura) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void updateMaximosReprocesoOperacion(Individuo individuo, Order operacion) throws GeneticDAOException {
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

}

package forex.genetic.dao.mongodb;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;

import forex.genetic.dao.IEstadisticaDAO;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Order;
import forex.genetic.entities.mongo.MongoEstadistica;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.DateUtil;

/**
 *
 * @author ricardorq85
 */
public class MongoEstadisticasIndividuoDAO extends MongoGeneticDAO<MongoEstadistica>
		implements IEstadisticaDAO<MongoEstadistica> {

	public MongoEstadisticasIndividuoDAO(MongoDatabase db) throws GeneticDAOException {
		this(db, true);
	}

	public MongoEstadisticasIndividuoDAO(MongoDatabase db, boolean configure) throws GeneticDAOException {
		super(db, "estadisticaIndividuo", configure);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		this.collection.createIndex(Indexes.ascending("idIndividuo", "fechaInicial"), indexOptions);
		this.collection.createIndex(Indexes.ascending("idIndividuo"));
		this.collection.createIndex(Indexes.ascending("fechaFinal"));
	}

	@Override
	public <I extends IndividuoEstrategia> MongoEstadistica getLast(I individuo) {
		Document doc = this.collection.find(Filters.eq("idIndividuo", individuo.getId()))
				.sort(Sorts.descending("fechaInicial")).limit(1).first();

		MongoEstadistica estadistica = null;
		if (doc != null) {
			estadistica = getMapper().helpOne(doc);
		}
		return estadistica;
	}

	@Override
	public <I extends IndividuoEstrategia, O extends Order> MongoEstadistica getLast(I individuo, Date fechaBase) {
		Document doc = this.collection
				.find(Filters.and(Filters.eq("idIndividuo", individuo.getId()), Filters.lt("fechaInicial", fechaBase)))
				.sort(Sorts.descending("fechaInicial")).limit(1).first();

		MongoEstadistica estadistica = null;
		if (doc != null) {
			estadistica = getMapper().helpOne(doc);
		}
		return estadistica;
	}

	@Override
	public void update(MongoEstadistica obj) {
		Document filterPk = new Document(getMapper().toPrimaryKeyMap(obj));
		this.collection.updateOne(filterPk, Updates.set("fechaFinal", obj.getFechaFinal()));
	}

	@Override
	public List<MongoEstadistica> consultarRandom(Date fechaInicial, Date fechaFinal, int cantidad) {
		Bson filtros = Filters.and(Filters.gte("fechaFinal", fechaInicial), Filters.lte("fechaFinal", fechaFinal));
		MongoCursor<Document> cursor = this.collection
				.aggregate(Arrays.asList(Aggregates.match(filtros), Aggregates.sample(cantidad))).iterator();
		return getMapper().helpList(cursor);
	}

	@Override
	public List<MongoEstadistica> consultarByDuracionPromedio(int duracionPromedioMinutosMinimos, int cantidad) {
		Date fechaMinima = null;
		try {
			fechaMinima = DateUtil.obtenerFecha("2014/01/01 00:00");
		} catch (ParseException e) {
			fechaMinima = new Date();
		}
		Bson filtros = Filters.and(Filters.lt("duracionPromedio", duracionPromedioMinutosMinimos),
				Filters.gt("fechaInicial", fechaMinima), Filters.exists("fechaFinal"));
		MongoCursor<Document> cursor = this.collection
				.aggregate(Arrays.asList(Aggregates.match(filtros), Aggregates.sample(cantidad))).iterator();
		return getMapper().helpList(cursor);
	}
}

package forex.genetic.dao.mongodb;

import org.bson.Document;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;

import forex.genetic.entities.Order;
import forex.genetic.entities.mongo.MongoEstadistica;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.exception.GeneticDAOException;

/**
 *
 * @author ricardorq85
 */
public class MongoEstadisticasIndividuoDAO extends MongoGeneticDAO<MongoEstadistica> {

	public MongoEstadisticasIndividuoDAO() throws GeneticDAOException {
		this(true);
	}

	public MongoEstadisticasIndividuoDAO(boolean configure) throws GeneticDAOException {
		super("estadisticaIndividuo", configure);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		this.collection.createIndex(Indexes.ascending("idIndividuo", "fechaInicial"), indexOptions);
		this.collection.createIndex(Indexes.ascending("idIndividuo"));
	}

	public MongoEstadistica getLast(MongoIndividuo individuo) {
		Document doc = this.collection.find(Filters.eq("idIndividuo", individuo.getId()))
				.sort(Sorts.descending("fechaInicial")).limit(1).first();

		MongoEstadistica estadistica = null;
		if (doc != null) {
			estadistica = getMapper().helpOne(doc);
		}
		return estadistica;
	}

	public MongoEstadistica getLast(MongoIndividuo individuo, Order order) {
		Document doc = this.collection
				.find(Filters.and(Filters.eq("idIndividuo", individuo.getId()),
						Filters.lt("fechaInicial", order.getCloseDate())))
				.sort(Sorts.descending("fechaInicial")).limit(1).first();

		MongoEstadistica estadistica = null;
		if (doc != null) {
			estadistica = getMapper().helpOne(doc);
		}
		return estadistica;
	}

	public void update(MongoEstadistica obj) {
		Document filterPk = new Document(getMapper().toPrimaryKeyMap(obj));
		this.collection.updateOne(filterPk, Updates.addToSet("fechaFinal", obj.getFechaFinal()));
	}
}
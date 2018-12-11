package forex.genetic.dao.mongodb;

import java.util.Date;

import org.bson.Document;

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

/**
 *
 * @author ricardorq85
 */
public class MongoEstadisticasIndividuoDAO extends MongoGeneticDAO<MongoEstadistica>
		implements IEstadisticaDAO<MongoEstadistica> {

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
				.find(Filters.and(Filters.eq("idIndividuo", individuo.getId()),
						Filters.lt("fechaInicial", fechaBase)))
				.sort(Sorts.descending("fechaInicial")).limit(1).first();

		MongoEstadistica estadistica = null;
		if (doc != null) {
			estadistica = getMapper().helpOne(doc);
		}
		return estadistica;
	}

	public void update(MongoEstadistica obj) {
		Document filterPk = new Document(getMapper().toPrimaryKeyMap(obj));
		this.collection.updateOne(filterPk, Updates.set("fechaFinal", obj.getFechaFinal()));
	}

}

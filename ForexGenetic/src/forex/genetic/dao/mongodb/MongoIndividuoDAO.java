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
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.mongo.MongoIndividuo;

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
	public List<? extends IndividuoEstrategia> getByProcesoEjecucion(String filtroAdicional, Date fechaHistorico) {
		Bson filtro = Filters.ne("procesoEjecucion.maxfechaHistorico", fechaHistorico);
		Bson ordenador = Sorts.orderBy(Sorts.ascending("procesoEjecucion.maxFechaHistorico"),
				Sorts.descending("idIndividuo"));
		MongoCursor<Document> cursor = collection.find(filtro).sort(ordenador).limit(10).iterator();
		return mapper.helpList(cursor);
	}
}

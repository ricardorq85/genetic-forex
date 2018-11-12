package forex.genetic.dao.mongodb;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.UpdateOptions;

import forex.genetic.dao.helper.mongodb.MongoIndividuoHelper;
import forex.genetic.entities.IndividuoEstrategia;

public class MongoIndividuoDAO extends MongoGeneticDAO<IndividuoEstrategia> {

	public MongoIndividuoDAO() {
		super("individuo", true);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		this.collection.createIndex(Indexes.ascending("idIndividuo"), indexOptions);
	}

	public IndividuoEstrategia consultarById(String idIndividuo) {
		IndividuoEstrategia obj = null;
		MongoCursor<Document> cursor = this.collection.find(Filters.eq("idIndividuo", idIndividuo)).iterator();
		if (cursor.hasNext()) {
			obj = MongoIndividuoHelper.helpOne(cursor.next());
		}
		return obj;
	}

	public void insertOrUpdate(IndividuoEstrategia obj) {
		// com.mongodb.client.model.Filters.
		Bson filterPk = MongoIndividuoHelper.toPrimaryKey(obj);
		Document doc = new Document("$set", MongoIndividuoHelper.toMap(obj));
		UpdateOptions options = new UpdateOptions();
		options.upsert(true);
		// com.mongodb.client.model.Filters
		this.collection.updateOne(filterPk, doc, options);
	}

	@Override
	public void insertMany(List<? extends IndividuoEstrategia> datos) {
		List<Document> docs = MongoIndividuoHelper.toMap(datos);
		InsertManyOptions options = new InsertManyOptions();
		options.bypassDocumentValidation(true);
		this.collection.insertMany(docs, options);
	}

}

package forex.genetic.dao.mongodb;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.UpdateOptions;

import forex.genetic.dao.helper.mongodb.MongoIndividuoHelper;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.IndividuoEstrategia;

public class MongoIndividuoDAO extends MongoGeneticDAO<Individuo> {

	public MongoIndividuoDAO () {
		super("individuo");
	}

	protected void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		this.collection.createIndex(Indexes.ascending("idIndividuo"), indexOptions);
	}

	public void insertOrUpdate(Individuo obj) {
		// com.mongodb.client.model.Filters.
		Bson filterPk = MongoIndividuoHelper.toPrimaryKey(obj);
		Document doc = new Document("$set", MongoIndividuoHelper.toMap(obj));
		UpdateOptions options = new UpdateOptions();
		options.upsert(true);
		// com.mongodb.client.model.Filters
		this.collection.updateOne(filterPk, doc, options);
	}

	public void insertMany(List<Individuo> datos) {
		List<Document> docs = MongoIndividuoHelper.toMap(datos);
		InsertManyOptions options = new InsertManyOptions();
		options.bypassDocumentValidation(true);
		this.collection.insertMany(docs, options);
	}
}

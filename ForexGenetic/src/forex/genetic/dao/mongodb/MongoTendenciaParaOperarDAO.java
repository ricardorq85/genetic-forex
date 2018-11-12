/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.mongodb;

import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.orderBy;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;

import forex.genetic.dao.helper.mongodb.MongoDatoAdicionalTPOHelper;
import forex.genetic.dao.helper.mongodb.MongoTendenciaParaOperarHelper;
import forex.genetic.entities.DatoAdicionalTPO;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.entities.TendenciaParaOperarMaxMin;
import forex.genetic.util.jdbc.mongodb.ConnectionMongoDB;

/**
 *
 * @author ricardorq85
 */
public class MongoTendenciaParaOperarDAO extends MongoGeneticDAO<TendenciaParaOperar> {

	private MongoCollection<Document> collection = null;
	private MongoCollection<Document> collectionDatoAdicional = null;

	// public MongoTendenciaParaOperarDAO(Connection connection) {
	public MongoTendenciaParaOperarDAO() {
		super("tendenciaParaOperar", true);
		this.collectionDatoAdicional = ConnectionMongoDB.getDatabase().getCollection("datoAdicionalTPO");
		this.configureCollection();
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		this.collection.createIndex(Indexes.ascending("periodo", "fechaBase", "tipoOperacion", "tipoExportacion"),
				indexOptions);
	}

	public Date getFechaBaseMinima() {
		Date fecha = null;
		Document doc = this.collection
				.aggregate(Arrays.asList(Aggregates.group(null, Accumulators.min("minDate", "$fechaBase")))).first();
		if (doc != null) {
			fecha = doc.getDate("minDate");
		}
		return fecha;
	}

	public void insertOrUpdate(TendenciaParaOperar tpo) {
		// System.out.println("TPOS: " + collection.countDocuments());

		// com.mongodb.client.model.Filters.
		Document filterPk = new Document(MongoTendenciaParaOperarHelper.toPrimaryKeyMap(tpo));
		Document doc = new Document("$set", MongoTendenciaParaOperarHelper.toMap(tpo));
		UpdateOptions options = new UpdateOptions();
		options.upsert(true);
		// com.mongodb.client.model.Filters
		this.collection.updateOne(filterPk, doc, options);
	}

	public List<TendenciaParaOperarMaxMin> consultarTendenciasParaOperar(Date fechaInicio) {
		List<TendenciaParaOperarMaxMin> list = null;
		Document filter = new Document();
		if (fechaInicio != null) {
			Map<String, Object> filterValue = new HashMap<String, Object>();
			filterValue.put("$gte", fechaInicio);
			filter.append("fechaBase", filterValue);
		}
		filter.append("activa", 1);
		MongoCursor<Document> cursor = this.collection.find(filter).sort(orderBy(ascending("fechaBase"))).iterator();
		list = MongoTendenciaParaOperarHelper.helpList(cursor);

		return list;
	}

	public long delete(TendenciaParaOperar tpo, Date fechaReferencia) {
		Document doc = new Document(MongoTendenciaParaOperarHelper.toMapForDelete(tpo, fechaReferencia));
		DeleteResult result = this.collection.deleteMany(doc);
		return result.getDeletedCount();
	}

	public void insertOrUpdateDatoAdicional(DatoAdicionalTPO datpo) {
		Document docPk = new Document(MongoDatoAdicionalTPOHelper.toPrimaryKeyMap(datpo));
		Document doc = new Document("$set", MongoDatoAdicionalTPOHelper.toMap(datpo));
		UpdateOptions options = new UpdateOptions();
		options.upsert(true);

		this.collectionDatoAdicional.updateOne(docPk, doc, options);
	}

	@Override
	public void insertMany(List<? extends TendenciaParaOperar> datos) {
	}

}

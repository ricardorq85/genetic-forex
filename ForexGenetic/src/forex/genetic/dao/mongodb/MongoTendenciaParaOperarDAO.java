/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.mongodb;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;

import forex.genetic.entities.DatoAdicionalTPO;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.entities.TendenciaParaOperarMaxMin;
import forex.genetic.entities.mongodb.MongoDatoAdicionalTPOHelper;
import forex.genetic.entities.mongodb.MongoTendenciaParaOperarHelper;
import forex.genetic.util.jdbc.mongodb.ConnectionMongoDB;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.orderBy;

/**
 *
 * @author ricardorq85
 */
public class MongoTendenciaParaOperarDAO extends MongoGeneticDAO {

	private MongoCollection<Document> collection = null;
	private MongoCollection<Document> collectionDatoAdicional = null;

	// public MongoTendenciaParaOperarDAO(Connection connection) {
	public MongoTendenciaParaOperarDAO() {
		this.collection = ConnectionMongoDB.getDatabase().getCollection("tendenciaParaOperar");
		this.collectionDatoAdicional = ConnectionMongoDB.getDatabase().getCollection("datoAdicionalTPO");
	}

	public void insertOrUpdateTendenciaParaOperar(TendenciaParaOperar tpo) {
		// System.out.println("TPOS: " + collection.countDocuments());

		//com.mongodb.client.model.Filters.
		Document filterPk = new Document(MongoTendenciaParaOperarHelper.toPrimaryKeyMap(tpo));
		Document doc = new Document("$set", MongoTendenciaParaOperarHelper.toMap(tpo));
		UpdateOptions options = new UpdateOptions();
		options.upsert(true);
		//com.mongodb.client.model.Filters
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

	public long deleteTendenciaParaProcesar(TendenciaParaOperar tpo, Date fechaReferencia) {
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

}

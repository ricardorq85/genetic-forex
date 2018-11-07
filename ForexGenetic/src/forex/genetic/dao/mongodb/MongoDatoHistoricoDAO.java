/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.mongodb;

import java.util.Arrays;
import java.util.Date;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.UpdateOptions;

import forex.genetic.entities.Point;
import forex.genetic.entities.mongodb.MongoDatoHistoricoHelper;
import forex.genetic.util.jdbc.mongodb.ConnectionMongoDB;

/**
 *
 * @author ricardorq85
 */
public class MongoDatoHistoricoDAO extends MongoGeneticDAO {

	private MongoCollection<Document> collection = null;

	public MongoDatoHistoricoDAO() {
		this.collection = ConnectionMongoDB.getDatabase().getCollection("datoHistorico");
		this.configureCollection();
	}

	private void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		this.collection.createIndex(Indexes.ascending("moneda", "periodo", "fechaHistorico"), indexOptions);
		this.collection.createIndex(Indexes.ascending("fechaHistorico"), indexOptions);
	}

	public Date getFechaHistoricaMinima() {
		Date fecha = null;
		Document doc = this.collection
				.aggregate(Arrays.asList(Aggregates.group(null, Accumulators.min("minDate", "$fechaHistorico"))))
				.first();
		if (doc != null) {
			fecha =  doc.getDate("minDate");
		}
		return fecha;
	}

	public Date getFechaHistoricaMaxima() {
		Document doc = this.collection
				.aggregate(Arrays.asList(Aggregates.group(null, Accumulators.max("maxDate", "$fechaHistorico"))))
				.first();

		return doc.getDate("maxDate");
	}

	public void insertOrUpdateDatoHistorico(Point datoHistorico) {
		// com.mongodb.client.model.Filters.
		Document filterPk = new Document(MongoDatoHistoricoHelper.toPrimaryKeyMap(datoHistorico));
		Document doc = new Document("$set", MongoDatoHistoricoHelper.toMap(datoHistorico));
		UpdateOptions options = new UpdateOptions();
		options.upsert(true);
		// com.mongodb.client.model.Filters
		this.collection.updateOne(filterPk, doc, options);
	}
	
	public void cleanDatosHistoricos() {
		this.collection.drop();
	}


	/*
	 * public List<TendenciaParaOperarMaxMin> consultarTendenciasParaOperar(Date
	 * fechaInicio) { List<TendenciaParaOperarMaxMin> list = null; Document filter =
	 * new Document(); if (fechaInicio != null) { Map<String, Object> filterValue =
	 * new HashMap<String, Object>(); filterValue.put("$gte", fechaInicio);
	 * filter.append("fechaBase", filterValue); } filter.append("activa", 1);
	 * MongoCursor<Document> cursor =
	 * this.collection.find(filter).sort(orderBy(ascending("fechaBase"))).iterator()
	 * ; list = MongoTendenciaParaOperarHelper.helpList(cursor);
	 * 
	 * return list; }
	 * 
	 * public long deleteTendenciaParaProcesar(TendenciaParaOperar tpo, Date
	 * fechaReferencia) { Document doc = new
	 * Document(MongoTendenciaParaOperarHelper.toMapForDelete(tpo,
	 * fechaReferencia)); DeleteResult result = this.collection.deleteMany(doc);
	 * return result.getDeletedCount(); }
	 * 
	 * public void insertOrUpdateDatoAdicional(DatoAdicionalTPO datpo) { Document
	 * docPk = new Document(MongoDatoAdicionalTPOHelper.toPrimaryKeyMap(datpo));
	 * Document doc = new Document("$set",
	 * MongoDatoAdicionalTPOHelper.toMap(datpo)); UpdateOptions options = new
	 * UpdateOptions(); options.upsert(true);
	 * 
	 * this.collectionDatoAdicional.updateOne(docPk, doc, options); }
	 */

}

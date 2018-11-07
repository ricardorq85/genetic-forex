/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.mongodb;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.UpdateOptions;

import forex.genetic.entities.DateInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.IntervalIndicator;
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
			fecha = doc.getDate("minDate");
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

	public void insertMany(List<Point> datos) {
		List<Document> docs = MongoDatoHistoricoHelper.toMap(datos);
		InsertManyOptions options = new InsertManyOptions();
		options.bypassDocumentValidation(true);
		this.collection.insertMany(docs, options);
	}

	public void cleanDatosHistoricos() {
		this.collection.drop();
	}

	public List<Date> consultarPuntosApertura(DateInterval rango, IndividuoEstrategia individuo) throws SQLException {
		List<Date> fechas = new ArrayList<Date>();

		Bson filtros = Filters.and(Filters.lte("fechaHistorico", rango.getLowInterval()),
				Filters.gte("fechaHistorico", rango.getHighInterval()));

		individuo.getOpenIndicators().stream().forEach(indicador -> {
			IntervalIndicator intervalIndicator = ((IntervalIndicator)indicador);
			StringBuilder nombreIndicador = new StringBuilder("indicadores").append(".").append(intervalIndicator.getName());
			//Filters.or("indicadores")
			//.getInterval()
		});
		
		this.collection.find(filtros);

		return fechas;
	}
}

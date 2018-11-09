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

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;

import forex.genetic.dao.helper.mongodb.MongoDatoHistoricoHelper;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.indicator.IndicadorManager;

/**
 *
 * @author ricardorq85
 */
public class MongoDatoHistoricoDAO extends MongoGeneticDAO<Point> {

	public MongoDatoHistoricoDAO() {
		super("datoHistorico");
	}

	protected void configureCollection() {
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

	public void insertOrUpdate(Point datoHistorico) {
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

	public List<Date> consultarPuntosApertura(DateInterval rango, IndividuoEstrategia individuo) throws SQLException {
		List<Date> fechas = null;

		List<Bson> filtros = new ArrayList<>();
		filtros.add(Filters.lte("fechaHistorico", rango.getLowInterval()));
		filtros.add(Filters.gte("fechaHistorico", rango.getHighInterval()));

		IndicadorController indicadorController = ControllerFactory
				.createIndicadorController(ControllerFactory.ControllerType.Individuo);
		for (int i = 0; i < individuo.getOpenIndicators().size(); i++) {
			IndicadorManager managerInstance = indicadorController.getManagerInstance(i);
			String nombreCalculado = managerInstance.getNombreCalculado();
			IntervalIndicator intervalIndicator = ((IntervalIndicator) individuo.getOpenIndicators().get(i));
			StringBuilder nombreIndicador = new StringBuilder("indicadores").append(".")
					.append(intervalIndicator.getName());

			if ((intervalIndicator != null) && (intervalIndicator.getInterval() != null)
					&& (intervalIndicator.getInterval().getLowInterval() != null)
					&& (intervalIndicator.getInterval().getHighInterval() != null)) {
				Bson filtroLow = Filters.gte(nombreIndicador.append(nombreCalculado).toString(),
						intervalIndicator.getInterval().getLowInterval());
				Bson filtroHigh = Filters.lte(nombreIndicador.append(nombreCalculado).toString(),
						intervalIndicator.getInterval().getHighInterval());
				filtros.add(filtroLow);
				filtros.add(filtroHigh);
			}
		}

		MongoCursor<Document> cursor = this.collection.find(Filters.and(filtros))
				.projection(Projections.fields(Projections.include("fechaHistorico"), Projections.excludeId()))
				.sort(Sorts.orderBy(Sorts.ascending("fechaHistorico"))).iterator();

		fechas = MongoDatoHistoricoHelper.helpFechas(cursor);
		return fechas;
	}
}

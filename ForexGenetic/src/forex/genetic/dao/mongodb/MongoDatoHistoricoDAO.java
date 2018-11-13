package forex.genetic.dao.mongodb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

import forex.genetic.dao.helper.mongodb.MongoDatoHistoricoMapper;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.indicator.IndicadorManager;
import forex.genetic.manager.indicator.IntervalIndicatorManager;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class MongoDatoHistoricoDAO extends MongoGeneticDAO<Point> {

	public MongoDatoHistoricoDAO() {
		super("datoHistorico", true);
	}

	public MongoDatoHistoricoDAO(boolean configure) {
		super("datoHistorico", configure);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		this.collection.createIndex(Indexes.ascending("moneda", "periodo", "fechaHistorico"), indexOptions);
		this.collection.createIndex(Indexes.ascending("fechaHistorico"), indexOptions);

		this.configureIndexIndicators();

	}

	private void configureIndexIndicators() {
		IndicadorController indicadorController = ControllerFactory
				.createIndicadorController(ControllerFactory.ControllerType.Individuo);
		for (int i = 0; i < indicadorController.getIndicatorNumber(); i++) {
			IntervalIndicatorManager<IntervalIndicator> indManager = (IntervalIndicatorManager) indicadorController
					.getManagerInstance(i);
			IntervalIndicator indicator = indManager.getIndicatorInstance();
			StringBuilder indexNamePrefix = new StringBuilder("indicadores.").append(indicator.getName());
			String[] nombresCalculados = indManager.getNombresCalculados();
			for (int j = 0; j < nombresCalculados.length; j++) {
				String indexName = new StringBuilder(indexNamePrefix).append(".").append(nombresCalculados[j]).toString();
				LogUtil.logTime("Index: " + indexName, 1);
				this.collection.createIndex(Indexes.ascending(indexName));
			}
		}
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

	public List<Date> consultarPuntosApertura(DateInterval rango, IndividuoEstrategia individuo) {
		List<Date> fechas = null;

		List<Bson> filtros = new ArrayList<>();
		filtros.add(Filters.gt("fechaHistorico", rango.getLowInterval()));
		filtros.add(Filters.lte("fechaHistorico", rango.getHighInterval()));

		IndicadorController indicadorController = ControllerFactory
				.createIndicadorController(ControllerFactory.ControllerType.Individuo);
		for (int i = 0; i < individuo.getOpenIndicators().size(); i++) {
			IndicadorManager<?> managerInstance = indicadorController.getManagerInstance(i);
			String[] nombreCalculado = managerInstance.getNombresCalculados();
			List<Bson> filtrosDatosCalculados = new ArrayList<>();
			for (int j = 0; j < nombreCalculado.length; j++) {
				IntervalIndicator intervalIndicator = ((IntervalIndicator) individuo.getOpenIndicators().get(i));
				if ((intervalIndicator != null) && (intervalIndicator.getInterval() != null)
						&& (intervalIndicator.getInterval().getLowInterval() != null)
						&& (intervalIndicator.getInterval().getHighInterval() != null)) {
					StringBuilder nombreIndicador = new StringBuilder("indicadores").append(".")
							.append(intervalIndicator.getName()).append(".");
					StringBuilder nombreIndicadorCalculado = new StringBuilder(nombreIndicador)
							.append(nombreCalculado[j]);

					Bson filtroLow = Filters.gte(nombreIndicadorCalculado.toString(),
							intervalIndicator.getInterval().getLowInterval());
					Bson filtroHigh = Filters.lte(nombreIndicadorCalculado.toString(),
							intervalIndicator.getInterval().getHighInterval());
					filtrosDatosCalculados.add(Filters.and(filtroLow, filtroHigh));
				}
			}
			if (filtrosDatosCalculados.size() > 1) {
				filtros.add(Filters.or(filtrosDatosCalculados));
			} else {
				filtros.addAll(filtrosDatosCalculados);
			}
		}

		Bson bsonFiltrosCompletos = Filters.and(filtros);
		LogUtil.logTime("idIndividuo=" + individuo.getId(), 1);
		LogUtil.logTime(
				bsonFiltrosCompletos.toBsonDocument(Document.class, MongoClient.getDefaultCodecRegistry()).toJson(), 1);

		MongoCursor<Document> cursor = this.collection.find(bsonFiltrosCompletos)
				.projection(Projections.fields(Projections.include("fechaHistorico"), Projections.excludeId()))
				.sort(Sorts.orderBy(Sorts.ascending("fechaHistorico"))).iterator();

		fechas = ((MongoDatoHistoricoMapper)mapper).helpFechas(cursor);
		return fechas;
	}
}

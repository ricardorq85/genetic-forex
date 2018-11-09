package forex.genetic.dao.helper.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;

import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Interval;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.factory.MonedaFactory;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.indicator.IntervalIndicatorManager;
import forex.genetic.util.Constants;

public class MongoIndividuoHelper {

	public static Bson toPrimaryKey(IndividuoEstrategia obj) {
		Bson filtros = Filters.eq("idIndividuo", obj.getId());
		return filtros;
	}

	public static Map<String, Object> toMap(IndividuoEstrategia obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("idIndividuo", obj.getId());
		objectMap.put("idParent1", obj.getIdParent1());
		objectMap.put("idParent2", obj.getIdParent2());
		objectMap.put("takeProfit", obj.getTakeProfit());
		objectMap.put("stopLoss", obj.getStopLoss());
		objectMap.put("lot", obj.getLot());
		objectMap.put("initialBalance", obj.getInitialBalance());
		if (obj.getCreationDate() == null) {
			objectMap.put("creationDate", null);
		} else {
			objectMap.put("creationDate", obj.getCreationDate());
		}
		objectMap.put("tipoOperacion", obj.getTipoOperacion().name());
		objectMap.put("tipoIndividuo", obj.getTipoIndividuo());
		objectMap.put("moneda", obj.getMoneda().getMoneda());

		objectMap.put("openIndicadores", getMapIndicadores(obj.getOpenIndicators()));
		objectMap.put("closeIndicadores", getMapIndicadores(obj.getCloseIndicators()));

		return objectMap;
	}

	private static List<Map<String, Object>> getMapIndicadores(List<? extends Indicator> list) {
		List<Map<String, Object>> indicadores = new ArrayList<Map<String, Object>>();
		((List<IntervalIndicator>) list).stream().forEach((ind) -> {
			if (ind != null) {
				indicadores.add(ind.toIntervalMap());
			} else {
				indicadores.add(null);
			}
		});
		return indicadores;
	}

	public static List<Document> toMap(List<? extends IndividuoEstrategia> datos) {
		List<Document> objectMaps = new ArrayList<Document>(datos.size());
		datos.stream().forEach(dato -> {
			objectMaps.add(new Document(toMap(dato)));
		});
		return objectMaps;
	}

	public static IndividuoEstrategia helpOne(Document one) {
		IndividuoEstrategia obj = new IndividuoEstrategia(one.getString("idIndividuo"));
		obj.setTakeProfit(one.getInteger("takeProfit"));
		obj.setStopLoss(one.getInteger("stopLoss"));
		obj.setLot(one.getDouble("lot"));
		obj.setInitialBalance(one.getInteger("initialBalance"));
		obj.setIdParent1(one.getString("idParent1"));
		obj.setIdParent2(one.getString("idParent2"));
		obj.setTipoOperacion("SELL".equalsIgnoreCase(one.getString("tipoOperacion")) ? Constants.OperationType.SELL
				: Constants.OperationType.BUY);
		obj.setTipoIndividuo(one.getString("tipoIndividuo"));
		obj.setMoneda(MonedaFactory.getMoneda(one.getString("moneda")));

		// obj.setOpenIndicators(getListIndicadores((List<Map<String, Object>>)
		// one.get("openIndicadores")));
		obj.setOpenIndicators(getListIndicadores((List<Map<String, Object>>) one.get("indicadores")));
		obj.setCloseIndicators(getListIndicadores((List<Map<String, Object>>) one.get("closeIndicadores")));

		return obj;
	}

	private static List<IntervalIndicator> getListIndicadores(List<Map<String, Object>> indicadoresMap) {
		IndicadorController indicadorController = ControllerFactory
				.createIndicadorController(ControllerFactory.ControllerType.Individuo);
		List<IntervalIndicator> indicadores = new ArrayList<IntervalIndicator>(
				indicadorController.getIndicatorNumber());
		if (indicadoresMap != null) {
			for (int i = 0; i < indicadorController.getIndicatorNumber(); i++) {
				IntervalIndicatorManager managerInstance = (IntervalIndicatorManager) indicadorController
						.getManagerInstance(i);
				if ((indicadoresMap != null) && (indicadoresMap.size() > i)) {
					Map<String, Object> mapIndicador = indicadoresMap.get(i);
					IntervalIndicator indicator = managerInstance.getIndicatorInstance();
					Interval<Double> interval = new DoubleInterval(managerInstance.getId());
					Map<String, Double> mapIntervalo = (Map<String, Double>) mapIndicador.get(indicator.getName());
					if (mapIntervalo != null) {
						interval.setLowInterval(mapIntervalo.get("low"));
						interval.setHighInterval(mapIntervalo.get("high"));
						indicator.setInterval(interval);
						indicadores.add(indicator);
					} else {
						indicadores.add(null);
					}
				} else {
					indicadores.add(null);
				}
			}
		}
		return indicadores;
	}
}

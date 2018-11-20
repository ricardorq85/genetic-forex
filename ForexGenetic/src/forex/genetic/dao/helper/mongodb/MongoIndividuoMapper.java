package forex.genetic.dao.helper.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Interval;
import forex.genetic.entities.dto.ProcesoEjecucionDTO;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.entities.mongo.MongoOrder;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.factory.MonedaFactory;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.indicator.IntervalIndicatorManager;
import forex.genetic.util.Constants;

public class MongoIndividuoMapper extends MongoMapper<MongoIndividuo> {

	@Override
	public Map<String, Object> toPrimaryKeyMap(MongoIndividuo obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("idIndividuo", obj.getId());
		return objectMap;
	}

	@Override
	public Map<String, Object> toMap(MongoIndividuo obj) {
		Map<String, Object> objectMap = toMapIndividuoEstrategia(obj);
		objectMap.put("procesoEjecucion", toMapProcesoEjecucion(obj));

		if (obj.getCurrentOrder() != null) {
			MongoOperacionMapper operacionMapper = new MongoOperacionMapper();
			objectMap.put("ordenActiva", operacionMapper.toMap((MongoOrder) obj.getCurrentOrder()));
		} else {
			objectMap.put("ordenActiva", null);
		}
		return objectMap;
	}

	public Map<String, Object> toMapProcesoEjecucion(MongoIndividuo obj) {
		Map<String, Object> procesoEjecucion = new HashMap<String, Object>();
		if (obj.getProcesoEjecucion() != null) {
			procesoEjecucion.put("maxFechaHistorico", obj.getProcesoEjecucion().getMaxFechaHistorico());
			if (obj.getProcesoEjecucion().getFechaProceso() != null) {
				procesoEjecucion.put("fechaProceso", obj.getProcesoEjecucion().getFechaProceso());
			} else {
				procesoEjecucion.put("fechaProceso", new Date());
			}
		}
		return procesoEjecucion;
	}

	private Map<String, Object> toMapIndividuoEstrategia(IndividuoEstrategia obj) {
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

	private List<Map<String, Object>> getMapIndicadores(List<? extends Indicator> list) {
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

	@Override
	public MongoIndividuo helpOne(Document one) {
		MongoIndividuo obj = new MongoIndividuo(one.getString("idIndividuo"));
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

		obj.setOpenIndicators(getListIndicadores((List<Map<String, Object>>) one.get("openIndicadores")));
		obj.setCloseIndicators(getListIndicadores((List<Map<String, Object>>) one.get("closeIndicadores")));

		if (one.get("procesoEjecucion") != null) {
			Map<String, Object> mapProcesoEjecucion = (Map<String, Object>) one.get("procesoEjecucion");
			ProcesoEjecucionDTO procesoEjecucion = new ProcesoEjecucionDTO();
			procesoEjecucion.setMaxFechaHistorico((Date) mapProcesoEjecucion.get("maxFechaHistorico"));
			procesoEjecucion.setFechaProceso((Date) mapProcesoEjecucion.get("fechaProceso"));
			obj.setProcesoEjecucion(procesoEjecucion);
		}
		if (one.get("ordenActiva") != null) {
			Map<String, Object> mapOrdenActiva = (Map<String, Object>) one.get("ordenActiva");
			Document documentOrdenActiva = new Document(mapOrdenActiva);
			MongoOperacionMapper operacionMapper = new MongoOperacionMapper();

			MongoOrder order = operacionMapper.helpOne(documentOrdenActiva);
			obj.setCurrentOrder(order);
		}
		return obj;
	}

	private List<IntervalIndicator> getListIndicadores(List<Map<String, Object>> indicadoresMap) {
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
					if (mapIndicador != null) {
						IntervalIndicator indicator = (IntervalIndicator) managerInstance.getIndicatorInstance();
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
				} else {
					indicadores.add(null);
				}
			}
		}
		return indicadores;
	}

	@Override
	public Map<String, Object> toMapForDelete(MongoIndividuo obj, Date fechaReferencia) {
		throw new UnsupportedOperationException();
	}

	public List<MongoIndividuo> toMongoIndividuo(List<? extends IndividuoEstrategia> list) {
		List<MongoIndividuo> mongoList = new ArrayList<MongoIndividuo>(list.size());
		for (IndividuoEstrategia individuoEstrategia : list) {
			MongoIndividuo mongoIndividuo = helpOne(new Document(toMapIndividuoEstrategia(individuoEstrategia)));
			mongoList.add(mongoIndividuo);
		}
		return mongoList;
	}
}

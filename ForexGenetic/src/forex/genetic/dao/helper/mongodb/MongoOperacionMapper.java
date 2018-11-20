package forex.genetic.dao.helper.mongodb;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import forex.genetic.entities.mongo.MongoOrder;
import forex.genetic.util.Constants;

public class MongoOperacionMapper extends MongoMapper<MongoOrder> {

	@Override
	public Map<String, Object> toPrimaryKeyMap(MongoOrder obj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap.put("idIndividuo", obj.getIdIndividuo());
		objMap.put("fechaApertura", obj.getOpenDate());
		return objMap;
	}

	@Override
	public Map<String, Object> toMap(MongoOrder obj) {
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap.put("idIndividuo", obj.getIdIndividuo());
		objMap.put("takeProfit", obj.getTakeProfit());
		objMap.put("stopLoss", obj.getStopLoss());
		objMap.put("fechaApertura", obj.getOpenDate());
		objMap.put("fechaCierre", obj.getCloseDate());
		objMap.put("spread", obj.getOpenSpread());
		objMap.put("openPrice", obj.getOpenOperationValue());
		objMap.put("pips", obj.getPips());
		objMap.put("lote", obj.getLot());
		objMap.put("fechaRegistro", new Date());
		objMap.put("closePriceByTakeProfit", obj.getClosePriceByTakeProfit());
		objMap.put("closePriceByStopLoss", obj.getClosePriceByStopLoss());
		if (obj.getTipoCierre() != null) {
			objMap.put("tipoCierre", obj.getTipoCierre().name());
		}
		return objMap;
	}

	@Override
	public Map<String, Object> toMapForDelete(MongoOrder obj, Date fechaReferencia) {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public MongoOrder helpOne(Document one) {
		MongoOrder order = new MongoOrder();
		order.setIdIndividuo(one.getString("idIndividuo"));
		order.setTakeProfit(one.getDouble("takeProfit"));
		order.setStopLoss(one.getDouble("stopLoss"));
		order.setOpenDate(one.getDate("fechaApertura"));
		order.setCloseDate(one.getDate("fechaCierre"));
		order.setOpenSpread(one.getDouble("spread"));
		order.setOpenOperationValue(one.getDouble("openPrice"));
		order.setPips(one.getDouble("pips"));
		order.setLot(one.getDouble("lote"));
		order.setFechaRegistro(one.getDate("fechaRegistro"));
		order.setClosePriceByTakeProfit(one.getDouble("closePriceByTakeProfit"));
		order.setClosePriceByStopLoss(one.getDouble("closePriceByStopLoss"));
		order.setTipoCierre(Constants.getCloseType(one.getString("tipoCierre")));
		return order;
	}
}

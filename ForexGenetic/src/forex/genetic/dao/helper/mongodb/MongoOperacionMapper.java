package forex.genetic.dao.helper.mongodb;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import forex.genetic.entities.Order;
import forex.genetic.entities.mongo.MongoOrder;

public class MongoOperacionMapper extends MongoMapper<MongoOrder> {

	@Override
	public Map<String, Object> toPrimaryKeyMap(MongoOrder obj) {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public Map<String, Object> toMap(MongoOrder obj) {
		Map<String, Object> objMap = new HashMap<String, Object>();

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
		objMap.put("tipoCierre", obj.getTipoCierre().name());
		
		return objMap;
	}

	@Override
	public Map<String, Object> toMapForDelete(MongoOrder obj, Date fechaReferencia) {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public MongoOrder helpOne(Document one) {
		throw new UnsupportedOperationException("Operacion no soportada");
	}
}

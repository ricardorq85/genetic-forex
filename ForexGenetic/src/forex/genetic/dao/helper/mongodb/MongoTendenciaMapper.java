package forex.genetic.dao.helper.mongodb;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import forex.genetic.entities.Regresion;
import forex.genetic.entities.Tendencia;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.entities.TendenciaParaOperarMaxMin;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;

public class MongoTendenciaMapper extends MongoMapper<Tendencia> {

	@Override
	public Map<String, Object> toPrimaryKeyMap(Tendencia obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();

		objectMap.put("idIndividuo", obj.getIndividuo().getId());
		objectMap.put("fechaBase", obj.getFechaBase());
		return objectMap;
	}

	@Override
	public Map<String, Object> toMap(Tendencia obj) {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public Map<String, Object> toMapForDelete(Tendencia obj, Date fechaReferencia) {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public Tendencia helpOne(Document one) {
		throw new UnsupportedOperationException("Operacion no soportada");
	}
}

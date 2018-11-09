package forex.genetic.dao.helper.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.indicator.IntervalIndicator;

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

		List<Map<String, Object>> indicadores = new ArrayList<Map<String, Object>>();

		List<IntervalIndicator> indicadoresBase = ((List<IntervalIndicator>) obj.getOpenIndicators());
		indicadoresBase.stream().forEach((ind) -> {
			if (ind != null) {
				indicadores.add(ind.toIntervalMap());
			} else {
				indicadores.add(null);
			}
		});

		objectMap.put("indicadores", indicadores);

		return objectMap;
	}

	public static List<Document> toMap(List<? extends IndividuoEstrategia> datos) {
		List<Document> objectMaps = new ArrayList<Document>(datos.size());
		datos.stream().forEach(dato -> {
			objectMaps.add(new Document(toMap(dato)));
		});
		return objectMaps;
	}

}

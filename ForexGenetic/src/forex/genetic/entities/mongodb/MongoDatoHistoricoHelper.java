package forex.genetic.entities.mongodb;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;

import forex.genetic.entities.DateInterval;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.IntervalIndicator;

public class MongoDatoHistoricoHelper {

	public static Map<String, Object> toPrimaryKeyMap(Point obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();

		objectMap.put("moneda", obj.getMoneda());
		objectMap.put("periodo", obj.getPeriodo());
		objectMap.put("fechaHistorico", obj.getDate());
		return objectMap;
	}

	public static Map<String, Object> toMap(Point datoHistorico) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("moneda", datoHistorico.getMoneda());
		objectMap.put("periodo", datoHistorico.getPeriodo());
		objectMap.put("monedaComparacion", datoHistorico.getMonedaComparacion());
		objectMap.put("fechaHistorico", datoHistorico.getDate());
		objectMap.put("open", datoHistorico.getOpen());
		objectMap.put("low", datoHistorico.getLow());
		objectMap.put("high", datoHistorico.getHigh());
		objectMap.put("close", datoHistorico.getClose());
		objectMap.put("volume", datoHistorico.getVolume());
		objectMap.put("spread", datoHistorico.getSpread());
		objectMap.put("closeCompare", datoHistorico.getCloseCompare());
		objectMap.put("fechaRegistro", new Date());

		List<Map<String, Object>> indicadores = new ArrayList<Map<String, Object>>();

		List<IntervalIndicator> indicadoresBase = ((List<IntervalIndicator>) datoHistorico.getIndicators());
		indicadoresBase.stream().forEach((ind) -> {
			indicadores.add(ind.toMap(datoHistorico));
		});

		objectMap.put("indicadores", indicadores);

		return objectMap;
	}

	public static List<Document> toMap(List<Point> datosHistoricos) {
		List<Document> objectMaps = new ArrayList<Document>(datosHistoricos.size());
		datosHistoricos.stream().forEach(dato -> {
			objectMaps.add(new Document(toMap(dato)));
		});
		return objectMaps;
	}

}

package forex.genetic.dao.helper.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCursor;

import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.indicator.IndicadorManager;
import forex.genetic.util.DateUtil;

public class MongoDatoHistoricoMapper extends MongoMapper<Point> {
	
	@Override
	public Map<String, Object> toPrimaryKeyMap(Point obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();

		objectMap.put("moneda", obj.getMoneda());
		objectMap.put("periodo", obj.getPeriodo());
		objectMap.put("fechaHistorico", obj.getDate());
		return objectMap;
	}

	@Override
	public Map<String, Object> toMap(Point datoHistorico) {
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
		IndicadorController indicadorController = ControllerFactory
				.createIndicadorController(ControllerFactory.ControllerType.Individuo);
		for (int i = 0; i < indicadorController.getIndicatorNumber(); i++) {
			IndicadorManager<Indicator> indicadorManager = indicadorController.getManagerInstance(i);
			Point prevPoint = datoHistorico.getPrevPoint();
			if (indicadoresBase.get(i) != null) {
				Indicator prevIndicator = (prevPoint != null) ? prevPoint.getIndicators().get(i) : null;
				Map<String, Object> values = indicadorManager.getCalculatedValues(prevIndicator, indicadoresBase.get(i),
						datoHistorico.getPrevPoint(), datoHistorico);
				Map<String, Object> indMap = new HashMap<String, Object>();
				indMap.put(indicadoresBase.get(i).getName(), values);
				indicadores.add(indMap);
			} else {
				indicadores.add(null);
			}
		}
//		indicadoresBase.stream().forEach((indicador) -> {
		// indicadores.add(indicador.toMap(datoHistorico));
		// });

		objectMap.put("indicadores", indicadores);

		return objectMap;
	}

	public List<Date> helpFechas(MongoCursor<Document> cursor) {
		List<Date> list = new ArrayList<Date>();
		try {
			while (cursor.hasNext()) {
				Date fecha = cursor.next().getDate("fechaHistorico");
				list.add(DateUtil.adicionarMinutos(fecha, -1));
			}
		} finally {
			cursor.close();
		}
		return list;
	}

	@Override
	public Map<String, Object> toMapForDelete(Point obj, Date fechaReferencia) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Point helpOne(Document one) {
		// TODO Auto-generated method stub
		return null;
	}

}

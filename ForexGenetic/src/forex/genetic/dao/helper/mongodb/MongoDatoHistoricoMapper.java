package forex.genetic.dao.helper.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCursor;

import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.RangoOperacionIndividuo;
import forex.genetic.entities.RangoOperacionIndividuoIndicador;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.IntervalIndicator;
import forex.genetic.factory.ControllerFactory;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.indicator.IndicadorManager;
import forex.genetic.manager.indicator.IntervalIndicatorManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.RandomUtil;

public class MongoDatoHistoricoMapper extends MongoMapper<Point> {

	private static final IndicadorController indicadorController = ControllerFactory
			.createIndicadorController(ControllerFactory.ControllerType.Individuo);

	@Override
	public Map<String, Object> toMapForDelete(Point obj, Date fechaReferencia) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("fechaHistorico", obj.getDate());
		return objectMap;
	}

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

		objectMap.put("indicadores", toMapIndicadores(datoHistorico));
		return objectMap;
	}

	private List<Map<String, Object>> toMapIndicadores(Point datoHistorico) {
		List<Map<String, Object>> indicadores = new ArrayList<Map<String, Object>>();
		List<IntervalIndicator> indicadoresBase = ((List<IntervalIndicator>) datoHistorico.getIndicators());
		IndicadorController indicadorController = ControllerFactory
				.createIndicadorController(ControllerFactory.ControllerType.Individuo);
		for (int i = 0; i < indicadorController.getIndicatorNumber(); i++) {
			IndicadorManager<Indicator> indicadorManager = indicadorController.getManagerInstance(i);
			Point prevPoint = datoHistorico.getPrevPoint();
			if (indicadoresBase.get(i) != null) {
				Indicator prevIndicator = (prevPoint != null) ? prevPoint.getIndicators().get(i) : null;
				Map<String, Double> values = indicadorManager.getCalculatedValues(prevIndicator, indicadoresBase.get(i),
						datoHistorico.getPrevPoint(), datoHistorico);
				Map<String, Object> indMap = new HashMap<String, Object>();
				indMap.put(indicadoresBase.get(i).getName(), values);
				indicadores.add(indMap);
			} else {
				indicadores.add(null);
			}
		}
		return indicadores;
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
	public Point helpOne(Document one) {
		Point obj = null;
		if ((one != null) && (!one.isEmpty())) {
			obj = new Point();
			obj.setMoneda(one.getString("moneda"));
			obj.setPeriodo(one.getInteger("periodo"));
			obj.setMonedaComparacion(one.getString("monedaComparacion"));
			obj.setDate(one.getDate("fechaHistorico"));
			obj.setLow(one.getDouble("low"));
			obj.setOpen(one.getDouble("open"));
			obj.setHigh(one.getDouble("high"));
			obj.setClose(one.getDouble("close"));
			obj.setVolume(one.getInteger("volume"));
			obj.setSpread(one.getDouble("spread"));
			obj.setCloseCompare(one.getDouble("closeCompare"));

			IndicadorController indicadorController = ControllerFactory
					.createIndicadorController(ControllerFactory.ControllerType.Individuo);

			List<Map<String, Object>> indicadoresMap = (List<Map<String, Object>>) one.get("indicadores");
			List<IntervalIndicator> indicadores = new ArrayList<IntervalIndicator>(
					indicadorController.getIndicatorNumber());
			for (int i = 0; i < indicadorController.getIndicatorNumber(); i++) {
				IndicadorManager<Indicator> indicadorManager = indicadorController.getManagerInstance(i);
				IntervalIndicator indicador = (IntervalIndicator) indicadorManager
						.getIndicatorInstance(indicadoresMap.get(i));
				indicadores.add(indicador);
			}
			obj.setIndicators(indicadores);
		}

		return obj;
	}

	public void helpRangoOperacionIndividuo(MongoCursor<Document> cursor,
			RangoOperacionIndividuo rangoOperacionIndividuo) {
		if (cursor.hasNext()) {
			Document doc = cursor.next();
			int cantidad = doc.getInteger("registros");
			if (cantidad == 0) {
				rangoOperacionIndividuo.setIndicadores(null);
				return;
			}

			int num_indicadores = indicadorController.getIndicatorNumber();
			for (int i = 0; i < num_indicadores; i++) {
				IntervalIndicatorManager<?> indManager = (IntervalIndicatorManager<?>) indicadorController
						.getManagerInstance(i);
				RangoOperacionIndividuoIndicador rangoIndicador = rangoOperacionIndividuo.getIndicadores().get(i);
				IntervalIndicator indicator = ((IntervalIndicator) rangoIndicador.getIndicator());

				String[] nombreCalculado = indManager.getNombresCalculados();
				double inferior = Double.MAX_VALUE;
				double superior = Double.MIN_VALUE;
				double maxSum = 0.0D;
				for (int j = 0; j < nombreCalculado.length; j++) {
					StringBuilder nombreIndicador = new StringBuilder("indicadores").append(".")
							.append(indicator.getName()).append(".");
					StringBuilder nombreIndicadorCalculado = new StringBuilder(nombreIndicador)
							.append(nombreCalculado[j]);
					String strNombre = nombreIndicadorCalculado.toString().replaceAll("\\.", "");

					Double min = doc.getDouble("min" + strNombre);
					Double max = doc.getDouble("max" + strNombre);
					Double sum = doc.getDouble("sum" + strNombre);
					if (min != null) {
						inferior = Math.min(inferior, min);
					}
					if (max != null) {
						superior = Math.max(superior, max);
					}
					if (sum != null) {
						maxSum = Math.max(maxSum, sum);
					}
				}

				Interval<Double> interval = new DoubleInterval(inferior, superior);
				Interval<Double> intervalWide = new DoubleInterval(inferior * (1 + RandomUtil.nextDouble()),
						superior * (1 + RandomUtil.nextDouble()));
				indicator.setInterval(interval);
				rangoIndicador.setCantidad(cantidad);
				rangoIndicador.setSuma(maxSum);
				rangoIndicador.setPromedio(maxSum / cantidad);
			}

			double minLow = doc.getDouble("minLow");
			double maxHigh = doc.getDouble("maxHigh");
			int diff = Math.min(3000, new Double((maxHigh - minLow) * 100000.0D).intValue());
			diff = Math.max(diff, 200);
			rangoOperacionIndividuo.setTakeProfit(diff);
			rangoOperacionIndividuo.setStopLoss(diff);
			rangoOperacionIndividuo.setCantidad(cantidad);
		}
	}
}

package forex.genetic.dao.helper.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import forex.genetic.entities.mongo.MongoEstadistica;

public class MongoEstadisticaIndividuoMapper extends MongoMapper<MongoEstadistica> {

	@Override
	public Map<String, Object> toPrimaryKeyMap(MongoEstadistica obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("idIndividuo", obj.getIdIndividuo());
		objectMap.put("fechaInicial", obj.getFechaInicial());
		return objectMap;
	}

	@Override
	public Map<String, Object> toMap(MongoEstadistica obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("idIndividuo", obj.getIdIndividuo());
		objectMap.put("fechaInicial", obj.getFechaInicial());
		objectMap.put("fechaFinal", obj.getFechaFinal());

		objectMap.put("cantidadPositivos", obj.getCantidadPositivos());
		objectMap.put("cantidadNegativos", obj.getCantidadNegativos());
		objectMap.put("cantidad", obj.getCantidadTotal());
		objectMap.put("pipsPositivos", obj.getPipsPositivos());
		objectMap.put("pipsNegativos", obj.getPipsNegativos());
		objectMap.put("pips", obj.getPips());
		objectMap.put("pipsMinimosPositivos", obj.getPipsMinimosPositivos());
		objectMap.put("pipsMinimosNegativos", obj.getPipsMinimosNegativos());
		objectMap.put("pipsMinimos", obj.getPipsMinimos());
		objectMap.put("pipsMaximosPositivos", obj.getPipsMaximosPositivos());
		objectMap.put("pipsMaximosNegativos", obj.getPipsMaximosNegativos());
		objectMap.put("pipsMaximos", obj.getPipsMaximos());
		objectMap.put("pipsPromedioPositivos", obj.getPipsPromedioPositivos());
		objectMap.put("pipsPromedioNegativos", obj.getPipsPromedioNegativos());
		objectMap.put("pipsPromedio", obj.getPipsPromedio());
		objectMap.put("pipsModaPositivos", obj.getPipsModaPositivos());
		objectMap.put("pipsModaNegativos", obj.getPipsModaNegativos());
		objectMap.put("pipsModa", obj.getPipsModa());
		objectMap.put("pipsMaximosRetrocesoPositivos", obj.getPipsMaximosRetrocesoPositivos());
		objectMap.put("pipsMaximosRetrocesoNegativos", obj.getPipsMaximosRetrocesoNegativos());
		objectMap.put("pipsMaximosRetroceso", obj.getPipsMaximosRetroceso());
		objectMap.put("pipsMinimosRetrocesoPositivos", obj.getPipsMinimosRetrocesoPositivos());
		objectMap.put("pipsMinimosRetrocesoNegativos", obj.getPipsMinimosRetrocesoNegativos());
		objectMap.put("pipsMinimosRetroceso", obj.getPipsMinimosRetroceso());
		objectMap.put("pipsPromedioRetrocesoPositivos", obj.getPipsPromedioRetrocesoPositivos());
		objectMap.put("pipsPromedioRetrocesoNegativos", obj.getPipsPromedioRetrocesoNegativos());
		objectMap.put("pipsPromedioRetroceso", obj.getPipsPromedioRetroceso());
		objectMap.put("pipsModaRetrocesoPositivos", obj.getPipsModaRetrocesoPositivos());
		objectMap.put("pipsModaRetrocesoNegativos", obj.getPipsModaRetrocesoNegativos());
		objectMap.put("pipsModaRetroceso", obj.getPipsModaRetroceso());
		objectMap.put("duracionMinimaPositivos", obj.getDuracionMinimaPositivos());
		objectMap.put("duracionMinimaNegativos", obj.getDuracionMinimaNegativos());
		objectMap.put("duracionMinima", obj.getDuracionMinima());
		objectMap.put("duracionMaximaPositivos", obj.getDuracionMaximaPositivos());
		objectMap.put("duracionMaximaNegativos", obj.getDuracionMaximaNegativos());
		objectMap.put("duracionMaxima", obj.getDuracionMaxima());
		objectMap.put("duracionPromedioPositivos", obj.getDuracionPromedioPositivos());
		objectMap.put("duracionPromedioNegativos", obj.getDuracionPromedioNegativos());
		objectMap.put("duracionPromedio", obj.getDuracionPromedio());
		objectMap.put("duracionModaPositivos", obj.getDuracionModaPositivos());
		objectMap.put("duracionModaNegativos", obj.getDuracionModaNegativos());
		objectMap.put("duracionModa", obj.getDuracionModa());
		objectMap.put("duracionDesvEstandarPositivos", obj.getDuracionDesvEstandarPositivos());
		objectMap.put("duracionDesvEstandarNegativos", obj.getDuracionDesvEstandarNegativos());
		objectMap.put("duracionDesvEstandar", obj.getDuracionDesvEstandar());

		objectMap.put("duracionTotal", obj.getDuracionTotal());
		objectMap.put("duracionTotalPositivos", obj.getDuracionTotalPositivos());
		objectMap.put("duracionTotalNegativos", obj.getDuracionTotalNegativos());

		objectMap.put("dataDuracion", obj.getDataDuracion());
		objectMap.put("dataDuracionPositivos", obj.getDataDuracionPositivos());
		objectMap.put("dataDuracionNegativos", obj.getDataDuracionNegativos());

		objectMap.put("dataPips", obj.getDataPips());
		objectMap.put("dataPipsPositivos", obj.getDataPipsPositivos());
		objectMap.put("dataPipsNegativos", obj.getDataPipsNegativos());

		objectMap.put("dataPipsRetroceso", obj.getDataPipsRetroceso());
		objectMap.put("dataPipsRetrocesoPositivos", obj.getDataPipsRetrocesoPositivos());
		objectMap.put("dataPipsRetrocesoNegativos", obj.getDataPipsRetrocesoNegativos());

		return objectMap;
	}

	@Override
	public MongoEstadistica helpOne(Document one) {
		MongoEstadistica obj = new MongoEstadistica();
		helpOne(one, obj);
		return obj;
	}

	@SuppressWarnings("unchecked")
	public void helpOne(Document one, MongoEstadistica obj) {
		if (one.containsKey("idIndividuo")) {
			obj.setIdIndividuo(one.getString("idIndividuo"));
		}
		if (one.containsKey("fechaInicial")) {
			obj.setFechaInicial(one.getDate("fechaInicial"));
		}
		if (one.containsKey("fechaFinal")) {
			obj.setFechaFinal(one.getDate("fechaFinal"));
		}

		obj.setCantidadPositivos((Integer) one.getOrDefault("cantidadPositivos", obj.getCantidadPositivos()));
		obj.setCantidadNegativos((Integer) one.getOrDefault("cantidadNegativos", obj.getCantidadNegativos()));
		obj.setCantidadTotal((Integer) one.getOrDefault("cantidad", obj.getCantidadTotal()));

		obj.setPipsPositivos((Double) one.getOrDefault("pipsPositivos", obj.getPipsPositivos()));
		obj.setPipsNegativos((Double) one.getOrDefault("pipsNegativos", obj.getPipsNegativos()));
		obj.setPips((Double) one.getOrDefault("pips", obj.getPips()));
		obj.setPipsMinimosPositivos((Double) one.getOrDefault("pipsMinimosPositivos", obj.getPipsMinimosPositivos()));
		obj.setPipsMinimosNegativos((Double) one.getOrDefault("pipsMinimosNegativos", obj.getPipsMinimosNegativos()));
		obj.setPipsMinimos((Double) one.getOrDefault("pipsMinimos", obj.getPipsMinimos()));
		obj.setPipsMaximosPositivos((Double) one.getOrDefault("pipsMaximosPositivos", obj.getPipsMaximosPositivos()));
		obj.setPipsMaximosNegativos((Double) one.getOrDefault("pipsMaximosNegativos", obj.getPipsMaximosNegativos()));
		obj.setPipsMaximos((Double) one.getOrDefault("pipsMaximos", obj.getPipsMaximos()));
		obj.setPipsPromedioPositivos((Double) one.getOrDefault("pipsPromedioPositivos", obj.getPipsPromedioPositivos()));
		obj.setPipsPromedioNegativos((Double) one.getOrDefault("pipsPromedioNegativos", obj.getPipsPromedioNegativos()));
		obj.setPipsPromedio((Double) one.getOrDefault("pipsPromedio", obj.getPipsPromedio()));

		obj.setPipsModaPositivos((Double) one.getOrDefault("pipsModaPositivos", obj.getPipsModaPositivos()));
		obj.setPipsModaNegativos((Double) one.getOrDefault("pipsModaNegativos", obj.getPipsModaNegativos()));
		obj.setPipsModa((Double) one.getOrDefault("pipsModa", obj.getPipsModa()));
		obj.setPipsMaximosRetrocesoPositivos((Double) one.getOrDefault("pipsMaximosRetrocesoPositivos", obj.getPipsMaximosRetrocesoPositivos()));
		obj.setPipsMaximosRetrocesoNegativos((Double) one.getOrDefault("pipsMaximosRetrocesoNegativos", obj.getPipsMaximosRetrocesoNegativos()));
		obj.setPipsMaximosRetroceso((Double) one.getOrDefault("pipsMaximosRetroceso", obj.getPipsMaximosRetroceso()));
		obj.setPipsMinimosRetrocesoPositivos((Double) one.getOrDefault("pipsMinimosRetrocesoPositivos", obj.getPipsMinimosRetrocesoPositivos()));
		obj.setPipsMinimosRetrocesoNegativos((Double) one.getOrDefault("pipsMinimosRetrocesoNegativos", obj.getPipsMinimosRetrocesoNegativos()));
		obj.setPipsMinimosRetroceso((Double) one.getOrDefault("pipsMinimosRetroceso", obj.getPipsMinimosRetroceso()));
		obj.setPipsPromedioRetrocesoPositivos((Double) one.getOrDefault("pipsPromedioRetrocesoPositivos", obj.getPipsPromedioRetrocesoPositivos()));
		obj.setPipsPromedioRetrocesoNegativos((Double) one.getOrDefault("pipsPromedioRetrocesoNegativos", obj.getPipsPromedioRetrocesoNegativos()));
		obj.setPipsPromedioRetroceso((Double) one.getOrDefault("pipsPromedioRetroceso", obj.getPipsPromedioRetroceso()));
		obj.setPipsModaRetrocesoPositivos((Double) one.getOrDefault("pipsModaRetrocesoPositivos", obj.getPipsModaRetrocesoPositivos()));
		obj.setPipsModaRetrocesoNegativos((Double) one.getOrDefault("pipsModaRetrocesoNegativos", obj.getPipsMaximosRetrocesoNegativos()));
		obj.setPipsModaRetroceso((Double) one.getOrDefault("pipsModaRetroceso", obj.getPipsModaRetroceso()));
		obj.setDuracionMinimaPositivos(new Double(String.valueOf(one.getOrDefault("duracionMinimaPositivos", obj.getDuracionMinimaPositivos()))));
		obj.setDuracionMinimaNegativos(new Double(String.valueOf(one.getOrDefault("duracionMinimaNegativos", obj.getDuracionMinimaNegativos()))));
		obj.setDuracionMinima(new Double(String.valueOf(one.getOrDefault("duracionMinima", obj.getDuracionMinima()))));
		obj.setDuracionMaximaPositivos(new Double(String.valueOf(one.getOrDefault("duracionMaximaPositivos", obj.getDuracionMaximaPositivos()))));
		obj.setDuracionMaximaNegativos(new Double(String.valueOf(one.getOrDefault("duracionMaximaNegativos", obj.getDuracionMaximaNegativos()))));
		obj.setDuracionMaxima(new Double(String.valueOf(one.getOrDefault("duracionMaxima", obj.getDuracionMaxima()))));
		obj.setDuracionPromedioPositivos((Double) one.getOrDefault("duracionPromedioPositivos", obj.getDuracionPromedioPositivos()));
		obj.setDuracionPromedioNegativos((Double) one.getOrDefault("duracionPromedioNegativos", obj.getDuracionPromedioNegativos()));
		obj.setDuracionPromedio((Double) one.getOrDefault("duracionPromedio", obj.getDuracionPromedio()));
		obj.setDuracionModaPositivos((Double) one.getOrDefault("duracionModaPositivos", obj.getDuracionModaPositivos()));
		obj.setDuracionModaNegativos((Double) one.getOrDefault("duracionModaNegativos", obj.getDuracionModaNegativos()));
		obj.setDuracionModa((Double) one.getOrDefault("duracionModa", obj.getDuracionModa()));
		obj.setDuracionDesvEstandarPositivos((Double) one.getOrDefault("duracionDesvEstandarPositivos", obj.getDuracionDesvEstandarPositivos()));
		obj.setDuracionDesvEstandarNegativos((Double) one.getOrDefault("duracionDesvEstandarNegativos",obj.getDuracionDesvEstandarNegativos()));
		obj.setDuracionDesvEstandar((Double) one.getOrDefault("duracionDesvEstandar", obj.getDuracionDesvEstandar()));

		obj.setDuracionTotal(new Double(String.valueOf(one.getOrDefault("duracionTotal", obj.getDuracionTotal()))));
		obj.setDuracionTotalPositivos(new Double(String.valueOf(one.getOrDefault("duracionTotalPositivos", obj.getDuracionTotalPositivos()))));
		obj.setDuracionTotalNegativos(new Double(String.valueOf(one.getOrDefault("duracionTotalNegativos", obj.getDuracionTotalNegativos()))));

		obj.setDataDuracion(one.get("dataDuracion", ArrayList.class));
		obj.setDataDuracionPositivos(one.get("dataDuracionPositivos", ArrayList.class));
		obj.setDataDuracionNegativos(one.get("dataDuracionNegativos", ArrayList.class));

		obj.setDataPips(one.get("dataPips", ArrayList.class));
		obj.setDataPipsPositivos(one.get("dataPipsPositivos", ArrayList.class));
		obj.setDataPipsNegativos(one.get("dataPipsNegativos", ArrayList.class));

		obj.setDataPipsRetroceso(one.get("dataPipsRetroceso", ArrayList.class));
		obj.setDataPipsRetrocesoPositivos(one.get("dataPipsRetrocesoPositivos", ArrayList.class));
		obj.setDataPipsRetrocesoNegativos(one.get("dataPipsRetrocesoNegativos", ArrayList.class));
	}

	@Override
	public Map<String, Object> toMapForDelete(MongoEstadistica obj, Date fechaReferencia) {
		throw new UnsupportedOperationException("Operacion no soportada");
	}
}

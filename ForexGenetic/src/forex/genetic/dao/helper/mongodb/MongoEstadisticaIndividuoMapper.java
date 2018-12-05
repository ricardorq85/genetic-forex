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
		objectMap.put("cantidadTotal", obj.getCantidadTotal());
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

		obj.setCantidadPositivos((Integer)one.getOrDefault("cantidadPositivos", 0));
		obj.setCantidadNegativos((Integer)one.getOrDefault("cantidadNegativos", 0));
		obj.setCantidadTotal((Integer)one.getOrDefault("cantidadTotal", 0));

		obj.setPipsPositivos((Double) one.getOrDefault("pipsPositivos", 0.0D));
		obj.setPipsPositivos((Double) one.getOrDefault("pipsPositivos", 0.0D));
		obj.setPipsNegativos((Double) one.getOrDefault("pipsNegativos", 0.0D));
		obj.setPips((Double) one.getOrDefault("pips", 0.0D));
		obj.setPipsMinimosPositivos((Double) one.getOrDefault("pipsMinimosPositivos", 0.0D));
		obj.setPipsMinimosNegativos((Double) one.getOrDefault("pipsMinimosNegativos", 0.0D));
		obj.setPipsMinimos((Double) one.getOrDefault("pipsMinimos", 0.0D));
		obj.setPipsMaximosPositivos((Double) one.getOrDefault("pipsMaximosPositivos", 0.0D));
		obj.setPipsMaximosNegativos((Double) one.getOrDefault("pipsMaximosNegativos", 0.0D));
		obj.setPipsMaximos((Double) one.getOrDefault("pipsMaximos", 0.0D));
		obj.setPipsPromedioPositivos((Double) one.getOrDefault("pipsPromedioPositivos", 0.0D));
		obj.setPipsPromedioNegativos((Double) one.getOrDefault("pipsPromedioNegativos", 0.0D));
		obj.setPipsPromedio((Double) one.getOrDefault("pipsPromedio", 0.0D));

		obj.setPipsModaPositivos((Double) one.getOrDefault("pipsModaPositivos", 0.0D));
		obj.setPipsModaNegativos((Double) one.getOrDefault("pipsModaNegativos", 0.0D));
		obj.setPipsModa((Double) one.getOrDefault("pipsModa", 0.0D));
		obj.setPipsMaximosRetrocesoPositivos((Double) one.getOrDefault("pipsMaximosRetrocesoPositivos", 0.0D));
		obj.setPipsMaximosRetrocesoNegativos((Double) one.getOrDefault("pipsMaximosRetrocesoNegativos", 0.0D));
		obj.setPipsMaximosRetroceso((Double) one.getOrDefault("pipsMaximosRetroceso", 0.0D));
		obj.setPipsMinimosRetrocesoPositivos((Double) one.getOrDefault("pipsMinimosRetrocesoPositivos", 0.0D));
		obj.setPipsMinimosRetrocesoNegativos((Double) one.getOrDefault("pipsMinimosRetrocesoNegativos", 0.0D));
		obj.setPipsMinimosRetroceso((Double) one.getOrDefault("pipsMinimosRetroceso", 0.0D));
		obj.setPipsPromedioRetrocesoPositivos((Double) one.getOrDefault("pipsPromedioRetrocesoPositivos", 0.0D));
		obj.setPipsPromedioRetrocesoNegativos((Double) one.getOrDefault("pipsPromedioRetrocesoNegativos", 0.0D));
		obj.setPipsPromedioRetroceso((Double) one.getOrDefault("pipsPromedioRetroceso", 0.0D));
		obj.setPipsModaRetrocesoPositivos((Double) one.getOrDefault("pipsModaRetrocesoPositivos", 0.0D));
		obj.setPipsModaRetrocesoNegativos((Double) one.getOrDefault("pipsModaRetrocesoNegativos", 0.0D));
		obj.setPipsModaRetroceso((Double) one.getOrDefault("pipsModaRetroceso", 0.0D));
		obj.setDuracionMinimaPositivos((Double) one.getOrDefault("duracionMinimaPositivos", 0.0D));
		obj.setDuracionMinimaNegativos((Double) one.getOrDefault("duracionMinimaNegativos", 0.0D));
		obj.setDuracionMinima((Double) one.getOrDefault("duracionMinima", 0.0D));
		obj.setDuracionMaximaPositivos((Double) one.getOrDefault("duracionMaximaPositivos", 0.0D));
		obj.setDuracionMaximaNegativos((Double) one.getOrDefault("duracionMaximaNegativos", 0.0D));
		obj.setDuracionMaxima((Double) one.getOrDefault("duracionMaxima", 0.0D));
		obj.setDuracionPromedioPositivos((Double) one.getOrDefault("duracionPromedioPositivos", 0.0D));
		obj.setDuracionPromedioNegativos((Double) one.getOrDefault("duracionPromedioNegativos", 0.0D));
		obj.setDuracionPromedio((Double) one.getOrDefault("duracionPromedio", 0.0D));
		obj.setDuracionModaPositivos((Double) one.getOrDefault("duracionModaPositivos", 0.0D));
		obj.setDuracionModaNegativos((Double) one.getOrDefault("duracionModaNegativos", 0.0D));
		obj.setDuracionModa((Double) one.getOrDefault("duracionModa", 0.0D));
		obj.setDuracionDesvEstandarPositivos((Double) one.getOrDefault("duracionDesvEstandarPositivos", 0.0D));
		obj.setDuracionDesvEstandarNegativos((Double) one.getOrDefault("duracionDesvEstandarNegativos", 0.0D));
		obj.setDuracionDesvEstandar((Double) one.getOrDefault("duracionDesvEstandar", 0.0D));

		obj.setDuracionTotal((Double) one.getOrDefault("duracionTotal", 0L));
		obj.setDuracionTotalPositivos((Double) one.getOrDefault("duracionTotalPositivos", 0L));
		obj.setDuracionTotalNegativos((Double) one.getOrDefault("duracionTotalNegativos", 0L));

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

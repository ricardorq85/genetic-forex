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
		objectMap.put("fechaFinal", obj.getFechaFinal());
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

		return objectMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MongoEstadistica helpOne(Document one) {
		MongoEstadistica obj = new MongoEstadistica();

		obj.setIdIndividuo(one.getString("idIndividuo"));
		obj.setFechaInicial(one.getDate("fechaInicial"));
		obj.setFechaFinal(one.getDate("fechaFinal"));

		obj.setCantidadPositivos(one.getInteger("cantidadPositivos"));
		obj.setCantidadNegativos(one.getInteger("cantidadNegativos"));
		obj.setCantidadTotal(one.getInteger("cantidadTotal"));
		obj.setPipsPositivos(one.getDouble("pipsPositivos"));
		obj.setPipsNegativos(one.getDouble("pipsNegativos"));
		obj.setPips(one.getDouble("pips"));
		obj.setPipsMinimosPositivos(one.getDouble("pipsMinimosPositivos"));
		obj.setPipsMinimosNegativos(one.getDouble("pipsMinimosNegativos"));
		obj.setPipsMinimos(one.getDouble("pipsMinimos"));
		obj.setPipsMaximosPositivos(one.getDouble("pipsMaximosPositivos"));
		obj.setPipsMaximosNegativos(one.getDouble("pipsMaximosNegativos"));
		obj.setPipsMaximos(one.getDouble("pipsMaximos"));
		obj.setPipsPromedioPositivos(one.getDouble("pipsPromedioPositivos"));
		obj.setPipsPromedioNegativos(one.getDouble("pipsPromedioNegativos"));
		obj.setPipsPromedio(one.getDouble("pipsPromedio"));
		obj.setPipsModaPositivos(one.getDouble("pipsModaPositivos"));
		obj.setPipsModaNegativos(one.getDouble("pipsModaNegativos"));
		obj.setPipsModa(one.getDouble("pipsModa"));
		obj.setPipsMaximosRetrocesoPositivos(one.getDouble("pipsMaximosRetrocesoPositivos"));
		obj.setPipsMaximosRetrocesoNegativos(one.getDouble("pipsMaximosRetrocesoNegativos"));
		obj.setPipsMaximosRetroceso(one.getDouble("pipsMaximosRetroceso"));
		obj.setPipsMinimosRetrocesoPositivos(one.getDouble("pipsMinimosRetrocesoPositivos"));
		obj.setPipsMinimosRetrocesoNegativos(one.getDouble("pipsMinimosRetrocesoNegativos"));
		obj.setPipsMinimosRetroceso(one.getDouble("pipsMinimosRetroceso"));
		obj.setPipsPromedioRetrocesoPositivos(one.getDouble("pipsPromedioRetrocesoPositivos"));
		obj.setPipsPromedioRetrocesoNegativos(one.getDouble("pipsPromedioRetrocesoNegativos"));
		obj.setPipsPromedioRetroceso(one.getDouble("pipsPromedioRetroceso"));
		obj.setPipsModaRetrocesoPositivos(one.getDouble("pipsModaRetrocesoPositivos"));
		obj.setPipsModaRetrocesoNegativos(one.getDouble("pipsModaRetrocesoNegativos"));
		obj.setPipsModaRetroceso(one.getDouble("pipsModaRetroceso"));
		obj.setDuracionMinimaPositivos(one.getDouble("duracionMinimaPositivos"));
		obj.setDuracionMinimaNegativos(one.getDouble("duracionMinimaNegativos"));
		obj.setDuracionMinima(one.getDouble("duracionMinima"));
		obj.setDuracionMaximaPositivos(one.getDouble("duracionMaximaPositivos"));
		obj.setDuracionMaximaNegativos(one.getDouble("duracionMaximaNegativos"));
		obj.setDuracionMaxima(one.getDouble("duracionMaxima"));
		obj.setDuracionPromedioPositivos(one.getDouble("duracionPromedioPositivos"));
		obj.setDuracionPromedioNegativos(one.getDouble("duracionPromedioNegativos"));
		obj.setDuracionPromedio(one.getDouble("duracionPromedio"));
		obj.setDuracionModaPositivos(one.getDouble("duracionModaPositivos"));
		obj.setDuracionModaNegativos(one.getDouble("duracionModaNegativos"));
		obj.setDuracionModa(one.getDouble("duracionModa"));
		obj.setDuracionDesvEstandarPositivos(one.getDouble("duracionDesvEstandarPositivos"));
		obj.setDuracionDesvEstandarNegativos(one.getDouble("duracionDesvEstandarNegativos"));
		obj.setDuracionDesvEstandar(one.getDouble("duracionDesvEstandar"));

		obj.setDuracionTotal(one.getLong("duracionTotal"));
		obj.setDuracionTotalPositivos(one.getLong("duracionTotalPositivos"));
		obj.setDuracionTotalNegativos(one.getLong("duracionTotalNegativos"));

		obj.setDataDuracion(one.get("dataDuracion", ArrayList.class));
		obj.setDataDuracionPositivos(one.get("dataDuracionPositivos", ArrayList.class));
		obj.setDataDuracionNegativos(one.get("dataDuracionNegativos", ArrayList.class));

		obj.setDataPips(one.get("dataPips", ArrayList.class));
		obj.setDataPipsPositivos(one.get("dataPipsPositivos", ArrayList.class));
		obj.setDataPipsNegativos(one.get("dataPipsNegativos", ArrayList.class));

		return obj;
	}

	@Override
	public Map<String, Object> toMapForDelete(MongoEstadistica obj, Date fechaReferencia) {
		throw new UnsupportedOperationException("Operacion no soportada");
	}
}

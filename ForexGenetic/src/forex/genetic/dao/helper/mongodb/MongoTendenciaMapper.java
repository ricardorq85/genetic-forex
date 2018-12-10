package forex.genetic.dao.helper.mongodb;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import forex.genetic.entities.Tendencia;

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
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("idIndividuo", obj.getIndividuo().getId());
		objectMap.put("fechaBase", obj.getFechaBase());
		objectMap.put("precioBase", obj.getPrecioBase());
		objectMap.put("fechaTendencia", obj.getFechaTendencia());
		objectMap.put("pips", obj.getPips());
		objectMap.put("precioCalculado", obj.getPrecioCalculado());
		objectMap.put("tipoTendencia", obj.getTipoTendencia());
		objectMap.put("fechaApertura", obj.getFechaApertura());
		objectMap.put("precioApertura", obj.getPrecioApertura());
		objectMap.put("duracion", obj.getDuracion());
		objectMap.put("pipsActuales", obj.getPipsActuales());
		objectMap.put("duracionActual", obj.getDuracionActual());
		objectMap.put("propabilidadPipsPositivos", obj.getProbabilidadPositivos());
		objectMap.put("probabilidadPipsNegativos", obj.getProbabilidadNegativos());
		objectMap.put("probabilidad", obj.getProbabilidad());
		objectMap.put("fecha", obj.getFecha());
		objectMap.put("fechaCierre", obj.getFechaCierre());
		objectMap.put("tipoCalculo", obj.getTipoCalculo());
		objectMap.put("pipsReales", obj.getPipsReales());

		return objectMap;
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

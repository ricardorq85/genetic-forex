package forex.genetic.dao.helper.mongodb;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import forex.genetic.entities.Tendencia;
import forex.genetic.entities.mongo.MongoIndividuo;

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
		Tendencia obj = new Tendencia();
		MongoIndividuo individuo = new MongoIndividuo();
		individuo.setId(one.getString("idIndividuo"));
		obj.setIndividuo(individuo);
		obj.setFechaBase(one.getDate("fechaBase"));
		obj.setPrecioBase(one.getDouble("precioBase"));
		obj.setFechaTendencia(one.getDate("fechaTendencia"));
		obj.setPips(one.getDouble("pips"));
		obj.setPrecioCalculado(one.getDouble("precioCalculado"));
		obj.setTipoTendencia(one.getString("tipoTendencia"));
		obj.setFechaApertura(one.getDate("fechaApertura"));
		obj.setPrecioApertura(one.getDouble("precioApertura"));
		obj.setDuracion(one.getLong("duracion"));
		obj.setPipsActuales(one.getDouble("pipsActuales"));
		obj.setDuracionActual(one.getLong("duracionActual"));
		obj.setProbabilidadPositivos(one.getDouble("probabilidadPositivos"));
		obj.setProbabilidadNegativos(one.getDouble("probabilidadNegativos"));
		obj.setProbabilidad(one.getDouble("probabilidad"));
		obj.setFecha(one.getDate("fecha"));
		obj.setFechaCierre(one.getDate("fechaCierre"));
		obj.setTipoCalculo(one.getString("tipoCalculo"));
		obj.setPipsReales(one.getDouble("pipsReales"));
		
		return obj;
	}
}

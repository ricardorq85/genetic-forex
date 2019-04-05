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
		objectMap.put("probabilidadPositivos", obj.getProbabilidadPositivos());
		objectMap.put("probabilidadNegativos", obj.getProbabilidadNegativos());
		objectMap.put("probabilidad", obj.getProbabilidad());
		objectMap.put("fecha", obj.getFecha());
		objectMap.put("fechaCierre", obj.getFechaCierre());
		objectMap.put("tipoCalculo", obj.getTipoCalculo());
		objectMap.put("pipsReales", obj.getPipsReales());
		objectMap.put("duracionMinutos", obj.getDuracionMinutos());

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
		obj.setPrecioBase(new Double(one.get("precioBase").toString()));
		obj.setFechaTendencia(one.getDate("fechaTendencia"));
//		obj.setPips(one.getDouble("pips"));
		obj.setPips(new Double(one.get("pips").toString()));
		obj.setPrecioCalculado(new Double(one.get("precioCalculado").toString()));
		obj.setTipoTendencia(one.getString("tipoTendencia"));
		obj.setFechaApertura(one.getDate("fechaApertura"));
		obj.setPrecioApertura(new Double(one.get("precioApertura").toString()));
		obj.setDuracion(new Long(one.get("duracion").toString()));
		obj.setPipsActuales(new Double(one.get("pipsActuales").toString()));
		obj.setDuracionActual(new Long(one.get("duracionActual").toString()));
		obj.setProbabilidadPositivos(one.getDouble("probabilidadPositivos"));
		obj.setProbabilidadNegativos(one.getDouble("probabilidadNegativos"));
		obj.setProbabilidad(one.getDouble("probabilidad"));
		obj.setFecha(one.getDate("fecha"));
		obj.setFechaCierre(one.getDate("fechaCierre"));
		obj.setTipoCalculo(one.getString("tipoCalculo"));
		obj.setPipsReales(new Double(one.get("pipsReales").toString()));
		obj.setDuracionMinutos(new Long(one.get("duracionMinutos").toString()));
		
		return obj;
	}
}

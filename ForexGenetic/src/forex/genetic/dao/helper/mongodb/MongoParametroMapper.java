package forex.genetic.dao.helper.mongodb;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import forex.genetic.bo.Parametro;
import forex.genetic.entities.dto.ParametroDTO;

public class MongoParametroMapper extends MongoMapper<Parametro> {

	@Override
	public Map<String, Object> toPrimaryKeyMap(Parametro obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("nombre", obj.getParametro().getNombre());
		return objectMap;
	}

	public Map<String, Object> toMap(Parametro obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("nombre", obj.getParametro().getNombre());
		objectMap.put("valor", obj.getParametro().getValor());
		objectMap.put("fecha", obj.getParametro().getFecha());
		return objectMap;
	}

	@Override
	public Parametro helpOne(Document one) {
		Parametro obj = new Parametro();
		ParametroDTO dto = new ParametroDTO();
		dto.setNombre(one.getString("nombre"));
		dto.setValor(one.get("valor"));
		dto.setFecha(one.getDate("fecha"));
		
		obj.setParametro(dto);
		return obj;
	}
	
	public Parametro helpOneDate(Document one) {
		Parametro obj = new Parametro();
		ParametroDTO dto = new ParametroDTO();
		dto.setNombre(one.getString("nombre"));
		dto.setValor(one.getDate("valor"));
		dto.setFecha(one.getDate("fecha"));
		
		obj.setParametro(dto);
		return obj;
	}


	@Override
	public Map<String, Object> toMapForDelete(Parametro obj, Date fechaReferencia) {
		throw new UnsupportedOperationException();
	}
}

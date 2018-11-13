package forex.genetic.dao.helper.mongodb;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import forex.genetic.entities.dto.ParametroDTO;

public class MongoParametroMapper extends MongoMapper<ParametroDTO> {

	@Override
	public Map<String, Object> toPrimaryKeyMap(ParametroDTO obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("nombre", obj.getNombre());
		return objectMap;
	}

	public Map<String, Object> toMap(ParametroDTO obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("nombre", obj.getNombre());
		objectMap.put("valor", obj.getValor());
		objectMap.put("fecha", obj.getFecha());
		return objectMap;
	}

	@Override
	public ParametroDTO helpOne(Document one) {
		ParametroDTO obj = new ParametroDTO();
		obj.setNombre(one.getString("nombre"));
		obj.setValor(one.get("valor"));
		obj.setFecha(one.getDate("fecha"));
		return obj;
	}

	@Override
	public Map<String, Object> toMapForDelete(ParametroDTO obj, Date fechaReferencia) {
		throw new UnsupportedOperationException();
	}
}

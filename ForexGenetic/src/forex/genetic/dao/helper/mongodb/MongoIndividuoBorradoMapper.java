package forex.genetic.dao.helper.mongodb;

import java.util.Date;
import java.util.Map;

import forex.genetic.entities.mongo.MongoIndividuo;

public class MongoIndividuoBorradoMapper extends MongoIndividuoMapper {

	@Override
	public Map<String, Object> toMap(MongoIndividuo obj) {
		Map<String, Object> objectMap = super.toMap(obj);
		objectMap.put("tipoBorrado", obj.getCausaBorrado());
		objectMap.put("idParentBorrado", obj.getIdParentBorrado());
		objectMap.put("fechaBorrado", new Date());

		return objectMap;
	}
}

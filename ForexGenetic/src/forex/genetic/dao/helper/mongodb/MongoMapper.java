package forex.genetic.dao.helper.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;
import org.bson.Document;

import com.mongodb.client.MongoCursor;

import forex.genetic.entities.Individuo;

public abstract class MongoMapper<T> {

	public abstract Map<String, Object> toPrimaryKeyMap(T obj);

	public abstract Map<String, Object> toMap(T obj);

	public abstract Map<String, Object> toMapForDelete(T obj, Date fechaReferencia);

	public abstract T helpOne(Document one);

	public Map<String, Object> toMapForUpdate(T obj) {
		return toMap(obj);
	}

	public Map<String, Object> toMapForDeleteByIndividuo(Individuo obj) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		objectMap.put("idIndividuo", obj.getId());
		return objectMap;
	}

	public List<String> helpJsonList(List<MongoCursor<Document>> documents) {
		List<String> list = new ArrayList<String>();
		String hostname = SystemUtils.getHostName();
		for (MongoCursor<Document> cursor : documents) {
			try {
				cursor.forEachRemaining(elem -> {
					if (!elem.containsKey("hostName")) {
						elem.append("hostName", hostname);
					}
					list.add(elem.toJson());
				});
			} finally {
				cursor.close();
			}
		}
		return list;
	}

	public List<T> helpList(MongoCursor<Document> cursor) {
		List<T> list = new ArrayList<T>();
		try {
			while (cursor.hasNext()) {
				T one = helpOne(cursor.next());
				list.add(one);
			}
		} finally {
			cursor.close();
		}
		return list;
	}

	public List<Document> toMap(List<T> datos) {
		List<Document> objectMaps = new ArrayList<Document>(datos.size());
		datos.stream().forEach(dato -> {
			objectMaps.add(new Document(toMap(dato)));
		});
		return objectMaps;
	}

}

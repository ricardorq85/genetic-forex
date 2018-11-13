package forex.genetic.dao.helper.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCursor;

public abstract class MongoMapper<T> {

	public abstract Map<String, Object> toPrimaryKeyMap(T obj);

	public abstract Map<String, Object> toMap(T obj);

	public abstract Map<String, Object> toMapForDelete(T obj, Date fechaReferencia);

	public abstract T helpOne(Document one);

	public List<? extends T> helpList(MongoCursor<Document> cursor) {
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

	public List<Document> toMap(List<? extends T> datos) {
		List<Document> objectMaps = new ArrayList<Document>(datos.size());
		datos.stream().forEach(dato -> {
			objectMaps.add(new Document(toMap(dato)));
		});
		return objectMaps;
	}

}

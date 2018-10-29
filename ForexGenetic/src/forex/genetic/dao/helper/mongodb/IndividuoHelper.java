package forex.genetic.dao.helper.mongodb;

import org.bson.Document;

import com.mongodb.client.MongoCursor;

public class IndividuoHelper {

	public static void helpIndividuos(MongoCursor<Document> cursor) {
		try {
		    while (cursor.hasNext()) {
		        System.out.println(cursor.next().toJson());
		    }
		} finally {
		    cursor.close();
		}
	}
	
}

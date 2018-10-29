	package forex.genetic.dao.mongodb;

import java.util.Date;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import forex.genetic.dao.helper.mongodb.IndividuoHelper;
import forex.genetic.util.jdbc.mongodb.ConnectionMongoDB;

public class IndividuosDAO {

	public void saveIndividuo() {
		
		MongoCollection<Document> collection = ConnectionMongoDB.getDatabase().getCollection("individuos", Document.class);
		System.out.println(collection.countDocuments());
		Date date = new Date();
		Document individuo = new Document(
				"ID_INDIVIDUO", "" + date.getTime())
                .append("CREATION_DATE", new Date())
                .append("TAKE_PROFIT", 300);
		
		collection.insertOne(individuo);
		System.out.println(collection.countDocuments());
	}
	
	public void listIndividuos(String idIndividuo) {
		MongoCollection<Document> collection = ConnectionMongoDB.getDatabase().getCollection("individuos", Document.class);
		
		Document query = new Document("ID_INDIVIDUO", idIndividuo);
		
		Document individuoConsultado = collection.find(query).first();
		System.out.println(individuoConsultado);
		
	}
	
	public void listAllIndividuos() {
		MongoCollection<Document> collection = ConnectionMongoDB.getDatabase().getCollection("individuos", Document.class);
		
		MongoCursor<Document> cursor = collection.find().iterator();
		IndividuoHelper.helpIndividuos(cursor);
	}
	
}

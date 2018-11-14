package forex.genetic.util.jdbc.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class ConnectionMongoDB {

	private final static String URI = "mongodb://localhost:27017";
	private final static MongoClient mongoClient = new MongoClient(new MongoClientURI(URI));
	// final static CodecRegistry pojoCodecRegistry =
	// fromRegistries(MongoClient.getDefaultCodecRegistry(),
	// fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	final static MongoDatabase db = mongoClient.getDatabase("forex");
	// .withCodecRegistry(pojoCodecRegistry);;

	public static MongoDataClient getMongoDataClient() {
		return new MongoDataClient(mongoClient);
	}

	public static MongoDatabase getDatabase() {
		return db;
	}

}

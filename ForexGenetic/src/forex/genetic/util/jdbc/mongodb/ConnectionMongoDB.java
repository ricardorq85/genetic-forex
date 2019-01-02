package forex.genetic.util.jdbc.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import forex.genetic.manager.PropertiesManager;

public class ConnectionMongoDB {

//	private final static String URI = "mongodb://localhost:27017";
//	private final static String URI_AZURE = "mongodb://usdcad:YJFJSs0Tg7fivJgBRitPAoc8vC4XHfpbkdw99dRRgrq7GoezTdokStq9ExV099Es4e9U11e1EQSRFxlnELWm7w==@usdcad.documents.azure.com:10255/?ssl=true&replicaSet=globaldb";
	private static MongoClient mongoClient;
	// final static CodecRegistry pojoCodecRegistry =
	// fromRegistries(MongoClient.getDefaultCodecRegistry(),
	// fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	private static MongoDatabase db;
	// .withCodecRegistry(pojoCodecRegistry);;

	private static MongoClient getMongoClient() {
		if (mongoClient == null) {
			String uri = PropertiesManager.getUriMongoDB();
			mongoClient = new MongoClient(new MongoClientURI(uri));
		}
		return mongoClient;
	}

	public static MongoDataClient getMongoDataClient() {
		return new MongoDataClient(getMongoClient());
	}

	public static MongoDatabase getDatabase() {
		if (db == null) {
			db = getMongoClient().getDatabase("forex");
		}
		return db;
	}

}

package forex.genetic.util.jdbc.mongodb;

import java.util.logging.Level;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import forex.genetic.manager.PropertiesManager;

public class ConnectionMongoDB {

//	private final static String URI = "mongodb://localhost:27017";
//	private final static String URI_AZURE = "mongodb://usdcad:YJFJSs0Tg7fivJgBRitPAoc8vC4XHfpbkdw99dRRgrq7GoezTdokStq9ExV099Es4e9U11e1EQSRFxlnELWm7w==@usdcad.documents.azure.com:10255/?ssl=true&replicaSet=globaldb";
	// final static CodecRegistry pojoCodecRegistry =
	// fromRegistries(MongoClient.getDefaultCodecRegistry(),
	// fromProviders(PojoCodecProvider.builder().automatic(true).build()));
//	private static MongoDatabase db;
	// .withCodecRegistry(pojoCodecRegistry);;

	static {
		java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
	}

	private static MongoClient getMongoClient() {
		String uri = PropertiesManager.getUriMongoDB();
		MongoClient mongoClient = new MongoClient(new MongoClientURI(uri));
		return mongoClient;
	}

	public static MongoDataClient getMongoDataClient() {
		return new MongoDataClient(getMongoClient());
	}

}

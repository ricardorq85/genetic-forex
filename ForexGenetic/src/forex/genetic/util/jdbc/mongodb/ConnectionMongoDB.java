package forex.genetic.util.jdbc.mongodb;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class ConnectionMongoDB {

	final static String URI = "mongodb://localhost:27017";
	final static MongoClient mongoClient = new MongoClient(new MongoClientURI(URI));
	//final static CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
      //      fromProviders(PojoCodecProvider.builder().automatic(true).build()));
	final static MongoDatabase db = mongoClient.getDatabase("forex");
			//.withCodecRegistry(pojoCodecRegistry);;

	public static MongoDatabase getDatabase() {
		return db;
	}
		
}

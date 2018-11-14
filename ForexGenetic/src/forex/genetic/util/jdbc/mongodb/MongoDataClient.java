package forex.genetic.util.jdbc.mongodb;

import com.mongodb.MongoClient;

import forex.genetic.util.jdbc.DataClient;

public class MongoDataClient extends DataClient<MongoClient> {

	public MongoDataClient(MongoClient client) {
		super(client);
	}
}

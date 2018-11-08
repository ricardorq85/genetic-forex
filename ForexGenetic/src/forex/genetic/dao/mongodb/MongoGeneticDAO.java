package forex.genetic.dao.mongodb;

import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.util.jdbc.mongodb.ConnectionMongoDB;

public abstract class MongoGeneticDAO<E> extends GeneticDAO<E> {

	protected MongoCollection<Document> collection = null;

	public MongoGeneticDAO(String collectionName) {
		this.collection = ConnectionMongoDB.getDatabase().getCollection(collectionName);
		this.configureCollection();
	}

	protected abstract void configureCollection();
	public abstract void insertOrUpdate(E obj);
	public abstract void insertMany(List<E> datos);

	public void clean() {
		this.collection.drop();
	}
}

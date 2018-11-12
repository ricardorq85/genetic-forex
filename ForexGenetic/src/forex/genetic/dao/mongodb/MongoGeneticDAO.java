package forex.genetic.dao.mongodb;

import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.mongodb.ConnectionMongoDB;

public abstract class MongoGeneticDAO<E> extends GeneticDAO<E> {

	private String collectionName = null;
	protected MongoCollection<Document> collection = null;

	public MongoGeneticDAO(String collectionName, boolean configure) {
		this.setCollectionName(collectionName);
		this.collection = ConnectionMongoDB.getDatabase().getCollection(collectionName);
		if (configure) {
			LogUtil.logTime(new StringBuilder("Configurando collection: ").append(collectionName).toString(), 1);
			this.configureCollection();
		}
	}

	public abstract void configureCollection();

	public abstract void insertOrUpdate(E obj);

	public abstract void insertMany(List<? extends E> datos);

	public void clean() {
		this.collection.drop();
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}
}

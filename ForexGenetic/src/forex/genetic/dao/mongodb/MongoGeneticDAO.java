package forex.genetic.dao.mongodb;

import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;

import forex.genetic.dao.IGeneticDAO;
import forex.genetic.dao.helper.mongodb.MongoMapper;
import forex.genetic.dao.helper.mongodb.MongoMapperFactory;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.mongodb.ConnectionMongoDB;

public abstract class MongoGeneticDAO<E> implements IGeneticDAO<E> {

	private String collectionName = null;
	protected MongoMapper<E> mapper;
	protected MongoCollection<Document> collection = null;

	public MongoGeneticDAO(String collectionName, boolean configure) {
		this.setCollectionName(collectionName);
		this.collection = ConnectionMongoDB.getDatabase().getCollection(collectionName);
		this.mapper = (MongoMapper<E>) MongoMapperFactory.get(collectionName);
		if (configure) {
			LogUtil.logTime(new StringBuilder("Configurando collection: ").append(collectionName).toString(), 1);
			this.configureCollection();
		}
	}

	public abstract void configureCollection();

	public void insertMany(List<? extends E> datos) {
		List<Document> docs = mapper.toMap(datos);
		InsertManyOptions options = new InsertManyOptions();
		options.bypassDocumentValidation(true);
		this.collection.insertMany(docs, options);
	}

	public void insertOrUpdate(E obj) {
		Document filterPk = new Document(mapper.toPrimaryKeyMap(obj));
		Document doc = new Document("$set", mapper.toMap(obj));
		UpdateOptions options = new UpdateOptions();
		options.upsert(true);
		this.collection.updateOne(filterPk, doc, options);
	}

	public void clean() {
		this.collection.drop();
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public long delete(E obj, Date fechaReferencia) {
		Document doc = new Document(mapper.toMapForDelete(obj, fechaReferencia));
		DeleteResult result = this.collection.deleteMany(doc);
		return result.getDeletedCount();
	}

	@Override
	public void close() throws GeneticDAOException {
	}

	@Override
	public void commit() throws GeneticDAOException {
	}

	@Override
	public boolean exists(E obj) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void insert(E obj) throws GeneticDAOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(E obj) throws GeneticDAOException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isClosed() throws GeneticDAOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void restoreConnection() throws GeneticDAOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollback() throws GeneticDAOException {
		// TODO Auto-generated method stub

	}

}

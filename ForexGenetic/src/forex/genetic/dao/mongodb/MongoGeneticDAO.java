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
	private MongoMapper<E> mapper;
	protected MongoCollection<Document> collection = null;

	public MongoGeneticDAO(String name, boolean configure) {
		this.setMapper((MongoMapper<E>) MongoMapperFactory.get(name));
		setCollection(name, configure);
	}

	protected void setCollection(String name, boolean configure) {
		this.setCollectionName(name);
		this.collection = ConnectionMongoDB.getDatabase().getCollection(name);
		if (configure) {
			LogUtil.logTime(new StringBuilder("Configurando collection: ").append(name).toString(), 1);
			this.configureCollection();
		}
	}

	public abstract void configureCollection();

	public void insertMany(List<E> datos) {
		List<Document> docs = getMapper().toMap(datos);
		this.insertManyDocuments(docs);
	}

	public void insertManyDocuments(List<Document> docs) {
		InsertManyOptions options = new InsertManyOptions();
		options.bypassDocumentValidation(true);
		this.collection.insertMany(docs, options);
	}

	public void insertOrUpdate(E obj) {
		Document filterPk = new Document(getMapper().toPrimaryKeyMap(obj));
		Document doc = new Document("$set", getMapper().toMap(obj));
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
		Document doc = new Document(getMapper().toMapForDelete(obj, fechaReferencia));
		DeleteResult result = this.collection.deleteMany(doc);
		return result.getDeletedCount();
	}

	@Override
	public void close() throws GeneticDAOException {
	}

	@Override
	public void commit() throws GeneticDAOException {
	}

	public void insertIfNoExists(E obj) throws GeneticDAOException {
		if (!exists(obj)) {
			insert(obj);
		}
	}

	@Override
	public boolean exists(E obj) throws GeneticDAOException {
		Document filterPk = new Document(getMapper().toPrimaryKeyMap(obj));
		return (this.collection.countDocuments(filterPk) > 0);
	}

	@Override
	public void insert(E obj) {
		Document doc = new Document(getMapper().toMap(obj));
		this.collection.insertOne(doc);
	}

	@Override
	public void update(E obj) throws GeneticDAOException {
		Document filterPk = new Document(getMapper().toPrimaryKeyMap(obj));
		Document doc = new Document("$set", getMapper().toMap(obj));
		this.collection.updateOne(filterPk, doc);
	}

	@Override
	public boolean isClosed() throws GeneticDAOException {
		return false;
	}

	@Override
	public void restoreConnection() throws GeneticDAOException {
	}

	@Override
	public void rollback() throws GeneticDAOException {
	}

	public MongoMapper<E> getMapper() {
		return mapper;
	}

	public void setMapper(MongoMapper<E> mapper) {
		this.mapper = mapper;
	}

}

package forex.genetic.dao.mongodb;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;
import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.DeleteResult;

import forex.genetic.dao.IGeneticDAO;
import forex.genetic.dao.helper.mongodb.MongoMapper;
import forex.genetic.dao.helper.mongodb.MongoMapperFactory;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.Individuo;
import forex.genetic.util.LogUtil;

public abstract class MongoGeneticDAO<E> implements IGeneticDAO<E> {

	private MongoDatabase database;
	private String collectionName = null;
	private MongoMapper<E> mapper;
	protected MongoCollection<Document> collection = null;

	@SuppressWarnings("unchecked")
	public MongoGeneticDAO(MongoDatabase db, String name, boolean configure) {
		database = db;
		this.setMapper((MongoMapper<E>) MongoMapperFactory.get(name));
		setCollection(name, configure);
	}

	protected void setCollection(String name, boolean configure) {
		if ((collectionName == null) || (!collectionName.equals(name))) {
			this.setCollectionName(name);
			this.collection = database.getCollection(name);
			if (configure) {
				LogUtil.logTime(new StringBuilder("Configurando collection: ").append(name).toString(), 3);
				this.configureCollection();
			}
		}
	}

	public abstract void configureCollection();

	protected List<MongoCursor<Document>> findJsonCursor(DateInterval interval) {
		return Collections.<MongoCursor<Document>>emptyList();
	}

	public List<String> findJson(DateInterval interval) {
		List<MongoCursor<Document>> documents = findJsonCursor(interval);
		List<String> list = getMapper().helpJsonList(documents);
		return list;
	}

	public List<E> findAll() {
		MongoCursor<Document> cursor = this.collection.find().iterator();
		List<E> list = getMapper().helpList(cursor);
		return list;
	}

	public void insertMany(List<E> datos) {
		List<Document> docs = getMapper().toMap(datos);
		this.insertManyDocuments(docs);
	}

	public void insertManyDocuments(List<Document> docs) {
		InsertManyOptions options = new InsertManyOptions();
		options.bypassDocumentValidation(true);
		docs.forEach((u) -> u.append("hostName", SystemUtils.getHostName()));
		this.collection.insertMany(docs, options);
	}

	public void insertOrUpdate(E obj) {
		Document filterPk = new Document(getMapper().toPrimaryKeyMap(obj));

		Map<String, Object> mapObject = getMapper().toMap(obj);
		mapObject.putIfAbsent("hostName", SystemUtils.getHostName());		
		Document doc = new Document("$set", mapObject);

		UpdateOptions options = new UpdateOptions();
		options.upsert(true);

		this.collection.updateOne(filterPk, doc, options);
		System.out.print("#");
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
	public void close() {
	}

	@Override
	public void commit() {
	}

	public void insertIfNoExists(E obj) {
		if (!exists(obj)) {
			insert(obj);
		}
	}

	@Override
	public boolean exists(E obj) {
		Document filterPk = new Document(getMapper().toPrimaryKeyMap(obj));
		return (this.collection.countDocuments(filterPk) > 0);
	}

	@Override
	public void insert(E obj) {
		Map<String, Object> mapObject = getMapper().toMap(obj);
		mapObject.putIfAbsent("hostName", SystemUtils.getHostName());		
		Document doc = new Document(mapObject);
		this.collection.insertOne(doc);
	}

	@Override
	public void update(E obj) {
		Document filterPk = new Document(getMapper().toPrimaryKeyMap(obj));
		Map<String, Object> mapObject = getMapper().toMapForUpdate(obj);
		mapObject.putIfAbsent("hostName", SystemUtils.getHostName());
		Document doc = new Document("$set", mapObject);
		this.collection.updateOne(filterPk, doc);
	}

	@Override
	public boolean isClosed() {
		return false;
	}

	@Override
	public void restoreConnection() {
	}

	@Override
	public void rollback() {
	}

	public int deleteByIndividuo(Individuo individuo) {
		Document doc = new Document(getMapper().toMapForDeleteByIndividuo(individuo));
		DeleteResult result = this.collection.deleteMany(doc);
		return new Long(result.getDeletedCount()).intValue();
	}

	public MongoMapper<E> getMapper() {
		return mapper;
	}

	public void setMapper(MongoMapper<E> mapper) {
		this.mapper = mapper;
	}

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.dao.mongodb;

import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.orderBy;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;

import forex.genetic.dao.ITendenciaParaOperarDAO;
import forex.genetic.dao.helper.mongodb.MongoTendenciaParaOperarMapper;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.exception.GeneticDAOException;

/**
 *
 * @author ricardorq85
 */
public class MongoTendenciaParaOperarDAO extends MongoGeneticDAO<TendenciaParaOperar>
		implements ITendenciaParaOperarDAO {

	public MongoTendenciaParaOperarDAO(MongoDatabase db) throws GeneticDAOException {
		super(db, "tendenciaParaOperar", true);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		this.collection.createIndex(Indexes.ascending("periodo", "fechaBase", "tipoOperacion", "tipoExportacion"),
				indexOptions);
	}
	
	public Date getFechaBaseMinima() {
		Date fecha = null;
		Document doc = this.collection.find().sort(Sorts.ascending("fechaBase")).first();
		if (doc != null) {
			fecha = doc.getDate("fechaBase");
		}
		return fecha;
	}

	public List<? extends TendenciaParaOperar> consultar(Date fechaInicio) {
		List<? extends TendenciaParaOperar> list = null;
		Document filter = new Document();
		if (fechaInicio != null) {
			Map<String, Object> filterValue = new HashMap<String, Object>();
			filterValue.put("$gte", fechaInicio);
			filter.append("fechaBase", filterValue);
		}
		filter.append("activa", 1);
		MongoCursor<Document> cursor = this.collection.find(filter).sort(orderBy(ascending("fechaBase"))).iterator();
		list = ((MongoTendenciaParaOperarMapper) getMapper()).helpList(cursor);

		return list;
	}

}
package forex.genetic.dao.mongodb;

import java.util.Date;

import org.bson.Document;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

import forex.genetic.bo.Parametro;
import forex.genetic.dao.IParametroDAO;
import forex.genetic.entities.dto.ParametroDTO;
import forex.genetic.exception.GeneticDAOException;

public class MongoParametroDAO extends MongoGeneticDAO<Parametro> implements IParametroDAO {

	public MongoParametroDAO() throws GeneticDAOException {
		super("parametro", true);
	}

	public void configureCollection() throws GeneticDAOException {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		this.collection.createIndex(Indexes.ascending("valor"), indexOptions);

		configureParameters();
	}

	private void configureParameters() throws GeneticDAOException {
		String[] paramNames = { "SOURCE_EXPORTED_HISTORY_DATA_PATH", "PROCESSED_EXPORTED_HISTORY_DATA_PATH",
				"EXPORTED_PROPERTY_FILE_NAME", "SOURCE_ESTRATEGIAS_PATH", "STEP_TENDENCIA", "INDIVIDUOS_X_TENDENCIA" };
		String[] paramvalues = new String[paramNames.length];

		for (int i = 0; i < paramNames.length; i++) {
			ParametroDTO p = new ParametroDTO();
			p.setNombre(paramNames[i]);
			p.setValor(paramvalues[i]);
			p.setFecha(new Date());

			Parametro param = new Parametro(p);
			insertIfNoExists(param);
		}
	}

	public Parametro consultarByName(String nombre) {
		Parametro obj = null;
		MongoCursor<Document> cursor = this.collection.find(Filters.eq("nombre", nombre)).iterator();
		if (cursor.hasNext()) {
			obj = getMapper().helpOne(cursor.next());
		}
		return obj;
	}

	@Override
	public int getIntValorParametro(String nombre) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public float getFloatValorParametro(String nombre) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public boolean getBooleanValorParametro(String nombre) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public String getValorParametro(String nombre) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public String[] getArrayStringParametro(String nombre) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public Date getDateValorParametro(String nombre) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void updateDateValorParametro(String nombre, Date valor) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}

	@Override
	public void updateValorParametro(String nombre, String valor) throws GeneticDAOException {
		throw new UnsupportedOperationException("Operacion no soportada");
	}
}

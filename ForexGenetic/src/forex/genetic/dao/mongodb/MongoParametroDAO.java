package forex.genetic.dao.mongodb;

import java.text.ParseException;
import java.util.Date;

import org.bson.Document;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

import forex.genetic.bo.Parametro;
import forex.genetic.dao.IParametroDAO;
import forex.genetic.dao.helper.mongodb.MongoParametroMapper;
import forex.genetic.entities.dto.ParametroDTO;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.DateUtil;

public class MongoParametroDAO extends MongoGeneticDAO<Parametro> implements IParametroDAO {

	public MongoParametroDAO() throws GeneticDAOException {
		super("parametro", true);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		this.collection.createIndex(Indexes.ascending("nombre"), indexOptions);

		configureParameters();
	}

	private void configureParameters() {
		String[] paramNames = { "SOURCE_EXPORTED_HISTORY_DATA_PATH", "PROCESSED_EXPORTED_HISTORY_DATA_PATH",
				"EXPORTED_PROPERTY_FILE_NAME", "SOURCE_ESTRATEGIAS_PATH", "STEP_TENDENCIA", "INDIVIDUOS_X_TENDENCIA",
				"TIPO_EXPORTACION_TENDENCIA", "FECHA_MINIMA_CREAR_INDIVIDUO", "FECHA_MAXIMA_CREAR_INDIVIDUO",
				"MESES_RANGOOPERACIONINDICADOR", "RETROCESO_RANGOOPERACIONINDICADOR", "PIPS_RANGOOPERACIONINDICADOR",
				"CANTIDAD_MUTAR", "CANTIDAD_CRUZAR" };
		Date feMinimaCrearIndividuo = null;
		Date feMaximaCrearIndividuo = null;
		try {
			feMinimaCrearIndividuo = DateUtil.obtenerFecha("2009/01/01 00:00");
			feMaximaCrearIndividuo = DateUtil.obtenerFecha("2018/01/01 00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Object[] paramvalues = {
				"c:\\Users\\Angela\\AppData\\Roaming\\MetaQuotes\\Terminal\\Common\\Files\\export\\exported",
				"c:\\Users\\Angela\\AppData\\Roaming\\MetaQuotes\\Terminal\\Common\\Files\\export\\processed",
				"c:\\Users\\Angela\\AppData\\Roaming\\MetaQuotes\\Terminal\\Common\\Files\\export\\Export.properties",
				"c:\\Users\\Angela\\AppData\\Roaming\\MetaQuotes\\Terminal\\Common\\Files\\estrategias\\live",
				new Integer(150), new Integer(200),
				"forex.genetic.tendencia.manager.mongo.MongoProcesarTendenciasGrupalManager", feMinimaCrearIndividuo,
				feMaximaCrearIndividuo, new Integer(6), new Integer(800), new Integer(1500), new Integer(100),
				new Integer(100) };

		for (int i = 0; i < paramNames.length; i++) {
			ParametroDTO p = new ParametroDTO();
			p.setNombre(paramNames[i]);
			p.setValor(paramvalues[i]);
			p.setFecha(new Date());

			Parametro param = new Parametro(p);
			//insertIfNoExists(param);
			insertOrUpdate(param);
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
	public String getValorParametro(String nombre) throws GeneticDAOException {
		Parametro p = consultarByName(nombre);
		if ((p != null) && (p.getParametro() != null) && (p.getParametro().getValor() != null)) {
			return (p.getParametro().getValor().toString());
		} else {
			throw new GeneticDAOException("No existe el parametro:" + nombre, null);
		}
	}

	@Override
	public int getIntValorParametro(String nombre) throws GeneticDAOException {
		String str = getValorParametro(nombre);
		return (Integer.parseInt(str));
	}

	@Override
	public float getFloatValorParametro(String nombre) throws GeneticDAOException {
		String str = getValorParametro(nombre);
		return (Float.parseFloat(str));
	}

	@Override
	public boolean getBooleanValorParametro(String nombre) throws GeneticDAOException {
		String str = getValorParametro(nombre);
		return (Boolean.parseBoolean(str));
	}

	@Override
	public String[] getArrayStringParametro(String nombre) throws GeneticDAOException {
		String str = getValorParametro(nombre);
		return (str.split(",", 0));
	}

	@Override
	public Date getDateValorParametro(String nombre) throws GeneticDAOException {
		Parametro obj = null;
		MongoCursor<Document> cursor = this.collection.find(Filters.eq("nombre", nombre)).iterator();
		if (cursor.hasNext()) {
			
			obj = ((MongoParametroMapper)getMapper()).helpOneDate(cursor.next());
		}
		return (Date)obj.getParametro().getValor();
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

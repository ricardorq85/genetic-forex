package forex.genetic.dao.mongodb;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;

import forex.genetic.dao.ITendenciaDAO;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.ParametroTendenciaGenetica;
import forex.genetic.entities.ProcesoTendencia;
import forex.genetic.entities.ProcesoTendenciaBuySell;
import forex.genetic.entities.Tendencia;
import forex.genetic.exception.GeneticDAOException;

/**
 *
 * @author ricardorq85
 */
public class MongoTendenciaDAO extends MongoGeneticDAO<Tendencia> implements ITendenciaDAO {

	public MongoTendenciaDAO() {
		this(true);
	}

	public MongoTendenciaDAO(boolean configure) {
		this("tendencia", configure);
	}

	public MongoTendenciaDAO(String name, boolean configure) {
		super(name, configure);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		this.collection.createIndex(Indexes.ascending("fechaBase", "idIndividuo"), indexOptions);
		this.collection.createIndex(Indexes.ascending("fechaTendencia"));
	}

	@Override
	public Date maxFechaBaseTendencia() throws GeneticDAOException {
		Date fecha = null;
		Document doc = this.collection.find().sort(Sorts.descending("fechaBase")).first();
		if (doc != null) {
			fecha = doc.getDate("fechaBase");
		}
		return fecha;
	}

	@Override
	public List<Tendencia> consultar(ProcesoTendenciaBuySell procesoTendencia) {
		Bson filtros = Filters.and(Filters.eq("tipoTendencia", procesoTendencia.getTipoTendencia()),
				Filters.lt("duracionMinutos", procesoTendencia.getTiempoTendencia()),
				Filters.lte("fechaBase", procesoTendencia.getFechaBase()),
				Filters.gt("fechaTendencia", procesoTendencia.getFechaBase()));
		Bson sorts = Sorts.orderBy(Sorts.ascending("fechaTendencia"));
		MongoCursor<Document> cursor = this.collection.find(filtros).sort(sorts).iterator();
		return getMapper().helpList(cursor);
	}

	@Override
	public int deleteTendencia(String idIndividuo) throws GeneticDAOException {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

	@Override
	public void deleteTendencia(String idIndividuo, Date fechaBase) throws GeneticDAOException {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

	@Override
	public int deleteTendenciaMenorQue(Date fechaBase) throws GeneticDAOException {
		// DeleteOptions deleteOptions = new DeleteOptions();
		DeleteResult result = this.collection.deleteMany(Filters.lt("fechaBase", fechaBase));
		return new Long(result.getDeletedCount()).intValue();
	}

	@Override
	public List<Tendencia> consultarTendenciasActualizar() throws GeneticDAOException {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

	@Override
	public Date nextFechaBase(Date fecha) throws GeneticDAOException {
		Date fechaBase = null;
		Document doc = this.collection.find(Filters.gt("fechaBase", fecha)).projection(Projections.include("fechaBase"))
				.sort(Sorts.ascending("fechaBase")).first();
		if (doc != null) {
			fechaBase = doc.getDate("fechaBase");
		}
		return fechaBase;
	}

	@Override
	public Date maxFechaProcesoTendencia(DateInterval intervaloFechaBase) throws GeneticDAOException {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

	@Override
	public int count(Date fecha) throws GeneticDAOException {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

	@Override
	public ProcesoTendencia consultarProcesarTendencia(Date fecha, Date fecha2) throws GeneticDAOException {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

	@Override
	public ProcesoTendencia consultarProcesarTendencia(Date fecha, Date fecha2, String tipo)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

	@Override
	public List<ProcesoTendencia> consultarProcesarTendenciaDetalle(Date fecha, Date fecha2, int groupByMinutes)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

	@Override
	public List<ProcesoTendencia> consultarTendenciaGenetica(Date fecha, Date fecha2,
			ParametroTendenciaGenetica parametroTendenciaGenetica) throws GeneticDAOException {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

	@Override
	public List<Date> consultarXCantidadFechaBase(Date fechaInicio, int parametroMesesTendencia)
			throws GeneticDAOException {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

	@Override
	public Date dummyTendencia(Date fecha, int rownum) throws GeneticDAOException {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

	@Override
	public String getTabla() {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

	@Override
	public void setTabla(String tabla) {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

}

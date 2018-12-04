package forex.genetic.dao.mongodb;

import java.util.Date;
import java.util.List;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

import forex.genetic.dao.ITendenciaDAO;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.ParametroTendenciaGenetica;
import forex.genetic.entities.ProcesoTendencia;
import forex.genetic.entities.Tendencia;
import forex.genetic.exception.GeneticDAOException;

/**
 *
 * @author ricardorq85
 */
public class MongoTendenciaDAO extends MongoGeneticDAO<Tendencia> implements ITendenciaDAO {

	public MongoTendenciaDAO() throws GeneticDAOException {
		this(true);
	}

	public MongoTendenciaDAO(boolean configure) throws GeneticDAOException {
		super("tendencia", configure);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		this.collection.createIndex(Indexes.ascending("fechaBase", "idIndividuo"), indexOptions);
		this.collection.createIndex(Indexes.ascending("fechaTendencia"));
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
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

	@Override
	public List<Tendencia> consultarTendenciasActualizar() throws GeneticDAOException {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

	@Override
	public Date nextFechaBase(Date fecha) throws GeneticDAOException {
		throw new UnsupportedOperationException("UnsupportedOperationException");
	}

	@Override
	public Date maxFechaBaseTendencia() throws GeneticDAOException {
		throw new UnsupportedOperationException("UnsupportedOperationException");
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

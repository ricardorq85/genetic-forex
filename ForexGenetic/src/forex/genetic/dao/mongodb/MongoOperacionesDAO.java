package forex.genetic.dao.mongodb;

import java.util.Date;
import java.util.List;

import com.mongodb.client.model.IndexOptions;

import forex.genetic.dao.IOperacionesDAO;
import forex.genetic.entities.DateInterval;
import forex.genetic.entities.Estadistica;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.ParametroOperacionPeriodo;
import forex.genetic.exception.GeneticDAOException;

/**
 *
 * @author ricardorq85
 */
public class MongoOperacionesDAO extends MongoGeneticDAO<Order> implements IOperacionesDAO {

	public MongoOperacionesDAO() {
		this(true);
	}

	public MongoOperacionesDAO(boolean configure) {
		super("operacion", configure);
	}

	public void configureCollection() {
		IndexOptions indexOptions = new IndexOptions();
		indexOptions.unique(true);

		//this.collection.createIndex(Indexes.ascending("moneda", "periodo", "fechaHistorico"), indexOptions);
		//this.collection.createIndex(Indexes.ascending("fechaHistorico"), indexOptions);
	}

	@Override
	public List<DateInterval> consultarVigencias(Date fechaPeriodo) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarOperacionesXPeriodo(Date fechaInicial, Date fechaFinal) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double consultarPipsXAgrupacion(String agrupador) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Estadistica consultarEstadisticasIndividuo(Individuo individuo) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuoOperacionActiva(Date fechaBase) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuoOperacionActiva(Date fechaBase, int filas) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarIndividuoOperacionActiva(Date fechaBase, Date fechaFin, int filas)
			throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteOperaciones(String idIndividuo) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateOperacion(Individuo individuo, Order operacion, Date fechaApertura) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMaximosReprocesoOperacion(Individuo individuo, Order operacion) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertOperaciones(Individuo individuo, List<Order> operaciones) throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Individuo consultarOperacionesIndividuoRetroceso(Individuo ind, Date fechaMaximo)
			throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Individuo> consultarOperacionesIndividuoRetroceso(Date fechaMaximo) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int cleanOperacionesPeriodo() throws GeneticDAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertOperacionesPeriodo(ParametroOperacionPeriodo param) throws GeneticDAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void actualizarOperacionesPositivasYNegativas() throws GeneticDAOException {
		// TODO Auto-generated method stub
		
	}
}

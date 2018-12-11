package forex.genetic.util.jdbc;

import forex.genetic.dao.IDatoAdicionalTPODAO;
import forex.genetic.dao.IDatoHistoricoDAO;
import forex.genetic.dao.IEstadisticaDAO;
import forex.genetic.dao.IIndividuoDAO;
import forex.genetic.dao.IOperacionesDAO;
import forex.genetic.dao.IParametroDAO;
import forex.genetic.dao.IProcesoEjecucionDAO;
import forex.genetic.dao.ITendenciaDAO;
import forex.genetic.dao.ITendenciaParaOperarDAO;
import forex.genetic.entities.Estadistica;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Order;
import forex.genetic.exception.GeneticDAOException;

public abstract class DataClient<C, I extends IndividuoEstrategia, O extends Order, E extends Estadistica> {

	protected C client;

	protected IDatoHistoricoDAO daoDatoHistorico;
	protected ITendenciaDAO daoTendencia, daoTendenciaUltimosDatos;
	protected IParametroDAO daoParametro;
	protected IIndividuoDAO<I> daoIndividuo;
	protected IOperacionesDAO<O> daoOperacion;
	protected IProcesoEjecucionDAO daoProcesoEjecucion;
	protected IEstadisticaDAO<E> daoEstadistica;
	protected ITendenciaParaOperarDAO daoTendenciaParaOperar;
	protected IDatoAdicionalTPODAO daoDatoAdicionalTPO;

	public DataClient(C client) {
		this.client = client;
	}

	public C getClient() {
		return client;
	}

	public void setClient(C client) {
		this.client = client;
	}

	public abstract void close() throws GeneticDAOException;

	public abstract void commit() throws GeneticDAOException;

	public abstract IDatoHistoricoDAO getDaoDatoHistorico() throws GeneticDAOException;

	public abstract ITendenciaDAO getDaoTendencia() throws GeneticDAOException;
	public abstract ITendenciaDAO getDaoTendenciaUltimosDatos() throws GeneticDAOException;
	
	public abstract IParametroDAO getDaoParametro() throws GeneticDAOException;

	public abstract IIndividuoDAO<I> getDaoIndividuo() throws GeneticDAOException;

	public abstract IOperacionesDAO<O> getDaoOperaciones() throws GeneticDAOException;

	public abstract IProcesoEjecucionDAO getDaoProcesoEjecucion() throws GeneticDAOException;

	public abstract IEstadisticaDAO<E> getDaoEstadistica() throws GeneticDAOException;
	
	public abstract ITendenciaParaOperarDAO getDaoTendenciaParaOperar() throws GeneticDAOException;

	public abstract IDatoAdicionalTPODAO getDaoDatoAdicionalTPO() throws GeneticDAOException;
}

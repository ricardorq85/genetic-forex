package forex.genetic.util.jdbc;

import forex.genetic.dao.IDatoHistoricoDAO;
import forex.genetic.dao.IGeneticDAO;
import forex.genetic.dao.IIndividuoDAO;
import forex.genetic.dao.IOperacionesDAO;
import forex.genetic.dao.IParametroDAO;
import forex.genetic.dao.IProcesoEjecucionDAO;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Order;
import forex.genetic.entities.Tendencia;
import forex.genetic.exception.GeneticDAOException;

public abstract class DataClient<C, I extends IndividuoEstrategia, O extends Order> {

	protected C client;

	protected IDatoHistoricoDAO daoDatoHistorico;
	protected IGeneticDAO<Tendencia> daoTendencia;
	protected IParametroDAO daoParametro;
	protected IIndividuoDAO<I> daoIndividuo;
	protected IOperacionesDAO<O> daoOperacion;
	protected IProcesoEjecucionDAO daoProcesoEjecucion;

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

	public abstract IGeneticDAO<Tendencia> getDaoTendencia() throws GeneticDAOException;

	public abstract IParametroDAO getDaoParametro() throws GeneticDAOException;

	public abstract IIndividuoDAO<I> getDaoIndividuo() throws GeneticDAOException;

	public abstract IOperacionesDAO<O> getDaoOperaciones() throws GeneticDAOException;

	public abstract IProcesoEjecucionDAO getDaoProcesoEjecucion() throws GeneticDAOException;

}

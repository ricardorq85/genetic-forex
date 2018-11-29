package forex.genetic.util.jdbc;

import java.sql.Connection;

import forex.genetic.dao.IDatoHistoricoDAO;
import forex.genetic.dao.IGeneticDAO;
import forex.genetic.dao.IIndividuoDAO;
import forex.genetic.dao.IOperacionesDAO;
import forex.genetic.dao.IParametroDAO;
import forex.genetic.dao.oracle.OracleDatoHistoricoDAO;
import forex.genetic.dao.oracle.OracleIndividuoDAO;
import forex.genetic.dao.oracle.OracleOperacionesDAO;
import forex.genetic.dao.oracle.OracleParametroDAO;
import forex.genetic.dao.oracle.OracleTendenciaDAO;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Order;
import forex.genetic.entities.Tendencia;
import forex.genetic.exception.GeneticDAOException;

public class OracleDataClient extends DataClient<Connection, IndividuoEstrategia, Order> {

	public OracleDataClient(Connection client) {
		super(client);
	}

	@Override
	public IDatoHistoricoDAO getDaoDatoHistorico() throws GeneticDAOException {
		if (daoDatoHistorico == null) {
			daoDatoHistorico = new OracleDatoHistoricoDAO(client);
		}
		return daoDatoHistorico;
	}

	@Override
	public IGeneticDAO<Tendencia> getDaoTendencia() throws GeneticDAOException {
		if (daoTendencia == null) {
			daoTendencia = new OracleTendenciaDAO(client);
		}
		return daoTendencia;
	}

	@Override
	public IParametroDAO getDaoParametro() throws GeneticDAOException {
		if (daoParametro == null) {
			daoParametro = new OracleParametroDAO(client);
		}
		return daoParametro;
	}

	@Override
	public IIndividuoDAO<IndividuoEstrategia> getDaoIndividuo() throws GeneticDAOException {
		if (daoIndividuo == null) {
			daoIndividuo = new OracleIndividuoDAO(client);
		}
		return daoIndividuo;
	}

	@Override
	public IOperacionesDAO<Order> getDaoOperaciones() throws GeneticDAOException {
		if (daoOperacion == null) {
			daoOperacion = new OracleOperacionesDAO(client);
		}
		return daoOperacion;

	}
}

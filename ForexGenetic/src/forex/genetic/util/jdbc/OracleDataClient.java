package forex.genetic.util.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import forex.genetic.dao.IDatoAdicionalTPODAO;
import forex.genetic.dao.IDatoHistoricoDAO;
import forex.genetic.dao.IEstadisticaDAO;
import forex.genetic.dao.IIndividuoDAO;
import forex.genetic.dao.IOperacionesDAO;
import forex.genetic.dao.IParametroDAO;
import forex.genetic.dao.IProcesoEjecucionDAO;
import forex.genetic.dao.ITendenciaDAO;
import forex.genetic.dao.ITendenciaParaOperarDAO;
import forex.genetic.dao.TendenciaUltimosDatosDAO;
import forex.genetic.dao.oracle.OracleDatoAdicionalTPODAO;
import forex.genetic.dao.oracle.OracleDatoHistoricoDAO;
import forex.genetic.dao.oracle.OracleIndividuoDAO;
import forex.genetic.dao.oracle.OracleOperacionesTendenciaDAO;
import forex.genetic.dao.oracle.OracleParametroDAO;
import forex.genetic.dao.oracle.OracleProcesoEjecucionDAO;
import forex.genetic.dao.oracle.OracleTendenciaDAO;
import forex.genetic.dao.oracle.OracleTendenciaParaOperarDAO;
import forex.genetic.entities.Estadistica;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Order;
import forex.genetic.exception.GeneticDAOException;

public class OracleDataClient extends DataClient<Connection, IndividuoEstrategia, Order, Estadistica> {

	public OracleDataClient(Connection client) {
		super(client);
	}

	@Override
	public void commit() throws GeneticDAOException {
		try {
			client.commit();
		} catch (SQLException e) {
			throw new GeneticDAOException("Error en commit", e);
		}
	}

	@Override
	public void close() {
		try {
			client.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IDatoHistoricoDAO getDaoDatoHistorico() throws GeneticDAOException {
		if (daoDatoHistorico == null) {
			daoDatoHistorico = new OracleDatoHistoricoDAO(client);
		}
		return daoDatoHistorico;
	}

	@Override
	public ITendenciaDAO getDaoTendencia() throws GeneticDAOException {
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
			daoOperacion = new OracleOperacionesTendenciaDAO(client);
		}
		return daoOperacion;
	}

	@Override
	public IProcesoEjecucionDAO getDaoProcesoEjecucion() throws GeneticDAOException {
		if (daoProcesoEjecucion == null) {
			daoProcesoEjecucion = new OracleProcesoEjecucionDAO(client);
		}
		return daoProcesoEjecucion;
	}

	@Override
	public IEstadisticaDAO<Estadistica> getDaoEstadistica() throws GeneticDAOException {
		throw new UnsupportedOperationException(
				"Mongo no tiene Proceso Ejecucion, se maneja directamente en el Individuo");
	}

	@Override
	public ITendenciaDAO getDaoTendenciaUltimosDatos() throws GeneticDAOException {
		if (daoTendenciaUltimosDatos == null) {
			daoTendenciaUltimosDatos = new TendenciaUltimosDatosDAO(client);
		}
		return daoTendenciaUltimosDatos;
	}

	@Override
	public ITendenciaParaOperarDAO getDaoTendenciaParaOperar() throws GeneticDAOException {
		if (daoTendenciaParaOperar == null) {
			daoTendenciaParaOperar = new OracleTendenciaParaOperarDAO(client);
		}
		return daoTendenciaParaOperar;
	}

	@Override
	public IDatoAdicionalTPODAO getDaoDatoAdicionalTPO() throws GeneticDAOException {
		if (daoDatoAdicionalTPO == null) {
			daoDatoAdicionalTPO = new OracleDatoAdicionalTPODAO(client);
		}
		return daoDatoAdicionalTPO;
	}

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.borrado;

import static forex.genetic.util.LogUtil.logTime;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import forex.genetic.dao.EstrategiaDAO;
import forex.genetic.dao.IProcesoEjecucionDAO;
import forex.genetic.dao.oracle.OracleIndividuoDAO;
import forex.genetic.dao.oracle.OracleOperacionesDAO;
import forex.genetic.dao.oracle.OracleProcesoEjecucionDAO;
import forex.genetic.dao.oracle.OracleTendenciaDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public abstract class BorradoManager {

	protected String tipoProceso = "DEFAULT";
	protected Connection conn;
	protected DataClient dataClient;
	protected OracleIndividuoDAO individuoDAO;
	protected OracleOperacionesDAO operacionDAO;
	protected IProcesoEjecucionDAO procesoDAO;
	protected OracleTendenciaDAO tendenciaDAO;
	protected EstrategiaDAO estrategiaDAO;

	public BorradoManager(Connection conn, String tipoProceso) {
		this(conn, null, tipoProceso);
	}

	public BorradoManager(Connection conn, OracleIndividuoDAO individuoDAO, String tipoProceso) {
		this.tipoProceso = tipoProceso;
		this.conn = conn;
		this.individuoDAO = (individuoDAO == null) ? (new OracleIndividuoDAO(conn)) : individuoDAO;
		operacionDAO = new OracleOperacionesDAO(conn);
		procesoDAO = new OracleProcesoEjecucionDAO(conn);
		tendenciaDAO = new OracleTendenciaDAO(conn);
		estrategiaDAO = new EstrategiaDAO(conn);
	}

	public BorradoManager(DataClient dc, String tipoProceso2) {
		this(null, null, tipoProceso2);
		this.dataClient = dc;
	}

	protected abstract List<Individuo> consultarIndividuos(Individuo individuo) throws GeneticBusinessException;

	public void borrarIndividuos() throws GeneticBusinessException {
		procesarBorradoIndividuos(null);
	}

	public void validarYBorrarIndividuo(Individuo individuo) throws GeneticBusinessException {
		procesarBorradoIndividuos(individuo);
	}

	protected void procesarBorradoIndividuos(Individuo individuo) throws GeneticBusinessException {
		List<Individuo> individuos = this.consultarIndividuos(individuo);
		int count = 0;
		while ((individuos != null) && (!individuos.isEmpty())) {
			LogUtil.logTime("Individuos consultados para borrar: " + individuos.size(), 1);
			this.smartDelete(individuos);
			count += individuos.size();
			if (count > 0) {
				logTime("Individuos borrados= " + count + ". Razon: " + this.getClass().getName(), 1);
			}
			individuos = this.consultarIndividuos(individuo);
		}
	}

	protected void smartDelete(List<Individuo> individuos) throws GeneticBusinessException {
		try {
			for (Individuo individuo : individuos) {
				int r_proceso = procesoDAO.deleteProceso(individuo.getId());
				logTime("->Individuo: " + individuo.getId() + ". Borrados PROCESO = " + r_proceso, 1);
				int r_operaciones = operacionDAO.deleteByIndividuo(individuo);
				logTime("Individuo: " + individuo.getId() + ". Borrados OPERACIONES = " + r_operaciones, 1);
				int r_tendencia = tendenciaDAO.deleteByIndividuo(individuo);
				logTime("Individuo: " + individuo.getId() + ". Borrados TENDENCIA = " + r_tendencia, 1);
				int r_indEst = estrategiaDAO.deleteIndividuoEstrategia(individuo.getId());
				logTime("Individuo: " + individuo.getId() + ". Borrados INDIVIDUOESTRATEGIA = " + r_indEst, 1);
				individuoDAO.smartDelete(individuo.getId(), tipoProceso, individuo.getIdParent1());
				individuoDAO.commit();
			}
		} catch (SQLException | GeneticDAOException e) {
			throw new GeneticBusinessException("smartDelete", e);
		}
	}
}

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

	public BorradoManager(Connection conn, String tipoProceso) throws ClassNotFoundException {
		this(conn, null, tipoProceso);
	}

	public BorradoManager(Connection conn, OracleIndividuoDAO individuoDAO, String tipoProceso)
			throws ClassNotFoundException {
		this.tipoProceso = tipoProceso;
		this.conn = conn;
		this.individuoDAO = (individuoDAO == null) ? (new OracleIndividuoDAO(conn)) : individuoDAO;
		operacionDAO = new OracleOperacionesDAO(conn);
		procesoDAO = new OracleProcesoEjecucionDAO(conn);
		tendenciaDAO = new OracleTendenciaDAO(conn);
		estrategiaDAO = new EstrategiaDAO(conn);
	}

	public BorradoManager(DataClient dc, String tipoProceso2) throws ClassNotFoundException {
		this(null, null, tipoProceso2);
		this.dataClient = dc;
	}

	protected abstract List<Individuo> consultarIndividuos(Individuo individuo)
			throws ClassNotFoundException, GeneticDAOException;

	public void borrarIndividuos() throws ClassNotFoundException, GeneticDAOException {
		procesarBorradoIndividuos(null);
	}

	public void validarYBorrarIndividuo(Individuo individuo) throws ClassNotFoundException, GeneticDAOException {
		procesarBorradoIndividuos(individuo);
	}

	protected void procesarBorradoIndividuos(Individuo individuo) throws ClassNotFoundException, GeneticDAOException {
		try {
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
		} finally {
		}
	}

	protected void smartDelete(List<Individuo> individuos) throws GeneticDAOException {
		for (Individuo individuo : individuos) {
			int r_proceso = procesoDAO.deleteProceso(individuo.getId());
			logTime("->Individuo: " + individuo.getId() + ". Borrados PROCESO = " + r_proceso, 1);
			int r_operaciones = operacionDAO.deleteOperaciones(individuo.getId());
			logTime("Individuo: " + individuo.getId() + ". Borrados OPERACIONES = " + r_operaciones, 1);
			int r_tendencia = tendenciaDAO.deleteTendencia(individuo.getId());
			logTime("Individuo: " + individuo.getId() + ". Borrados TENDENCIA = " + r_tendencia, 1);
			try {
				int r_indEst = estrategiaDAO.deleteIndividuoEstrategia(individuo.getId());
				logTime("Individuo: " + individuo.getId() + ". Borrados INDIVIDUOESTRATEGIA = " + r_indEst, 1);
			} catch (SQLException e) {
			}
			individuoDAO.smartDelete(individuo.getId(), tipoProceso, individuo.getIdParent1());
			individuoDAO.commit();
		}
	}

}

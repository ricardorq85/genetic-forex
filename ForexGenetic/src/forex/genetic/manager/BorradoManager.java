/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import static forex.genetic.util.LogUtil.logTime;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import forex.genetic.dao.EstrategiaDAO;
import forex.genetic.dao.IndividuoDAO;
import forex.genetic.dao.OperacionesDAO;
import forex.genetic.dao.ProcesoPoblacionDAO;
import forex.genetic.dao.TendenciaDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public abstract class BorradoManager {

	protected String tipoProceso = "DEFAULT";
	protected Connection conn = null;
	protected IndividuoDAO individuoDAO;
	protected OperacionesDAO operacionDAO;
	protected ProcesoPoblacionDAO procesoDAO;
	protected TendenciaDAO tendenciaDAO;
	protected EstrategiaDAO estrategiaDAO;

	public BorradoManager() throws ClassNotFoundException, SQLException {
		conn = JDBCUtil.getConnection();
		individuoDAO = new IndividuoDAO(conn);
		operacionDAO = new OperacionesDAO(conn);
		procesoDAO = new ProcesoPoblacionDAO(conn);
		tendenciaDAO = new TendenciaDAO(conn);
		estrategiaDAO = new EstrategiaDAO(conn);
	}

	public void finish() {
		JDBCUtil.close(conn);
	}

	protected abstract List<Individuo> consultarIndividuos(Individuo individuo)
			throws ClassNotFoundException, SQLException;

	public abstract void borrarIndividuos() throws ClassNotFoundException, SQLException;

	public abstract void validarYBorrarIndividuo(Individuo individuo) throws ClassNotFoundException, SQLException;

	protected void procesarBorradoIndividuos(Individuo individuo) throws ClassNotFoundException, SQLException {
		try {
			List<Individuo> individuos = this.consultarIndividuos(individuo);
			LogUtil.logTime("Individuos consultados: " + individuos.size(), 1);
			int count = 0;
			while ((individuos != null) && (!individuos.isEmpty())) {
				LogUtil.logTime("Individuos consultados: " + individuos.size(), 1);
				this.smartDelete(individuos);
				conn.commit();
				count += individuos.size();
				if (count > 0) {
					logTime("Individuos borrados= " + count, 1);
				}
				individuos = this.consultarIndividuos(individuo);
			}
		} finally {
			JDBCUtil.close(conn);
		}
	}

	protected void smartDelete(List<Individuo> individuos) throws SQLException {
		for (Individuo individuo : individuos) {
			int r_proceso = procesoDAO.deleteProceso(individuo.getId());
			logTime("Individuo: " + individuo.getId() + ". Borrados PROCESO = " + r_proceso, 1);
			int r_operaciones = operacionDAO.deleteOperaciones(individuo.getId());
			logTime("Individuo: " + individuo.getId() + ". Borrados OPERACIONES = " + r_operaciones, 1);
			int r_tendencia = tendenciaDAO.deleteTendencia(individuo.getId());
			logTime("Individuo: " + individuo.getId() + ". Borrados TENDENCIA = " + r_tendencia, 1);
			try {
				int r_indEst = estrategiaDAO.deleteIndividuoEstrategia(individuo.getId());
				logTime("Borrados INDIVIDUOESTRATEGIA = " + r_indEst, 1);
			} catch (SQLException e) {
			}
			individuoDAO.smartDelete(individuo.getId(), tipoProceso, individuo.getIdParent1());
		}
	}

}

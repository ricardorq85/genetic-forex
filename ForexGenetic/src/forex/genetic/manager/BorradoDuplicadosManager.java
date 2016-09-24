/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
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
public class BorradoDuplicadosManager extends BorradoManager {

	private Connection conn = null;

	/**
	 *
	 * @param tipoProceso
	 * @throws ClassNotFoundException
	 */
	private void borrarDuplicados(String tipoProceso) throws ClassNotFoundException, SQLException {
		try {
			conn = JDBCUtil.getConnection();

			IndividuoDAO individuoDAO = new IndividuoDAO(conn);
			OperacionesDAO operacionDAO = new OperacionesDAO(conn);
			ProcesoPoblacionDAO procesoDAO = new ProcesoPoblacionDAO(conn);
			TendenciaDAO tendenciaDAO = new TendenciaDAO(conn);
			EstrategiaDAO estrategiaDAO = new EstrategiaDAO(conn);

			List<Individuo> individuosPadres = individuoDAO.consultarIndividuosPadreRepetidos(tipoProceso);
			LogUtil.logTime("Individuos padres consultados: " + individuosPadres.size(), 1);
			int count = 0;
			while ((individuosPadres != null) && (!individuosPadres.isEmpty())) {
				for (int i = 0; i < individuosPadres.size(); i++) {
					Individuo individuoPadre = individuosPadres.get(i);
					LogUtil.logTime("Individuo Padre: " + individuoPadre.getId(), 1);
					List<Individuo> individuosRepetidos = individuoDAO.consultarIndividuosRepetidos(individuoPadre);
					LogUtil.logTime("Individuos repetidos consultados: " + individuosRepetidos.size(), 1);
					for (int j = 0; j < individuosRepetidos.size(); j++) {
						Individuo individuo = individuosRepetidos.get(j);
						LogUtil.logTime("Individuo: " + individuo.getId(), 1);
						procesoDAO.deleteProceso(individuo.getId());
						operacionDAO.deleteOperaciones(individuo.getId());
						tendenciaDAO.deleteTendencia(individuo.getId());
						try {
							estrategiaDAO.deleteIndividuoEstrategia(individuo.getId());
						} catch (SQLSyntaxErrorException ex) {
						}
						individuoDAO.smartDelete(individuo.getId(), tipoProceso, individuoPadre.getId());
					}
					if ((individuosRepetidos == null) || (individuosRepetidos.isEmpty())) {
						procesoDAO.insertProcesoRepetidos(individuoPadre.getId(), tipoProceso);
					}
					conn.commit();
					count += individuosRepetidos.size();
					LogUtil.logTime("Individuos borrados: " + count, 1);
				}
				individuosPadres = individuoDAO.consultarIndividuosPadreRepetidos(tipoProceso);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			JDBCUtil.close(conn);
		}
	}

	@Override
	public void borrarIndividuos() throws ClassNotFoundException, SQLException {
		String tipoProceso = "DUPLICADO_OPERACIONES";
		this.borrarDuplicados(tipoProceso);
	}

	@Override
	protected List<Individuo> consultarIndividuos() throws ClassNotFoundException, SQLException {
		return null;
	}

}

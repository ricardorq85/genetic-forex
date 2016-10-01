/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import java.sql.SQLException;
import java.util.List;

import forex.genetic.entities.Individuo;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class BorradoDuplicadosManager extends BorradoManager {

	public BorradoDuplicadosManager() throws ClassNotFoundException, SQLException {
		super.tipoProceso = "DUPLICADO_OPERACIONES";
	}

	/**
	 *
	 * @param tipoProceso
	 * @throws ClassNotFoundException
	 */
	private void borrarDuplicados() throws ClassNotFoundException, SQLException {
		try {
			List<Individuo> individuosPadres = individuoDAO.consultarIndividuosPadreRepetidos(tipoProceso);
			LogUtil.logTime("Individuos padres consultados: " + individuosPadres.size(), 1);
			int count = 0;
			while ((individuosPadres != null) && (!individuosPadres.isEmpty())) {
				for (int i = 0; i < individuosPadres.size(); i++) {
					Individuo individuoPadre = individuosPadres.get(i);
					LogUtil.logTime("Individuo Padre: " + individuoPadre.getId(), 1);
					List<Individuo> individuosRepetidos = individuoDAO.consultarIndividuosRepetidos(individuoPadre);
					LogUtil.logTime("Individuos repetidos consultados: " + individuosRepetidos.size(), 1);
					super.smartDelete(individuosRepetidos);
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
		this.borrarDuplicados();
	}

	@Override
	protected List<Individuo> consultarIndividuos(Individuo individuo) throws ClassNotFoundException, SQLException {
		return null;
	}

	@Override
	public void validarYBorrarIndividuo(Individuo individuo) throws ClassNotFoundException, SQLException {
		this.borrarDuplicados(individuo);
	}

	private void borrarDuplicados(Individuo individuo) throws ClassNotFoundException, SQLException {
		try {
			int count = 0;
			List<Individuo> individuosRepetidos = individuoDAO.consultarIndividuoHijoRepetido(individuo);
			super.smartDelete(individuosRepetidos);
			conn.commit();
			count += individuosRepetidos.size();
			LogUtil.logTime("Individuos borrados: " + count, 1);
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			JDBCUtil.close(conn);
		}
	}

}

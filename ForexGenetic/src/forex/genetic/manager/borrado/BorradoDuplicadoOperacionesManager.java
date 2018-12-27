/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.borrado;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import forex.genetic.entities.Individuo;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class BorradoDuplicadoOperacionesManager extends BorradoDuplicadoIndividuoManager {

	public BorradoDuplicadoOperacionesManager(Connection conn) {
		super(conn, null, "DUPLICADO_OPERACIONES");
	}

	/**
	 *
	 * @param tipoProceso
	 * @throws GeneticBusinessException 
	 * @throws ClassNotFoundException
	 * @throws GeneticDAOException 
	 */
	protected void borrarDuplicados() throws GeneticBusinessException {
		try {
			List<Individuo> individuosPadres = individuoDAO.consultarIndividuosPadreRepetidos(tipoProceso);
			LogUtil.logTime("Individuos padres consultados: " + individuosPadres.size(), 1);
			int count = 0;
			while ((individuosPadres != null) && (!individuosPadres.isEmpty())) {
				for (int i = 0; i < individuosPadres.size(); i++) {
					Individuo individuoPadre = individuosPadres.get(i);
					LogUtil.logTime("Individuo Padre: " + individuoPadre.getId(), 1);
					List<Individuo> individuosRepetidos = individuoDAO.consultarIndividuosRepetidosOperaciones(individuoPadre);
					deleteRepetidos(individuosRepetidos);
					if ((individuosRepetidos == null) || (individuosRepetidos.isEmpty())) {
						procesoDAO.insertProcesoRepetidos(individuoPadre.getId(), tipoProceso);
					}
					conn.commit();
					count += individuosRepetidos.size();
					LogUtil.logTime("Individuos borrados: " + count, 1);
				}
				individuosPadres = individuoDAO.consultarIndividuosPadreRepetidos(tipoProceso);
			}
		} catch (SQLException | GeneticDAOException e) {
			throw new GeneticBusinessException(e);
		} finally {
		}
	}	

	protected void borrarDuplicados(Individuo individuo) throws GeneticBusinessException {
		try {
			int count = 0;
			List<Individuo> individuosRepetidos = individuoDAO.consultarIndividuoHijoRepetidoOperaciones(individuo);
			deleteRepetidos(individuosRepetidos);
			count += individuosRepetidos.size();
			LogUtil.logTime("Individuos borrados: " + count, 1);
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(e);
		} finally {
		}
	}

}

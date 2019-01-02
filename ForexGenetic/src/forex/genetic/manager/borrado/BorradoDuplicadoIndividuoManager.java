/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.borrado;

import java.sql.Connection;
import java.util.List;

import forex.genetic.dao.oracle.OracleIndividuoDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class BorradoDuplicadoIndividuoManager extends BorradoManager {

	public BorradoDuplicadoIndividuoManager(Connection conn) {
		super(conn, "DUPLICADO_INDIVIDUO");
	}

	public BorradoDuplicadoIndividuoManager(Connection conn, OracleIndividuoDAO individuoDAO, String tipoProceso) {
		super(conn, individuoDAO, tipoProceso);
	}

	@Override
	public void borrarIndividuos() throws GeneticBusinessException {
		borrarDuplicados();
	}

	@Override
	protected List<Individuo> consultarIndividuos(Individuo individuo) {
		return null;
	}

	@Override
	public boolean validarYBorrarIndividuo(Individuo individuo) throws GeneticBusinessException {
		return borrarDuplicados(individuo);
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
			int count = 0;
			List<Individuo> individuosRepetidos = individuoDAO.consultarIndividuosRepetidos();
			while ((individuosRepetidos != null) && (!individuosRepetidos.isEmpty())) {
				deleteRepetidos(individuosRepetidos);
				count += individuosRepetidos.size();
				individuosRepetidos = individuoDAO.consultarIndividuosRepetidos();
			}
			LogUtil.logTime("Individuos borrados: " + count, 1);
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(e);
		} finally {
		}
	}

	protected void deleteRepetidos(List<Individuo> individuosRepetidos) throws GeneticBusinessException {
		if (individuosRepetidos.size() > 0) {
			LogUtil.logTime("Individuos repetidos consultados: " + individuosRepetidos.size(), 1);
		}
		super.smartDelete(individuosRepetidos);
		try {
			this.individuoDAO.commit();
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(e);
		}
	}

	protected boolean borrarDuplicados(Individuo individuo) throws GeneticBusinessException {
		int count = 0;
		List<Individuo> individuosRepetidos;
		try {
			individuosRepetidos = individuoDAO.consultarIndividuoHijoRepetido(individuo);
			deleteRepetidos(individuosRepetidos);
			count += individuosRepetidos.size();
			if (count > 0) {
				LogUtil.logTime("Individuos borrados: " + count, 1);
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(e);
		}
		return false;
	}
}

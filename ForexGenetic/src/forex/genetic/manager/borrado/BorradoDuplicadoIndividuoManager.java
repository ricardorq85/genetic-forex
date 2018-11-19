/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.borrado;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import forex.genetic.dao.oracle.OracleIndividuoDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class BorradoDuplicadoIndividuoManager extends BorradoManager {

	public BorradoDuplicadoIndividuoManager(Connection conn) throws ClassNotFoundException, SQLException {
		super(conn, "DUPLICADO_INDIVIDUO");
	}
	
	public BorradoDuplicadoIndividuoManager(Connection conn, OracleIndividuoDAO individuoDAO, String tipoProceso)
			throws ClassNotFoundException, SQLException {
		super(conn, individuoDAO, tipoProceso);
	}

	@Override
	public void borrarIndividuos() throws ClassNotFoundException, GeneticDAOException {
		borrarDuplicados();
	}

	@Override
	protected List<Individuo> consultarIndividuos(Individuo individuo) throws ClassNotFoundException {
		return null;
	}

	@Override
	public void validarYBorrarIndividuo(Individuo individuo) throws ClassNotFoundException, GeneticDAOException {
		borrarDuplicados(individuo);
	}

	/**
	 *
	 * @param tipoProceso
	 * @throws ClassNotFoundException
	 * @throws GeneticDAOException 
	 */
	protected void borrarDuplicados() throws ClassNotFoundException, GeneticDAOException {
		try {
			int count = 0;
			List<Individuo> individuosRepetidos = individuoDAO.consultarIndividuosRepetidos();
			while ((individuosRepetidos != null) && (!individuosRepetidos.isEmpty())) {
				deleteRepetidos(individuosRepetidos);
				count += individuosRepetidos.size();
				individuosRepetidos = individuoDAO.consultarIndividuosRepetidos();
			}
			LogUtil.logTime("Individuos borrados: " + count, 1);
		} finally {
		}
	}

	protected void deleteRepetidos(List<Individuo> individuosRepetidos) throws GeneticDAOException {
		if (individuosRepetidos.size() > 0) {
			LogUtil.logTime("Individuos repetidos consultados: " + individuosRepetidos.size(), 1);
		}
		super.smartDelete(individuosRepetidos);
		this.individuoDAO.commit();
	}

	protected void borrarDuplicados(Individuo individuo) throws ClassNotFoundException, GeneticDAOException {
		try {
			int count = 0;
			List<Individuo> individuosRepetidos = individuoDAO.consultarIndividuoHijoRepetido(individuo);
			deleteRepetidos(individuosRepetidos);
			count += individuosRepetidos.size();
			if (count > 0) {
				LogUtil.logTime("Individuos borrados: " + count, 1);
			}
		} finally {
		}
	}

}

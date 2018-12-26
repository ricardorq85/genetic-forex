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
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public class MongoBorradoDuplicadoIndividuoManager extends BorradoManager {

	public MongoBorradoDuplicadoIndividuoManager(DataClient dc) throws ClassNotFoundException, SQLException {
		super(dc, "DUPLICADO_INDIVIDUO");
	}
	
	public MongoBorradoDuplicadoIndividuoManager(Connection conn, OracleIndividuoDAO individuoDAO, String tipoProceso)
			throws ClassNotFoundException, SQLException {
		super(conn, individuoDAO, tipoProceso);
	}

	@Override
	protected List<Individuo> consultarIndividuos(Individuo individuo) throws ClassNotFoundException {
		return null;
	}

	@Override
	public void validarYBorrarIndividuo(Individuo individuo) throws ClassNotFoundException, GeneticDAOException {
		borrarDuplicados(individuo);
	}

	protected void deleteRepetidos(List<Individuo> individuosRepetidos) throws GeneticDAOException {
		if (individuosRepetidos.size() > 0) {
			LogUtil.logTime("Individuos repetidos consultados: " + individuosRepetidos.size(), 1);
		}
		//TODO borrar individuos
		//super.smartDelete(individuosRepetidos);
		this.dataClient.commit();
	}

	protected void borrarDuplicados(Individuo individuo) throws ClassNotFoundException, GeneticDAOException {
		try {
			int count = 0;
			List<Individuo> individuosRepetidos = dataClient.getDaoIndividuo().consultarIndividuoHijoRepetido(individuo);
			deleteRepetidos(individuosRepetidos);
			count += individuosRepetidos.size();
			if (count > 0) {
				LogUtil.logTime("Individuos borrados: " + count, 1);
			}
		} finally {
		}
	}

}

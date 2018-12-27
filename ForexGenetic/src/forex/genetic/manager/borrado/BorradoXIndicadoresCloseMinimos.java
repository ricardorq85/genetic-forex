/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.borrado;

import java.sql.Connection;
import java.util.List;

import forex.genetic.dao.oracle.OracleIndividuoDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;

/**
 *
 * @author ricardorq85
 */
public class BorradoXIndicadoresCloseMinimos extends BorradoManager {

	public BorradoXIndicadoresCloseMinimos(Connection conn) {
		super(conn, "IND_CLOSE_MINIMO");
	}

	@Override
	public List<Individuo> consultarIndividuos(Individuo individuo) throws GeneticBusinessException {
		List<Individuo> individuos;
		try {
			OracleIndividuoDAO individuoDAO = new OracleIndividuoDAO(conn);
			if (individuo == null) {
				individuos = individuoDAO.consultarIndividuosIndicadoresCloseMinimos(2);
			} else {
				individuos = individuoDAO.consultarIndividuosIndicadoresCloseMinimos(2, individuo.getId());
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(e);
		}
		return individuos;
	}

}

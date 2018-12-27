/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.borrado;

import java.sql.Connection;
import java.util.List;

import forex.genetic.entities.Individuo;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;

/**
 *
 * @author ricardorq85
 */
public class BorradoInconsistentesStopLossManager extends BorradoManager {

	public BorradoInconsistentesStopLossManager(Connection conn) {
		super(conn, "STOP_LOSS_MINIMO");
	}

	@Override
	public List<Individuo> consultarIndividuos(Individuo individuo) throws GeneticBusinessException {
		List<Individuo> individuos;
		try {
			if (individuo == null) {
				individuos = individuoDAO.consultarIndividuosStopLossInconsistente(200);
			} else {
				individuos = individuoDAO.consultarIndividuosStopLossInconsistente(200, individuo.getId());
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(e);
		}
		return individuos;
	}

}

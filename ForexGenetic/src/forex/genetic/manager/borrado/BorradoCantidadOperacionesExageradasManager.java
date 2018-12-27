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
public class BorradoCantidadOperacionesExageradasManager extends BorradoManager {

	public BorradoCantidadOperacionesExageradasManager(Connection conn) {
		super(conn, "CANTIDAD_LIMITE");
	}

	@Override
	public List<Individuo> consultarIndividuos(Individuo individuo) throws GeneticBusinessException {
		List<Individuo> individuos;
		try {
			double cantidadLimite = 0.010;
			if (individuo == null) {
				individuos = individuoDAO.consultarIndividuosCantidadLimite(cantidadLimite);
			} else {
				individuos = individuoDAO.consultarIndividuosCantidadLimite(cantidadLimite, individuo.getId());
			}
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(e);
		}
		return individuos;
	}

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.dao.IndividuoDAO;
import forex.genetic.entities.Individuo;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class BorradoCantidadOperacionesExageradasManager extends BorradoManager {

	public BorradoCantidadOperacionesExageradasManager() throws ClassNotFoundException, SQLException {
		super.tipoProceso = "CANTIDAD_LIMITE";
	}

	@Override
	public List<Individuo> consultarIndividuos(Individuo individuo) throws ClassNotFoundException, SQLException {
		List<Individuo> individuos;
		IndividuoDAO individuoDAO = new IndividuoDAO(conn);
		double cantidadLimite = 0.010;
		if (individuo == null) {
			individuos = individuoDAO.consultarIndividuosCantidadLimite(cantidadLimite);
		} else {
			individuos = individuoDAO.consultarIndividuosCantidadLimite(cantidadLimite, individuo.getId());
		}
		return individuos;
	}

	@Override
	public void borrarIndividuos() throws ClassNotFoundException, SQLException {
		super.procesarBorradoIndividuos(null);
	}

	@Override
	public void validarYBorrarIndividuo(Individuo individuo) throws ClassNotFoundException, SQLException {
		super.procesarBorradoIndividuos(individuo);
	}
}

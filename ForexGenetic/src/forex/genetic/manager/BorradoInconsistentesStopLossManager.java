/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import java.sql.SQLException;
import java.util.List;

import forex.genetic.dao.IndividuoDAO;
import forex.genetic.entities.Individuo;

/**
 *
 * @author ricardorq85
 */
public class BorradoInconsistentesStopLossManager extends BorradoManager {

	public BorradoInconsistentesStopLossManager() throws ClassNotFoundException, SQLException {
		super.tipoProceso = "STOP_LOSS_MINIMO";
	}

	@Override
	public List<Individuo> consultarIndividuos(Individuo individuo) throws ClassNotFoundException, SQLException {
		List<Individuo> individuos;
		IndividuoDAO individuoDAO = new IndividuoDAO(conn);
		if (individuo == null) {
			individuos = individuoDAO.consultarIndividuosStopLossInconsistente(200);
		} else {
			individuos = individuoDAO.consultarIndividuosStopLossInconsistente(200, individuo.getId());
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

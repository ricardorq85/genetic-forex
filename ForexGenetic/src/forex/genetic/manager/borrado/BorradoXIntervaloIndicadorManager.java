/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.borrado;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import forex.genetic.dao.IndividuoDAO;
import forex.genetic.entities.Individuo;

/**
 *
 * @author ricardorq85
 */
public class BorradoXIntervaloIndicadorManager extends BorradoManager {

	public BorradoXIntervaloIndicadorManager(Connection conn) throws ClassNotFoundException, SQLException {
		super(conn, "INTERVALO_INDICADOR");
	}

	@Override
	public List<Individuo> consultarIndividuos(Individuo individuo) throws ClassNotFoundException, SQLException {
		List<Individuo> individuos;
		if (individuo == null) {
			individuos = individuoDAO.consultarIndividuosIntervaloIndicadores();
		} else {
			individuos = individuoDAO.consultarIndividuosIntervaloIndicadores(individuo.getId());
		}
		return individuos;
	}

}

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
public class BorradoXIndicadoresCloseMinimos extends BorradoManager {

	public BorradoXIndicadoresCloseMinimos(Connection conn) throws ClassNotFoundException, SQLException {
		super(conn, "IND_CLOSE_MINIMO");
	}

	@Override
	public List<Individuo> consultarIndividuos(Individuo individuo) throws ClassNotFoundException, SQLException {
		List<Individuo> individuos;
		IndividuoDAO individuoDAO = new IndividuoDAO(conn);
		if (individuo == null) {
			individuos = individuoDAO.consultarIndividuosIndicadoresCloseMinimos(2);
		} else {
			individuos = individuoDAO.consultarIndividuosIndicadoresCloseMinimos(2, individuo.getId());
		}
		return individuos;
	}

}

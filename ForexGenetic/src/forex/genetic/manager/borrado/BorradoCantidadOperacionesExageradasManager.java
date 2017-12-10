/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.borrado;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import forex.genetic.entities.Individuo;

/**
 *
 * @author ricardorq85
 */
public class BorradoCantidadOperacionesExageradasManager extends BorradoManager {

	public BorradoCantidadOperacionesExageradasManager(Connection conn) throws ClassNotFoundException, SQLException {
		super(conn, "CANTIDAD_LIMITE");
	}

	@Override
	public List<Individuo> consultarIndividuos(Individuo individuo) throws ClassNotFoundException, SQLException {
		List<Individuo> individuos;
		double cantidadLimite = 0.010;
		if (individuo == null) {
			individuos = individuoDAO.consultarIndividuosCantidadLimite(cantidadLimite);
		} else {
			individuos = individuoDAO.consultarIndividuosCantidadLimite(cantidadLimite, individuo.getId());
		}
		return individuos;
	}

}

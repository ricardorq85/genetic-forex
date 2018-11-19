/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.borrado;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import forex.genetic.dao.DuracionIndividuoDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.exception.GeneticDAOException;

/**
 *
 * @author ricardorq85
 */
public class BorradoXDuracionPromedioManager extends BorradoManager {

	public BorradoXDuracionPromedioManager(Connection conn) throws ClassNotFoundException, SQLException {
		super(conn, new DuracionIndividuoDAO(conn), "DURACION_PROMEDIO_MINIMA");
	}

	@Override
	public List<Individuo> consultarIndividuos(Individuo individuo) throws ClassNotFoundException, GeneticDAOException {
		List<Individuo> individuos;
		if (individuo == null) {
			individuos = individuoDAO.consultarIndividuosParaBorrar(5);
		} else {
			individuos = individuoDAO.consultarIndividuosParaBorrar(individuo.getId(), 5);
		}
		return individuos;
	}

}

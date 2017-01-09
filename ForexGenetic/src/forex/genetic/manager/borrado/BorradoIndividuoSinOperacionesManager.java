/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.borrado;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.IndividuoSinOperacionesDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.util.DateUtil;

/**
 *
 * @author ricardorq85
 */
public class BorradoIndividuoSinOperacionesManager extends BorradoManager {

	public BorradoIndividuoSinOperacionesManager(Connection conn) throws ClassNotFoundException, SQLException {
		super(conn, new IndividuoSinOperacionesDAO(conn), "SIN_OPERACIONES");
	}

	@Override
	public List<Individuo> consultarIndividuos(Individuo individuo) throws ClassNotFoundException, SQLException {
		List<Individuo> individuos;
		Date fechaLimite;
		try {
			fechaLimite = DateUtil.obtenerFecha("2014/01/01 00:00");
		} catch (ParseException e) {
			fechaLimite = new Date();
			e.printStackTrace();
		}
		if (individuo == null) {
			individuos = individuoDAO.consultarIndividuosParaBorrar(fechaLimite);
		} else {
			individuos = individuoDAO.consultarIndividuosParaBorrar(individuo.getId(), fechaLimite);
		}
		return individuos;
	}

}

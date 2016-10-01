/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.IndividuoDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.util.DateUtil;

/**
 *
 * @author ricardorq85
 */
public class BorradoIndividuoSinOperacionesManager extends BorradoManager {

	public BorradoIndividuoSinOperacionesManager() throws ClassNotFoundException, SQLException {
		super.tipoProceso = "SIN_OPERACIONES";
	}

	@Override
	public List<Individuo> consultarIndividuos(Individuo individuo) throws ClassNotFoundException, SQLException {
		List<Individuo> individuos;
		IndividuoDAO individuoDAO = new IndividuoDAO(conn);
		Date fechaLimite;
		try {
			fechaLimite = DateUtil.obtenerFecha("2015/01/01 00:00");
		} catch (ParseException e) {
			fechaLimite = new Date();
			e.printStackTrace();
		}
		if (individuo == null) {
			individuos = individuoDAO.consultarIndividuosYaProcesadosSinOperaciones(fechaLimite);
		} else {
			individuos = individuoDAO.consultarIndividuosYaProcesadosSinOperaciones(fechaLimite, individuo.getId());
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

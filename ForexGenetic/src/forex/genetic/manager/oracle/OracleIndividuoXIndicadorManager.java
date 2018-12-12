/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.oracle;

import java.sql.SQLException;
import java.util.Date;

import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.IndividuoXIndicadorManager;

/**
 *
 * @author ricardorq85
 */
public class OracleIndividuoXIndicadorManager extends IndividuoXIndicadorManager {

	public OracleIndividuoXIndicadorManager() throws ClassNotFoundException, SQLException, GeneticDAOException {
		super();
	}

	public OracleIndividuoXIndicadorManager(Date fechaMinima, Date fechaMaxima, int maximoMeses)
			throws ClassNotFoundException, SQLException, GeneticDAOException {
		super(fechaMinima, fechaMaxima, maximoMeses);
	}

}

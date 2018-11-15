/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import static forex.genetic.manager.PropertiesManager.load;
import static forex.genetic.util.LogUtil.logTime;
import static java.lang.System.currentTimeMillis;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import forex.genetic.exception.GeneticDAOException;
import forex.genetic.proxy.ProcesosAlternosProxy;

/**
 *
 * @author ricardorq85
 */
public class ForexProcesosAlternos {

	/**
	 * @param args
	 *            the command line arguments
	 * @throws java.lang.InterruptedException
	 */
	public static void main(String[] args)
			throws IOException, ClassNotFoundException, InterruptedException, ParseException {
		long id = currentTimeMillis();
		load().join();
		logTime("ProcesosAlternos: " + id, 1);
		try {
			ProcesosAlternosProxy alternosManager = new ProcesosAlternosProxy(id);
			alternosManager.procesar();
		} catch (SQLException | GeneticDAOException e) {
			e.printStackTrace();
		}
		logTime("Fin Procesos alternos", 1);
	}
}

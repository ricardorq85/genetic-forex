/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

import static forex.genetic.delegate.GeneticDelegate.setId;
import static forex.genetic.manager.PropertiesManager.getPropertyString;
import static forex.genetic.manager.PropertiesManager.load;
import static forex.genetic.util.Constants.LOG_PATH;
import static forex.genetic.util.LogUtil.logTime;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.setErr;
import static java.lang.System.setOut;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.sql.SQLException;

import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.oracle.OracleIndividuoXIndicadorManager;
import forex.genetic.proxy.ProcesosAlternosProxy;

/**
 *
 * @author ricardorq85
 */
public class ForexCrearIndividuosXIndicador {

	/**
	 * @param args
	 *            the command line arguments
	 * @throws java.io.IOException
	 * @throws java.sql.SQLException
	 * @throws GeneticDAOException 
	 */
	public static void main(String[] args)
			throws IOException, ClassNotFoundException, InterruptedException, SQLException, GeneticDAOException {
		long id = currentTimeMillis();
		load().join();
		logTime("ForexCrearIndividuosXIndicador.java: " + id, 1);
		String name = getPropertyString(LOG_PATH) + "CrearIndividuosXIndicador_" + id + ".log";
		PrintStream out = new PrintStream(name, Charset.defaultCharset().name());
		setOut(out);
		setErr(out);
		logTime("Inicio: " + id, 1);
		setId(Long.toString(id));
		OracleIndividuoXIndicadorManager manager = new OracleIndividuoXIndicadorManager();
		manager.crearIndividuos();
		logTime("Fin: " + id, 1);
		logTime("Lanzando Procesos alternos...", 1);
		ProcesosAlternosProxy alternosManager = new ProcesosAlternosProxy(id);
		try {
			alternosManager.procesar();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

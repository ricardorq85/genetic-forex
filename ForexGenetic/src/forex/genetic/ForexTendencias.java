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
import java.util.logging.Logger;

import forex.genetic.exception.GeneticException;
import forex.genetic.facade.TendenciaFacade;
import forex.genetic.tendencia.manager.TendenciasManager;

/**
 *
 * @author ricardorq85
 */
public class ForexTendencias {

	/**
	 * @param args
	 *            the command line arguments
	 * @throws java.lang.InterruptedException
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		long id = currentTimeMillis();
		load().join();
		logTime("Tendencias: " + id, 1);
		String name = getPropertyString(LOG_PATH) + "Tendencias_" + id + ".log";
		PrintStream out = new PrintStream(name, Charset.defaultCharset().name());
		setOut(out);
		setErr(out);
		logTime("Inicio: " + id, 1);
		setId(Long.toString(id));
		try {
			TendenciaFacade facade = new TendenciaFacade();
			facade.procesarTendencias();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		logTime("Fin: " + id, 1);
	}

	private static final Logger LOG = Logger.getLogger(ForexTendencias.class.getName());
}

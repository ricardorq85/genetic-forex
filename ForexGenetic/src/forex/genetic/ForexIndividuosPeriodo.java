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
import java.text.ParseException;
import java.util.logging.Logger;

import forex.genetic.entities.ParametroOperacionPeriodo;
import forex.genetic.manager.IndividuosPeriodoManager;

/**
 *
 * @author ricardorq85
 */
public class ForexIndividuosPeriodo {

	/**
	 * @param args
	 *            the command line arguments
	 * @throws java.lang.InterruptedException
	 */
	public static void main(String[] args)
			throws IOException, ClassNotFoundException, InterruptedException, ParseException {
		long id = currentTimeMillis();
		load().join();
		logTime("ForexIndividuosXPeriodo: " + id, 1);
		StringBuilder name = new StringBuilder(getPropertyString(LOG_PATH));
		name.append("IndividuosXPeriodo_").append(id).append("_log.log");
		PrintStream out = new PrintStream(name.toString(), Charset.defaultCharset().name());
		setOut(out);
		setErr(out);
		logTime("Inicio: " + id, 1);
		setId(Long.toString(id));
		try {
			IndividuosPeriodoManager manager = new IndividuosPeriodoManager();
			manager.ejecutarIndividuosXPeriodo(new ParametroOperacionPeriodo(), 0);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		logTime("Fin: " + id, 1);
	}

	private static final Logger LOG = Logger.getLogger(ForexIndividuosPeriodo.class.getName());
}

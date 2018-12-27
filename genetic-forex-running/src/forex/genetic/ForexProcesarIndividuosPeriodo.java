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

import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.IndividuosPeriodoManager;
import forex.genetic.proxy.ProcesosAlternosProxy;

/**
 *
 * @author ricardorq85
 */
public class ForexProcesarIndividuosPeriodo {

	/**
	 * @param args
	 *            the command line arguments
	 * @throws java.lang.InterruptedException
	 */
	public static void main(String[] args)
			throws IOException, ClassNotFoundException, InterruptedException, ParseException {
		long id = currentTimeMillis();
		load().join();
		logTime("ProcesarIndividuosXPeriodo: " + id, 1);
		StringBuilder name = new StringBuilder(getPropertyString(LOG_PATH));
		name.append("ProcesarIndividuosXPeriodo_").append(id).append("_log.log");
		PrintStream out = new PrintStream(name.toString(), Charset.defaultCharset().name());
		setOut(out);
		setErr(out);
		logTime("Inicio: " + id, 1);
		setId(Long.toString(id));
		try {
			IndividuosPeriodoManager manager = new IndividuosPeriodoManager();
			manager.procesarIndividuosXPeriodo();
		} catch (SQLException | GeneticDAOException ex) {
			ex.printStackTrace();
		}
		logTime("Fin: " + id, 1);
		logTime("Lanzando Procesos alternos...", 1);
		try {
			ProcesosAlternosProxy alternosManager = new ProcesosAlternosProxy(id);
			alternosManager.procesar();
		} catch (GeneticBusinessException e) {
			e.printStackTrace();
		}
		logTime("Init Proceso alternos", 1);
	}
}

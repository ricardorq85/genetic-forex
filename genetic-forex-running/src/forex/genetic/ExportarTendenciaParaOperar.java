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

import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.tendencia.manager.ExportarTendenciaParaOperarManager;

/**
 *
 * @author ricardorq85
 */
public class ExportarTendenciaParaOperar {

	/**
	 * @param args
	 *            the command line arguments
	 * @throws java.lang.InterruptedException
	 * @throws SQLException 
	 * @throws GeneticDAOException 
	 */
	public static void main(String[] args)
			throws IOException, ClassNotFoundException, InterruptedException, ParseException, SQLException, GeneticDAOException {
		long id = currentTimeMillis();
		load().join();
		logTime("ExportarTendenciaParaOperar: " + id, 1);
		String name = getPropertyString(LOG_PATH) + "ExportarTendenciaParaOperar" + id + ".log";
		PrintStream out = new PrintStream(name, Charset.defaultCharset().name());
		setOut(out);
		setErr(out);
		logTime("Inicio: " + id, 1);
		setId(Long.toString(id));
		ExportarTendenciaParaOperarManager manager = new ExportarTendenciaParaOperarManager(DriverDBFactory.createDataClient("oracle"));
		manager.exportar();
		logTime("Fin: " + id, 1);
	}
}

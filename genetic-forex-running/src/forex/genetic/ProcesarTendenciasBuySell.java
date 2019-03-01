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
import java.text.ParseException;

import forex.genetic.exception.GeneticException;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.factory.ProcesarTendenciasFactory;
import forex.genetic.tendencia.manager.ProcesarTendenciasBuySellManager;
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public class ProcesarTendenciasBuySell {

	/**
	 * @param args the command line arguments
	 * @throws java.lang.InterruptedException
	 */
	public static void main(String[] args)
			throws IOException, ClassNotFoundException, InterruptedException, ParseException {
		long id = currentTimeMillis();
		load().join();
		logTime("ProcesarTendenciasBuySell: " + id + ". "
				+ (((args != null) && (args.length > 0)) ? args[0].toString() : ""), 1);
		String name = getPropertyString(LOG_PATH) + "ProcesarTendenciasBuySell" + id + ".log";
		PrintStream out = new PrintStream(name, Charset.defaultCharset().name());
		setOut(out);
		setErr(out);
		logTime("Inicio: " + id, 1);
		setId(Long.toString(id));
		try {
			ProcesarTendenciasBuySellManager manager = null;
			if ((args != null) && (args.length > 0)) {
				DataClient dc = DriverDBFactory.createDataClient(args[0]);
				manager = ProcesarTendenciasFactory.createManager(dc);
			} else {
				manager = ProcesarTendenciasFactory.createManager();
			}
			// new ProcesarTendenciasGrupalManager();
			// new ProcesarTendenciasIndividualManager();
			// new ProcesarTendenciasBuySellManager();
			manager.procesarTendencias();
		} catch (GeneticException ex) {
			ex.printStackTrace();
		}
		logTime("Fin: " + id, 1);
	}
}

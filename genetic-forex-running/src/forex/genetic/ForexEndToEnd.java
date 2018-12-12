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
import forex.genetic.mediator.EndToEndMediator;
import forex.genetic.mediator.OracleEndToEndMediator;

/**
 *
 * @author ricardorq85
 */
public class ForexEndToEnd {

	/**
	 * @param args
	 *            the command line arguments
	 * @throws java.lang.InterruptedException
	 */
	public static void main(String[] args)
			throws IOException, ClassNotFoundException, InterruptedException, ParseException, NoSuchMethodException {
		long id = currentTimeMillis();
		load().join();
		String prefix = "EndToEnd";
		logTime(prefix + ": " + id, 1);
		String name = getPropertyString(LOG_PATH) + prefix + id + ".log";
		PrintStream out = new PrintStream(name, Charset.defaultCharset().name());
		setOut(out);
		setErr(out);
		logTime("Inicio: " + id, 1);
		setId(Long.toString(id));
		try {
			EndToEndMediator mediator = new OracleEndToEndMediator();
			mediator.init();
			mediator.start();
		} catch (GeneticException ex) {
			ex.printStackTrace();
		}
		logTime("Fin: " + id, 1);
	}
}

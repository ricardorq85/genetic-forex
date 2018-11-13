/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.mongo;

import static forex.genetic.delegate.GeneticDelegate.setId;
import static forex.genetic.manager.PropertiesManager.getPropertyString;
import static forex.genetic.manager.PropertiesManager.load;
import static forex.genetic.util.Constants.LOG_PATH;
import static forex.genetic.util.LogUtil.logTime;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.setErr;
import static java.lang.System.setOut;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import forex.genetic.mediator.mongo.MongoPointToPointMediator;

/**
 *
 * @author ricardorq85
 */
public class MongoPointToPoint {

	/**
	 * @param args the command line arguments
	 * @throws                              java.lang.InterruptedException
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args)
			throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
		long id = currentTimeMillis();
		load().join();
		String prefix = "PointToPoint";
		logTime(prefix + ": " + id, 1);
		String name = getPropertyString(LOG_PATH) + prefix + id + ".log";
		PrintStream out = new PrintStream(name, Charset.defaultCharset().name());
		setOut(out);
		setErr(out);
		logTime("Inicio: " + id, 1);
		setId(Long.toString(id));
		try {
			MongoPointToPointMediator mediator = new MongoPointToPointMediator();
			mediator.init();
			// mediator.start();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		logTime("Fin: " + id, 1);
	}
}

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
import java.text.ParseException;

import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.manager.mongodb.MongoExportManager;
import forex.genetic.manager.mongodb.MongoExportTendenciaManager;

/**
 *
 * @author ricardorq85
 */
public class MongoExportarJsonTendencias {

	/**
	 * @param args the command line arguments
	 * @throws                              java.lang.InterruptedException
	 * @throws GeneticBusinessException
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 * @throws ParseException 
	 */
	public static void main(String[] args)
			throws GeneticBusinessException, InterruptedException, FileNotFoundException, UnsupportedEncodingException, ParseException {
		long id = currentTimeMillis();
		load().join();
		logTime("MongoExportarJsonTendencias: " + id, 1);
		String name = getPropertyString(LOG_PATH) + "MongoExportarJsonTendencias_" + id + ".log";
		PrintStream out = new PrintStream(name, Charset.defaultCharset().name());
		setOut(out);
		setErr(out);
		logTime("Inicio: " + id, 1);
		setId(Long.toString(id));
		MongoExportTendenciaManager manager = new MongoExportTendenciaManager();
		manager.exportar();
		logTime("Fin: " + id, 1);
	}

}

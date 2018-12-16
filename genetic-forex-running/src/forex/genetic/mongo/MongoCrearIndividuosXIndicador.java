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

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.sql.SQLException;

import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.manager.IndividuoXIndicadorManager;
import forex.genetic.manager.mongodb.MongoIndividuoXIndicadorManager;

/**
 *
 * @author ricardorq85
 */
public class MongoCrearIndividuosXIndicador {

	/**
	 * @param args
	 *            the command line arguments
	 * @throws java.io.IOException
	 * @throws java.sql.SQLException
	 * @throws GeneticDAOException 
	 * @throws GeneticBusinessException 
	 */
	public static void main(String[] args)
			throws IOException, ClassNotFoundException, InterruptedException, SQLException, GeneticDAOException, GeneticBusinessException {
		long id = currentTimeMillis();
		load().join();
		logTime("MongoIndividuoXIndicadorManager.java: " + id, 1);
		String name = getPropertyString(LOG_PATH) + "MongoIndividuoXIndicadorManager_" + id + ".log";
		PrintStream out = new PrintStream(name, Charset.defaultCharset().name());
		setOut(out);
		setErr(out);
		logTime("Inicio: " + id, 1);
		setId(Long.toString(id));
		IndividuoXIndicadorManager manager = new MongoIndividuoXIndicadorManager(DriverDBFactory.createDataClient("mongodb"));
		manager.crearIndividuos();
		logTime("Fin: " + id, 1);
	}
}

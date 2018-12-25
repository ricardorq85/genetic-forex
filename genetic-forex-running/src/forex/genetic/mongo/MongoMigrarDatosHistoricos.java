/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.mongo;

import static forex.genetic.delegate.GeneticDelegate.setId;
import static forex.genetic.manager.PropertiesManager.getOperationType;
import static forex.genetic.manager.PropertiesManager.getPair;
import static forex.genetic.manager.PropertiesManager.getPropertyString;
import static forex.genetic.manager.PropertiesManager.load;
import static forex.genetic.util.Constants.LOG_PATH;
import static forex.genetic.util.LogUtil.logTime;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.setErr;
import static java.lang.System.setOut;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;

import forex.genetic.delegate.MigracionDelegate;
import forex.genetic.exception.GeneticBusinessException;

/**
 *
 * @author ricardorq85
 */
public class MongoMigrarDatosHistoricos {

	public static void main(String[] args) throws InterruptedException, FileNotFoundException, ClassNotFoundException, GeneticBusinessException, IOException {
		long id = currentTimeMillis();
		load().join();
		logTime("MongoMigrarDatosHistoricos: " + id, 1);
		setId("" + id);
		StringBuilder name = new StringBuilder();
		name.append(getPropertyString(LOG_PATH)).append("MongoMigrarDatosHistoricos_");
		name.append(getOperationType()).append(getPair()).append(id).append(".log");
		PrintStream out = new PrintStream(name.toString(), Charset.defaultCharset().name());
		setOut(out);
		setErr(out);

		// Object t = new MongoDatoHistoricoDAO(true);
		MigracionDelegate delegate = new MigracionDelegate();
		logTime("Init Migrar Datos Historicos", 1);
//		delegate.migrarDatosHistoricos();
		logTime("End Migrar Datos Historicos", 1);
		delegate.migrarIndividuos();
//		MongoTendenciaParaOperarDAO tendenciaParaOperarDAO = new MongoTendenciaParaOperarDAO();
		// Date fecha = tendenciaParaOperarDAO.getFechaBaseMinima();
		// logTime(DateUtil.getDateString(fecha), 1);
	}
}

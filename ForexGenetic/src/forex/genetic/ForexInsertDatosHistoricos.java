/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic;

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

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.sql.SQLException;

import forex.genetic.delegate.PoblacionDelegate;
import forex.genetic.proxy.ProcesosAlternosProxy;

/**
 *
 * @author ricardorq85
 */
public class ForexInsertDatosHistoricos {

	/**
	 *
	 * @param args
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		long id = currentTimeMillis();
		load().join();
		logTime("ForexInsertDatosHistoricos: " + id, 1);
		setId("" + id);
		StringBuilder name = new StringBuilder();
		name.append(getPropertyString(LOG_PATH)).append("InsertDatosHistoricos_");
		name.append(getOperationType()).append(getPair()).append(id).append(".log");
		PrintStream out = new PrintStream(name.toString(), Charset.defaultCharset().name());
		setOut(out);
		setErr(out);
		PoblacionDelegate delegate = new PoblacionDelegate();
		logTime("Init Insert Datos Historicos", 1);
		delegate.cargarDatosHistoricos();
		logTime("End Insert Datos Historicos", 1);
		logTime("Lanzando Procesos alternos...", 1);
		try {
			ProcesosAlternosProxy alternosManager = new ProcesosAlternosProxy(id);
			alternosManager.procesar();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		logTime("End Proceso alternos", 1);
	}
}

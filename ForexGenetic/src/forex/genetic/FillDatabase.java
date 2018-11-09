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

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import forex.genetic.delegate.FillDatabaseDelegate;

public class FillDatabase {

	public static void main(String[] args) throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
		long id = currentTimeMillis();
		load().join();
		logTime("FillDatabaseDelegate: " + id, 1);
		setId("" + id);
		StringBuilder name = new StringBuilder();
		name.append(getPropertyString(LOG_PATH)).append("FillDatabaseDelegate");
		name.append(getOperationType()).append(getPair()).append(id).append(".log");
		PrintStream out = new PrintStream(name.toString(), Charset.defaultCharset().name());
		setOut(out);
		setErr(out);

		FillDatabaseDelegate delegate = new FillDatabaseDelegate();
		logTime("Init FillDatabaseDelegate", 1);
		delegate.fill();
		logTime("End FillDatabaseDelegate", 1);

//		MongoTendenciaParaOperarDAO tendenciaParaOperarDAO = new MongoTendenciaParaOperarDAO();
		// Date fecha = tendenciaParaOperarDAO.getFechaBaseMinima();
		// logTime(DateUtil.getDateString(fecha), 1);

	}

}

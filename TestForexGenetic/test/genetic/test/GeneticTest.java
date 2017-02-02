package genetic.test;

import static forex.genetic.delegate.GeneticDelegate.setId;
import static forex.genetic.manager.PropertiesManager.load;
import static forex.genetic.util.LogUtil.logTime;
import static java.lang.System.currentTimeMillis;

import org.junit.BeforeClass;

public class GeneticTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		long id = currentTimeMillis();
		load().join();
		logTime("GeneticTest.java: " + id, 1);
		// String name = getPropertyString(LOG_PATH) + "GeneticTest_" + id +
		// ".log";
		// PrintStream out = new PrintStream(name,
		// Charset.defaultCharset().name());
		// setOut(out);
		// setErr(out);
		logTime("Inicio: " + id, 1);
		setId(Long.toString(id));
	}

}

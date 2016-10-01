package forex.genetic.proxy;

import static forex.genetic.manager.PropertiesManager.getPropertyString;
import static forex.genetic.util.Constants.LOG_PATH;
import static forex.genetic.util.LogUtil.logTime;
import static java.lang.System.setErr;
import static java.lang.System.setOut;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import forex.genetic.entities.Individuo;
import forex.genetic.manager.BorradoCantidadOperacionesExageradasManager;
import forex.genetic.manager.BorradoDuplicadosManager;
import forex.genetic.manager.BorradoInconsistentesStopLossManager;
import forex.genetic.manager.BorradoIndividuoSinOperacionesManager;
import forex.genetic.manager.BorradoManager;

public class ProcesosAlternosProxy {

	private long id;
	List<BorradoManager> managers;

	public ProcesosAlternosProxy(long id) throws ClassNotFoundException, SQLException {
		this.id = id;
		managers = new ArrayList<>();
		managers.add(new BorradoCantidadOperacionesExageradasManager());
		managers.add(new BorradoInconsistentesStopLossManager());
		managers.add(new BorradoIndividuoSinOperacionesManager());
		managers.add(new BorradoDuplicadosManager());
	}

	public void procesar()
			throws ClassNotFoundException, SQLException, FileNotFoundException, UnsupportedEncodingException {
		StringBuilder name = new StringBuilder(getPropertyString(LOG_PATH));
		name.append("ProcesosAlternos").append(id).append("_log.log");
		PrintStream out = new PrintStream(name.toString(), Charset.defaultCharset().name());
		setOut(out);
		setErr(out);
		logTime("Init Procesos alternos", 1);
		Random random = new Random();
		while (!managers.isEmpty()) {
			int index = random.nextInt(managers.size());
			BorradoManager manager = managers.get(index);
			logTime(manager.getClass().getName(), 1);
			manager.borrarIndividuos();
			managers.remove(index);
		}
		logTime("Fin Procesos alternos", 1);
	}

	public void procesar(Individuo individuo)
			throws ClassNotFoundException, SQLException, FileNotFoundException {
		logTime("Init Procesos alternos:" + individuo.getId(), 1);
		Random random = new Random();
		while (!managers.isEmpty()) {
			int index = random.nextInt(managers.size());
			BorradoManager manager = managers.get(index);
			logTime(manager.getClass().getName(), 1);
			manager.validarYBorrarIndividuo(individuo);
			managers.remove(index);
		}
		logTime("Fin Procesos alternos:" + individuo.getId(), 1);
	}
}

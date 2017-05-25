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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import forex.genetic.entities.Individuo;
import forex.genetic.manager.borrado.BorradoCantidadOperacionesExageradasManager;
import forex.genetic.manager.borrado.BorradoDuplicadoIndividuoBorradoManager;
import forex.genetic.manager.borrado.BorradoDuplicadoIndividuoManager;
import forex.genetic.manager.borrado.BorradoDuplicadoOperacionesManager;
import forex.genetic.manager.borrado.BorradoInconsistentesStopLossManager;
import forex.genetic.manager.borrado.BorradoIndividuoSinOperacionesManager;
import forex.genetic.manager.borrado.BorradoManager;
import forex.genetic.manager.borrado.BorradoXDuracionPromedioManager;
import forex.genetic.manager.borrado.BorradoXIndicadoresCloseMinimos;
import forex.genetic.manager.borrado.BorradoXIntervaloIndicadorManager;
import forex.genetic.util.jdbc.JDBCUtil;

public class ProcesosAlternosProxy {

	private long id;
	protected Connection conn;
	List<BorradoManager> managers;

	public ProcesosAlternosProxy(long id) throws ClassNotFoundException, SQLException {
		this.id = id;
		conn = JDBCUtil.getConnection();
	}

	public void procesar()
			throws ClassNotFoundException, SQLException, FileNotFoundException, UnsupportedEncodingException {
		managers = new ArrayList<>();
		managers.add(new BorradoDuplicadoIndividuoManager(conn));
		managers.add(new BorradoDuplicadoIndividuoBorradoManager(conn));
		managers.add(new BorradoXIndicadoresCloseMinimos(conn));
		managers.add(new BorradoInconsistentesStopLossManager(conn));
		managers.add(new BorradoXIntervaloIndicadorManager(conn));
		managers.add(new BorradoXDuracionPromedioManager(conn));
		managers.add(new BorradoIndividuoSinOperacionesManager(conn));
		managers.add(new BorradoCantidadOperacionesExageradasManager(conn));
		managers.add(new BorradoDuplicadoOperacionesManager(conn));

		StringBuilder name = new StringBuilder(getPropertyString(LOG_PATH));
		name.append("ProcesosAlternos").append(id).append(".log");
		PrintStream out = new PrintStream(name.toString(), Charset.defaultCharset().name());
		setOut(out);
		setErr(out);
		logTime("Init Procesos alternos", 1);
		while (!managers.isEmpty()) {
			int index = 0; // random.nextInt(managers.size());
			BorradoManager manager = managers.get(index);
			logTime(manager.getClass().getName(), 2);
			manager.borrarIndividuos();
			managers.remove(index);
		}
		JDBCUtil.close(conn);
		logTime("Fin Procesos alternos", 1);
	}

	public void procesar(Individuo individuo) throws ClassNotFoundException, FileNotFoundException {
		try {
			managers = new ArrayList<>();
			managers.add(new BorradoDuplicadoIndividuoManager(conn));		
			managers.add(new BorradoDuplicadoIndividuoBorradoManager(conn));
			managers.add(new BorradoXIndicadoresCloseMinimos(conn));
			managers.add(new BorradoInconsistentesStopLossManager(conn));
			managers.add(new BorradoXIntervaloIndicadorManager(conn));
			managers.add(new BorradoXDuracionPromedioManager(conn));
			managers.add(new BorradoIndividuoSinOperacionesManager(conn));
			managers.add(new BorradoCantidadOperacionesExageradasManager(conn));
			// managers.add(new BorradoDuplicadoOperacionesManager(conn));

			logTime("Init Procesos alternos:" + individuo.getId(), 1);
			int index = 0;
			while (!managers.isEmpty()) {
				// int index = random.nextInt(managers.size());
				BorradoManager manager = managers.get(0);
				logTime(manager.getClass().getName(), 2);
				manager.validarYBorrarIndividuo(individuo);
				managers.remove(index);
			}
		} catch (SQLException e) {
			logTime("SQLException: " + individuo.getId() + " " + e.getMessage(), 1);
			e.printStackTrace();
			JDBCUtil.rollback(conn);
		} finally {
			JDBCUtil.close(conn);
			logTime("Fin Procesos alternos:" + individuo.getId(), 2);
		}
	}
}

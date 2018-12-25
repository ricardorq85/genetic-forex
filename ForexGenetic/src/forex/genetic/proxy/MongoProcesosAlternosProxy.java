package forex.genetic.proxy;

import static forex.genetic.util.LogUtil.logTime;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import forex.genetic.entities.Individuo;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.borrado.BorradoManager;
import forex.genetic.manager.borrado.MongoBorradoDuplicadoIndividuoManager;
import forex.genetic.util.jdbc.DataClient;

public class MongoProcesosAlternosProxy {

	private long id;
	private DataClient dataClient;
	List<BorradoManager> managers;

	public MongoProcesosAlternosProxy(long id, DataClient dc) throws ClassNotFoundException, SQLException {
		this.id = id;
		this.dataClient = dc;
	}

	public void procesar(Individuo individuo)
			throws ClassNotFoundException, FileNotFoundException, GeneticDAOException {
		try {
			managers = new ArrayList<>();
			managers.add(new MongoBorradoDuplicadoIndividuoManager(dataClient));

			logTime("Init Procesos alternos:" + individuo.getId(), 2);
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
		} finally {
			logTime("Fin Procesos alternos:" + individuo.getId(), 2);
		}
	}
}

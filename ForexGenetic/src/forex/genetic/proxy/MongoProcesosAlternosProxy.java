package forex.genetic.proxy;

import static forex.genetic.util.LogUtil.logTime;

import java.util.ArrayList;
import java.util.List;

import forex.genetic.entities.dto.ProcesoEjecucionDTO;
import forex.genetic.entities.mongo.MongoEstadistica;
import forex.genetic.entities.mongo.MongoIndividuo;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.borrado.BorradoManager;
import forex.genetic.manager.borrado.MongoBorradoCantidadOperacionesExageradasManager;
import forex.genetic.manager.borrado.MongoBorradoDuplicadoConBorradoIndividuoManager;
import forex.genetic.manager.borrado.MongoBorradoDuplicadoIndividuoManager;
import forex.genetic.manager.borrado.MongoBorradoIndividuoIncompletoManager;
import forex.genetic.manager.borrado.MongoBorradoIndividuoSinOperacionesManager;
import forex.genetic.manager.borrado.MongoBorradoXDuracionPromedioManager;
import forex.genetic.util.jdbc.DataClient;

public class MongoProcesosAlternosProxy {

	private long id;
	private DataClient dataClient;
	List<BorradoManager> managers;

	public MongoProcesosAlternosProxy(long id, DataClient dc) {
		this.id = id;
		this.dataClient = dc;
	}

	public void procesar(MongoIndividuo individuo) {
		try {
			MongoEstadistica estadisticaAnterior = null;
			ProcesoEjecucionDTO procesoEjecucion = individuo.getProcesoEjecucion();
			if ((procesoEjecucion != null) && (procesoEjecucion.getMaxFechaHistorico() != null)) {
				try {
					estadisticaAnterior = (MongoEstadistica) dataClient.getDaoEstadistica().getLast(individuo,
							procesoEjecucion.getMaxFechaHistorico());
				} catch (GeneticDAOException e) {
				}
			}
			managers = new ArrayList<>();
			managers.add(new MongoBorradoIndividuoSinOperacionesManager(dataClient, estadisticaAnterior));
			managers.add(new MongoBorradoXDuracionPromedioManager(dataClient, estadisticaAnterior));
			managers.add(new MongoBorradoIndividuoIncompletoManager(dataClient, estadisticaAnterior));
			managers.add(new MongoBorradoCantidadOperacionesExageradasManager(dataClient, estadisticaAnterior));
			managers.add(new MongoBorradoDuplicadoIndividuoManager(dataClient, estadisticaAnterior));
			managers.add(new MongoBorradoDuplicadoConBorradoIndividuoManager(dataClient, estadisticaAnterior));
			logTime("Init Procesos alternos:" + individuo.getId(), 2);
			for (BorradoManager borradoManager : managers) {
				logTime(borradoManager.getClass().getName(), 2);
				if (borradoManager.validarYBorrarIndividuo(individuo)) {
					break;
				}
			}
		} catch (GeneticBusinessException e) {
			e.printStackTrace();
		} finally {
			logTime("Fin Procesos alternos:" + individuo.getId(), 2);
		}
	}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.entities.Individuo;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.ProcesoIndividuoManager;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.thread.oracle.OracleProcesarIndividuoThread;
import forex.genetic.util.LogUtil;
import forex.genetic.util.ThreadUtil;
import forex.genetic.util.jdbc.DataClient;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class OracleProcesoIndividuoManager extends ProcesoIndividuoManager {

	private Connection conn;

	public OracleProcesoIndividuoManager(DataClient dc) {
		super(dc);
	}

	public void process(boolean onlyOne) throws GeneticBusinessException {
		boolean any;
		// maxFechaHistorico = DateUtil.adicionarDias(dhDAO.getFechaHistoricaMaxima(),
		// -10);
		try {
			Date maxFechaHistorico = dataClient.getDaoDatoHistorico().getFechaHistoricaMaxima();
			Date minFechaHistorico = dataClient.getDaoDatoHistorico().getFechaHistoricaMinima();
			do {
				any = false;
				List<Thread> threads = new ArrayList<>();
				int countFiltro = 1;
				String filtroAdicional = PropertiesManager.getPropertyString("FILTRO_ADICIONAL_" + countFiltro);
				while (filtroAdicional != null) {
					LogUtil.logTime("Obteniendo individuos para el filtro " + countFiltro + ": " + filtroAdicional, 1);
					List<Individuo> individuos = dataClient.getDaoProcesoEjecucion().getIndividuos(filtroAdicional,
							maxFechaHistorico);
					if ((individuos != null) && (!individuos.isEmpty())) {
						OracleProcesarIndividuoThread procesarIndividuoThread = new OracleProcesarIndividuoThread(
								"FILTRO_ADICIONAL_" + countFiltro, individuos);
						procesarIndividuoThread.setMaxFechaHistorico(maxFechaHistorico);
						procesarIndividuoThread.setMinFechaHistorico(minFechaHistorico);
						procesarIndividuoThread.start();
						threads.add(procesarIndividuoThread);
						any = true;
					} else {
						LogUtil.logTime("No existen individuos para el FILTRO_ADICIONAL_" + countFiltro + ": "
								+ filtroAdicional, 1);
					}

					countFiltro++;
					try {
						filtroAdicional = PropertiesManager.getPropertyString("FILTRO_ADICIONAL_" + countFiltro);
					} catch (IllegalArgumentException ex) {
						filtroAdicional = null;
					}
				}
				ThreadUtil.joinThreads(threads);
				this.consolidarIndividuo();
			} while (any && !onlyOne);
		} catch (SQLException | GeneticDAOException ex) {
			ex.printStackTrace();
		} finally {
			JDBCUtil.close(conn);
		}
	}

	private void consolidarIndividuo() throws SQLException {
		// JDBCUtil.refreshMaterializedViews(conn, vistas);
	}
}

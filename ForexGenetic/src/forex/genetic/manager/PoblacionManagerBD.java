/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.ProcesoPoblacionDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.thread.ProcesarIndividuoThreadBD;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class PoblacionManagerBD {

	private Connection conn;
	private ProcesoPoblacionDAO poblacionDAO;
	private DatoHistoricoDAO dhDAO;
	private Date maxFechaHistorico;
	private Date minFechaHistorico;
	private String[] vistas = { "FILTERED_PTFS", "FILTERED_EOP", "FILTERED_PARALELAS_EOP",
			"FILTERED_PARA_OPERAR_SELL", "FILTERED_PARA_OPERAR_BUY", "FILTERED_PARA_OPERAR_BOTH" };

	public PoblacionManagerBD() {
		super();
		try {
			conn = JDBCUtil.getConnection();
			poblacionDAO = new ProcesoPoblacionDAO(conn);
			dhDAO = new DatoHistoricoDAO(conn);
			maxFechaHistorico = dhDAO.getFechaHistoricaMaxima();
			minFechaHistorico = dhDAO.getFechaHistoricaMinima();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void process() {
		boolean any;
		try {
			do {
				any = false;
				List<Thread> threads = new ArrayList<>();
				int countFiltro = 1;
				String filtroAdicional = PropertiesManager.getPropertyString("FILTRO_ADICIONAL_" + countFiltro);
				while (filtroAdicional != null) {
					LogUtil.logTime("Obteniendo individuos para el filtro " + countFiltro + ": " + filtroAdicional, 1);
					List<Individuo> individuos = poblacionDAO.getIndividuos(filtroAdicional, maxFechaHistorico);
					if ((individuos != null) && (!individuos.isEmpty())) {
						ProcesarIndividuoThreadBD procesarIndividuoThread = new ProcesarIndividuoThreadBD(
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
				for (Thread thread : threads) {
					thread.join();
				}
				this.consolidarIndividuo();
			} while (any);
		} catch (InterruptedException | SQLException ex) {
			ex.printStackTrace();
		} finally {
			JDBCUtil.close(conn);
		}
	}

	private void consolidarIndividuo() throws SQLException {
		JDBCUtil.refreshMaterializedViews(conn, vistas);
	}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread;

import java.util.Date;
import java.util.List;

import forex.genetic.dao.IProcesoEjecucionDAO;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.LogUtil;

/**
 *
 * @author ricardorq85
 */
public class ProcesarIndividuoThread extends Thread {

	private IProcesoEjecucionDAO dao = null;
	private List<String> individuos = null;

	/**
	 *
	 * @param name
	 * @param dao
	 * @param individuos
	 */
	public ProcesarIndividuoThread(String name, IProcesoEjecucionDAO dao, List<String> individuos) {
		super(name);
		this.dao = dao;
		this.individuos = individuos;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < individuos.size(); i++) {
				String idIndividuo = individuos.get(i);
				try {
					if (dao.isClosed()) {
						dao.restoreConnection();
					}
					procesarIndividuo(idIndividuo);
				} catch (GeneticDAOException ex) {
					try {
						dao.rollback();
					} catch (GeneticDAOException ex1) {
						ex.printStackTrace();
					}
					ex.printStackTrace();
					System.err.println(ex.getMessage() + " " + idIndividuo);
				}
			}
		} finally {
			try {
				dao.close();
			} catch (GeneticDAOException e) {
				e.printStackTrace();
			}
		}
	}

	private void procesarIndividuo(String idIndividuo) throws GeneticDAOException {
		Date fechaInicial = null;// dao.getFechaHistoricaMinima();
		Date fechaOperacion = fechaInicial;
		Date fechaMaxima = null;// dao.getFechaHistoricaMaxima();
		boolean hasOperacionesMinimas = true;
		int diasProceso = 10;

		while (fechaOperacion.compareTo(fechaMaxima) < 0 && hasOperacionesMinimas) {
			LogUtil.logTime("Procesar Individuo;" + this.getName() + ";" + idIndividuo + ";" + fechaOperacion, 1);
			int counthistorico = dao.getCountHistorico(fechaOperacion, diasProceso);
			if (counthistorico > 0) {
				dao.insertOperacionBase(fechaOperacion, diasProceso, idIndividuo);
			}
			dao.insertProceso(fechaOperacion, idIndividuo);
			dao.commit();

			int has = dao.hasMinimumOperations(fechaInicial, fechaOperacion, idIndividuo);
			hasOperacionesMinimas = (has == 1);
			fechaOperacion = dao.getFechaOperacion(fechaOperacion, diasProceso, idIndividuo);
		}
	}
}

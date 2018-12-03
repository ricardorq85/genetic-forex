/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.facade;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import forex.genetic.dao.oracle.OracleIndividuoDAO;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.manager.oracle.OracleProcesoIndividuoManager;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class PoblacionFacade implements IGeneticFacade {

	/**
	 * @throws GeneticDAOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 *
	 */
	public void process() throws GeneticBusinessException {
		this.process(false);
	}

	public void process(boolean onlyOne) throws GeneticBusinessException {
		OracleProcesoIndividuoManager manager;
		try {
			manager = new OracleProcesoIndividuoManager(DriverDBFactory.createDataClient("oracle"));
			manager.process(onlyOne);
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(null, e);
		}
	}

	/**
	 *
	 * @param indicadorController
	 * @param poblacion
	 */
	public void cargarPoblacion(IndicadorController indicadorController, Poblacion poblacion) {
		Connection conn = null;
		try {
			conn = JDBCUtil.getConnection();
			OracleIndividuoDAO dao = new OracleIndividuoDAO(conn);

			List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
			int countError = 0;
			for (IndividuoEstrategia individuo1 : individuos) {
				try {
					IndividuoEstrategia individuo = individuo1;
					dao.insertIndividuo(individuo);
					dao.insertIndicadorIndividuo(indicadorController, individuo);
				} catch (GeneticDAOException ex) {
					// ex.printStackTrace();
					countError++;
				}
			}
			conn.commit();
			LogUtil.logTime(
					"Individuos cargados=" + (individuos.size() - countError) + ";Individuos con error=" + countError,
					1);
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
		} finally {
			JDBCUtil.close(conn);
		}
	}

}

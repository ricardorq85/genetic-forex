/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.facade;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.dao.oracle.OracleDatoHistoricoDAO;
import forex.genetic.entities.Point;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class DatoHistoricoFacade implements IGeneticFacade {

	/**
	 *
	 * @param points
	 */
	public void cargarDatoHistorico(List<Point> points) throws GeneticDAOException {
		Connection conn = null;
		try {
			conn = JDBCUtil.getConnection();
			OracleDatoHistoricoDAO dao = new OracleDatoHistoricoDAO(conn);
			//MongoDatoHistoricoDAO mongoDao = new MongoDatoHistoricoDAO(true);
			int countError = 0;

			for (int i = 0; i < points.size(); i++) {
				Point point = points.get(i);
				try {
					//mongoDao.insertOrUpdate(point);
					if (dao.exists(point)) {
						dao.update(point);
						System.out.print("*");
					} else {
						dao.insert(point);
						System.out.print(".");
					}
					if ((i % 3000) == 0) {
						conn.commit();
						System.out.println("");
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
					countError++;
				}
			}
			conn.commit();
			LogUtil.logTime("Datos cargados=" + (points.size() - countError) + ";Datos con error=" + countError, 1);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			JDBCUtil.close(conn);
		}
	}
}

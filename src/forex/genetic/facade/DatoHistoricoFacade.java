/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.facade;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.entities.Point;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class DatoHistoricoFacade {

    public void cargarDatoHistorico(List<Point> points) {
        Connection conn = null;
        try {
            conn = JDBCUtil.getConnection();
            DatoHistoricoDAO dao = new DatoHistoricoDAO(conn);
            int countError = 0;

            for (int i = 0; i < points.size(); i++) {
                Point point = points.get(i);
                try {
                    dao.insertDatoHistorico(point);
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

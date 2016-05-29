/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.IndividuoDAO;
import forex.genetic.dao.OperacionesDAO;
import forex.genetic.dao.ProcesoPoblacionDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.manager.OperacionesManager;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class ProcesarIndividuoThreadBD extends Thread {

    private List<Individuo> individuos = null;
    private Connection conn = null;

    public ProcesarIndividuoThreadBD(String name, List<Individuo> individuos) {
        super(name);
        this.individuos = individuos;
    }

    public void run() {
        try {
            conn = JDBCUtil.getConnection();
            for (int i = 0; i < individuos.size(); i++) {
                Individuo individuo = individuos.get(i);
                try {
                    procesarIndividuo(individuo);
                } catch (SQLException ex) {
                    conn.rollback();
                    ex.printStackTrace();
                    System.err.println(ex.getMessage() + " " + individuo.getId());
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void procesarIndividuo(Individuo individuo) throws SQLException, ClassNotFoundException {
        DatoHistoricoDAO daoHistorico = new DatoHistoricoDAO(conn);
        OperacionesDAO daoOperaciones = new OperacionesDAO(conn);
        IndividuoDAO daoIndividuo = new IndividuoDAO(conn);
        ProcesoPoblacionDAO daoProceso = new ProcesoPoblacionDAO(conn);

        List<Point> points = daoHistorico.consultarHistorico(individuo.getFechaHistorico());
        daoIndividuo.consultarDetalleIndividuoProceso(individuo);
        OperacionesManager operacionesManager = new OperacionesManager();
        while ((points != null) && (!points.isEmpty())) {
            Date lastDate = points.get(points.size() - 1).getDate();
            LogUtil.logTime("Procesar Individuo;" + this.getName() + ";" + individuo.getId() + ";lastDate=" + lastDate, 1);
            List<Order> ordenes = operacionesManager.calcularOperaciones(points, individuo);
            if (individuo.getFechaApertura() != null) {
                if (ordenes.get(0).getCloseDate() != null) {
                    daoOperaciones.updateOperacion(individuo, ordenes.get(0), individuo.getFechaApertura());
                }
                ordenes.remove(0);
                individuo.setFechaApertura(null);
            }
            daoOperaciones.insertOperaciones(individuo, ordenes);
            int processed = daoProceso.updateProceso(lastDate, individuo.getId());
            if (processed == 0) {
                daoProceso.insertProceso(lastDate, individuo.getId());
            }
            conn.commit();
            points = daoHistorico.consultarHistorico(lastDate);
            if (individuo.getCurrentOrder() != null) {
                individuo.setFechaApertura(individuo.getCurrentOrder().getOpenDate());
            }
        }
    }
}

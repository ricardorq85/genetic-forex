/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.dao.ProcesoPoblacionDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.thread.ProcesarIndividuoThreadBD;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class PoblacionManagerBD {

    /**
     *
     */
    public void process() {
        boolean any;
        do {
            any = false;
            try {
                List<Thread> threads = new ArrayList<>();
                int countFiltro = 1;
                String filtroAdicional = PropertiesManager.getPropertyString("FILTRO_ADICIONAL_" + countFiltro);
                while (filtroAdicional != null) {
                    Connection conn = JDBCUtil.getConnection();
                    ProcesoPoblacionDAO dao = new ProcesoPoblacionDAO(conn);
                    List<Individuo> individuos = dao.getIndividuos(filtroAdicional);
                    if ((individuos != null) && (!individuos.isEmpty())) {
                        ProcesarIndividuoThreadBD procesarIndividuoThread
                                = new ProcesarIndividuoThreadBD("FILTRO_ADICIONAL_" + countFiltro, individuos);
                        procesarIndividuoThread.start();
                        threads.add(procesarIndividuoThread);
                        any = true;
                    } else {
                        LogUtil.logTime("No existen individuos para el filtro " + countFiltro, 1);
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
            } catch (InterruptedException | ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
            }
        } while (any);
    }
}

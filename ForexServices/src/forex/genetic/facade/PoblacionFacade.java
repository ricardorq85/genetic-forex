/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.facade;

import forex.genetic.dao.IndividuoDAO;
import forex.genetic.dao.ProcesoPoblacionDAO;
import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Poblacion;
import forex.genetic.manager.PoblacionManagerBD;
import forex.genetic.util.io.PropertiesManager;
import forex.genetic.manager.controller.IndicadorController;
import forex.genetic.thread.ProcesarIndividuoThread;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricardorq85
 */
public class PoblacionFacade {

    /**
     *
     */
    public void process() {
        PoblacionManagerBD manager = new PoblacionManagerBD();
        manager.process();
    }

    /**
     *
     */
    public void process2() {
        List<Thread> threads = new ArrayList<Thread>();
        try {
            int countFiltro = 1;
            String filtroAdicional = PropertiesManager.getPropertyString("FILTRO_ADICIONAL_" + countFiltro);
            while (filtroAdicional != null) {
                Connection conn = JDBCUtil.getConnection();
                ProcesoPoblacionDAO dao = new ProcesoPoblacionDAO(conn);
                List<String> individuos = null;//dao.getIndividuos(filtroAdicional);
                ProcesarIndividuoThread procesarIndividuoThread = new ProcesarIndividuoThread("FILTRO_ADICIONAL_" + countFiltro, dao, individuos);
                procesarIndividuoThread.start();
                threads.add(procesarIndividuoThread);

                countFiltro++;
                try {
                    filtroAdicional = PropertiesManager.getPropertyString("FILTRO_ADICIONAL_" + countFiltro);
                } catch (IllegalArgumentException ex) {
                    filtroAdicional = null;
                }
            }
            for (int i = 0; i < threads.size(); i++) {
                Thread thread = threads.get(i);
                thread.join();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(PoblacionFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
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
            IndividuoDAO dao = new IndividuoDAO(conn);

            List<IndividuoEstrategia> individuos = poblacion.getIndividuos();
            int countError = 0;
            for (IndividuoEstrategia individuo1 : individuos) {
                try {
                    IndividuoEstrategia individuo = individuo1;
                    dao.insertIndividuo(individuo);
                    dao.insertIndicadorIndividuo(indicadorController, individuo);
                } catch (SQLException ex) {
                    //ex.printStackTrace();
                    countError++;
                }
            }            
            conn.commit();
            LogUtil.logTime("Individuos cargados=" + (individuos.size() - countError) + ";Individuos con error=" + countError, 1);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBCUtil.close(conn);
        }
    }
}

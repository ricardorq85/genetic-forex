/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.thread;

import forex.genetic.dao.ProcesoPoblacionDAO;
import forex.genetic.util.LogUtil;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class ProcesarIndividuoThread extends Thread {

    private ProcesoPoblacionDAO dao = null;
    private List<String> individuos = null;

    public ProcesarIndividuoThread(String name, ProcesoPoblacionDAO dao, List<String> individuos) {
        super(name);
        this.dao = dao;
        this.individuos = individuos;
    }

    public void run() {
        try {
            for (int i = 0; i < individuos.size(); i++) {
                String idIndividuo = individuos.get(i);
                try {
                    if (dao.isClosed()) {
                        dao.restoreConnection();
                    }
                    procesarIndividuo(idIndividuo);
                } catch (SQLException ex) {
                    try {
                        dao.rollback();
                    } catch (SQLException ex1) {
                        ex.printStackTrace();
                    }
                    ex.printStackTrace();
                    System.err.println(ex.getMessage() + " " + idIndividuo);
                }
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            dao.close();
        }
    }

    private void procesarIndividuo(String idIndividuo) throws SQLException {
        Date fechaInicial = dao.getFechaHistoricaMinima();
        Date fechaOperacion = fechaInicial;
        Date fechaMaxima = dao.getFechaHistoricaMaxima();
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

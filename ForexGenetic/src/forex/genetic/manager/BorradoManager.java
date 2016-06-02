/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.dao.EstrategiaDAO;
import forex.genetic.dao.IndividuoDAO;
import forex.genetic.dao.OperacionesDAO;
import forex.genetic.dao.ProcesoPoblacionDAO;
import forex.genetic.dao.TendenciaDAO;
import forex.genetic.entities.Individuo;
import forex.genetic.util.LogUtil;
import static forex.genetic.util.LogUtil.logTime;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public abstract class BorradoManager {

    protected Connection conn = null;

    protected abstract List<Individuo> consultarIndividuos() throws ClassNotFoundException, SQLException;

    public abstract void borrarIndividuos() throws ClassNotFoundException, SQLException;

    protected void borrarIndividuos(String tipoProceso) throws ClassNotFoundException, SQLException {
        try {
            conn = JDBCUtil.getConnection();

            IndividuoDAO individuoDAO = new IndividuoDAO(conn);
            OperacionesDAO operacionDAO = new OperacionesDAO(conn);
            ProcesoPoblacionDAO procesoDAO = new ProcesoPoblacionDAO(conn);
            TendenciaDAO tendenciaDAO = new TendenciaDAO(conn);
            EstrategiaDAO estrategiaDAO = new EstrategiaDAO(conn);

            List<Individuo> individuos = this.consultarIndividuos();
            while ((individuos != null) && (!individuos.isEmpty())) {
                for (Individuo individuo : individuos) {
                    LogUtil.logTime("Individuo: " + individuo.getId(), 1);
                    int r_proceso = procesoDAO.deleteProceso(individuo.getId());
                    logTime("Registro borrados PROCESO = " + r_proceso, 1);
                    int r_operaciones = operacionDAO.deleteOperaciones(individuo.getId());
                    logTime("Registro borrados OPERACIONES = " + r_operaciones, 1);
                    int r_tendencia = tendenciaDAO.deleteTendencia(individuo.getId());
                    logTime("Registro borrados TENDENCIA = " + r_tendencia, 1);                    
                    int r_indEst = estrategiaDAO.deleteIndividuoEstrategia(individuo.getId());
                    logTime("Registro borrados INDIVIDUOESTRATEGIA = " + r_indEst, 1);
                    individuoDAO.smartDelete(individuo.getId(), tipoProceso, null);
                }
                conn.commit();
                individuos = this.consultarIndividuos();
            }
        } finally {
            JDBCUtil.close(conn);
        }
    }

}

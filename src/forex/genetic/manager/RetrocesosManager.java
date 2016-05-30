/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.OperacionesDAO;
import forex.genetic.dao.ParametroDAO;
import forex.genetic.dao.TendenciaDAO;
import forex.genetic.entities.CalculoTendencia;
import forex.genetic.entities.Estadistica;
import forex.genetic.entities.Individuo;
import forex.genetic.entities.Order;
import forex.genetic.entities.Point;
import forex.genetic.entities.Tendencia;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.exception.GeneticException;
import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.NumberUtil;
import forex.genetic.util.jdbc.JDBCUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class RetrocesosManager {

    private Connection conn = null;

    public RetrocesosManager() {
    }

    public RetrocesosManager(Connection conn) {
        this.conn = conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public void procesarMaximosRetroceso() throws ClassNotFoundException, SQLException {
        conn = JDBCUtil.getConnection();
        try {
            OperacionesManager operacionManager = new OperacionesManager(conn);
            operacionManager.procesarMaximosRetroceso(new Date());
        } finally {
            JDBCUtil.close(conn);
        }
    }
}

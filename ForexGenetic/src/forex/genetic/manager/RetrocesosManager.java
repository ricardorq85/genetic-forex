/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import forex.genetic.util.jdbc.JDBCUtil;

/**
 *
 * @author ricardorq85
 */
public class RetrocesosManager {

    private Connection conn = null;

    /**
     *
     */
    public RetrocesosManager() {
    }

    /**
     *
     * @param conn
     */
    public RetrocesosManager(Connection conn) {
        this.conn = conn;
    }

    /**
     *
     * @param conn
     */
    public void setConn(Connection conn) {
        this.conn = conn;
    }

    /**
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
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

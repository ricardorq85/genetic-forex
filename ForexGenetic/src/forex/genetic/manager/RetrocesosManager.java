/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import forex.genetic.exception.GeneticDAOException;
import forex.genetic.factory.DriverDBFactory;
import forex.genetic.manager.oracle.OracleOperacionesManager;
import forex.genetic.util.jdbc.DataClient;

/**
 *
 * @author ricardorq85
 */
public class RetrocesosManager {

    private Connection conn = null;

    private DataClient dataClient = null;
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
     * @throws GeneticDAOException 
     */
    public void procesarMaximosRetroceso() throws ClassNotFoundException, SQLException, GeneticDAOException {
        //conn = JDBCUtil.getConnection();
    	dataClient = DriverDBFactory.createDataClient("oracle");
        try {
            OperacionesManager operacionManager = new OracleOperacionesManager(dataClient);
            operacionManager.procesarMaximosRetroceso(new Date());
        } finally {
            //JDBCUtil.close(conn);
        	dataClient.close();
        }
    }
}

package forex.genetic.util.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author RROJASQ
 */
public class JDBCUtil {

    /**
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public synchronized static Connection getConnection(String url, String username, String password) 
            throws ClassNotFoundException, SQLException {
        Connection conn;
        String driver = "oracle.jdbc.OracleDriver";
        //String url = PropertiesManager.getUrlDB();
                //"jdbc:oracle:thin:@localhost:1521/FOREX2";
        //String username = PropertiesManager.getUserDB();
                //"FOREX";
        //String password = PropertiesManager.getPwdDB();
                //"forex";
        Class.forName(driver);
        conn = DriverManager.getConnection(url, username, password);
        conn.setAutoCommit(false);
        return conn;
    }

    /**
     *
     * @param rs
     */
    public synchronized static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }
    }

    /**
     *
     * @param stmt
     */
    public synchronized static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
            }
        }
    }

    /**
     *
     * @param stmt
     */
    public synchronized static void close(PreparedStatement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
            }
        }
    }

    /**
     *
     * @param conn
     */
    public synchronized static void close(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    /**
     *
     * @param conn
     */
    public synchronized static void rollback(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.rollback();
                }
            } catch (SQLException e) {
            }
        }
    }
}

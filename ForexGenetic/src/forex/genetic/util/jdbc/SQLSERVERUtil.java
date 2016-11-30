package forex.genetic.util.jdbc;

import forex.genetic.manager.PropertiesManager;
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
public class SQLSERVERUtil {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Connection c = getConnection();
		System.out.println(c.toString());
	}
    /**
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public synchronized static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn;
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String url = "jdbc:sqlserver://TEST-VPS02A.test.epm.com.co,52096;databaseName=sonar";
                //"jdbc:oracle:thin:@localhost:1521/FOREX2";
        String username = "sonar";
                //"FOREX";
        String password = "DevOps2016";
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
            	e.printStackTrace();
            }
        }
    }
}

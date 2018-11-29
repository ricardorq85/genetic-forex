package forex.genetic.util.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import forex.genetic.entities.IndividuoEstrategia;
import forex.genetic.entities.Order;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.PropertiesManager;

/**
 *
 * @author RROJASQ
 */
public class JDBCUtil {

	public synchronized static OracleDataClient getDataClient() throws GeneticDAOException {
		OracleDataClient client;
		try {
			client = new OracleDataClient(getConnection());
		} catch (ClassNotFoundException | SQLException e) {
			throw new GeneticDAOException("Error creando DataClient", e);
		}
		return client;
	}

	public synchronized static Connection getGeneticConnection() throws GeneticDAOException {
		Connection conn = null;
		try {
			String driver = "oracle.jdbc.OracleDriver";
			String url = PropertiesManager.getUrlDB();
			// "jdbc:oracle:thin:@localhost:1521/FOREX2";
			String username = PropertiesManager.getUserDB();
			String password = PropertiesManager.getPwdDB();
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			conn.setAutoCommit(false);
		} catch (ClassNotFoundException | SQLException e) {
			throw new GeneticDAOException("Error creando DataClient", e);
		}
		return conn;
	}

	/**
	 *
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public synchronized static Connection getConnection() throws ClassNotFoundException, SQLException {
		Connection conn;
		String driver = "oracle.jdbc.OracleDriver";
		String url = PropertiesManager.getUrlDB();
		// "jdbc:oracle:thin:@localhost:1521/FOREX2";
		String username = PropertiesManager.getUserDB();
		// "FOREX";
		String password = PropertiesManager.getPwdDB();
		// "forex";
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

	public static void refreshMaterializedView(Connection conn, String viewName) throws GeneticDAOException {
		refreshMaterializedView(conn, viewName, "F");
	}

	public static void refreshMaterializedView(Connection conn, String viewName, String refreshType)
			throws GeneticDAOException {
		CallableStatement cstmt = null;
		try {
			cstmt = conn.prepareCall("{call DBMS_SNAPSHOT.REFRESH(?,?)}");
			cstmt.setString(1, viewName);
			cstmt.setString(2, refreshType);
			cstmt.execute();
		} catch (SQLException e) {
			throw new GeneticDAOException(null, e);
		} finally {
			JDBCUtil.close(cstmt);
		}
	}

	public static void refreshMaterializedViews(Connection conn, String[] vistas) throws GeneticDAOException {
		for (String viewName : vistas) {
			JDBCUtil.refreshMaterializedView(conn, viewName, "F");
		}
	}
}

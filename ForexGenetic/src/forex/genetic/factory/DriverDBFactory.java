package forex.genetic.factory;

import java.sql.Connection;
import java.sql.SQLException;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.GeneticDAO;
import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

public class DriverDBFactory {

	private static String drivers[] = new String[] { "oracle", "mongodb" };

	public static void registerDriver(String oneDriver) {
		drivers = new String[] { oneDriver };
	}

	public static GeneticDAO<?>[] createDAO(String entidad) {
		GeneticDAO<?>[] daos = new GeneticDAO[drivers.length];
		for (int i = 0; i < drivers.length; i++) {
			if ("oracle".equals(drivers[i])) {
				Connection conn;
				try {
					conn = JDBCUtil.getConnection();
					if ("datoHistorico".equals(entidad)) {
						daos[i] = new DatoHistoricoDAO(conn);
					}					
				} catch (ClassNotFoundException | SQLException e) {
					LogUtil.logTime("Error al crear conexion con BD Oracle. Se continua el proceso con drivers.", 1);
					e.printStackTrace();
				}
			} else if ("oracle".equals(drivers[i])) {
				if ("datoHistorico".equals(entidad)) {
					daos[i] = new MongoDatoHistoricoDAO();
				}
			}
		}
		return daos;
	}
}

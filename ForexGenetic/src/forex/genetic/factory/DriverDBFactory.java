package forex.genetic.factory;

import java.sql.Connection;
import java.sql.SQLException;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.GeneticDAO;
import forex.genetic.dao.mongodb.MongoDatoHistoricoDAO;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

public class DriverDBFactory extends GeneticFactory {

	public static GeneticDAO<? extends Object>[] createDAO(String entidad) {
		GeneticDAO<?>[] daos = new GeneticDAO[drivers.length];
		for (int i = 0; i < drivers.length; i++) {
			if ("oracle".equals(drivers[i])) {
				try {
					createOracleDAO(entidad);
				} catch (ClassNotFoundException | SQLException e) {
					LogUtil.logTime("Error al crear conexion con BD Oracle. Se continua el proceso con drivers.", 1);
					e.printStackTrace();
				}
			} else if ("mongodb".equals(drivers[i])) {
				createMongoDAO(entidad);
			}
		}
		return daos;
	}

	private static GeneticDAO<? extends Object> createOracleDAO(String entidad)
			throws ClassNotFoundException, SQLException {
		GeneticDAO<? extends Object> dao = null;
		Connection conn = JDBCUtil.getConnection();
		if ("datoHistorico".equals(entidad)) {
			dao = new DatoHistoricoDAO(conn);
		}
		return dao;
	}

	private static GeneticDAO<? extends Object> createMongoDAO(String entidad) {
		GeneticDAO<? extends Object> dao = null;
		if ("datoHistorico".equals(entidad)) {
			dao = new MongoDatoHistoricoDAO();
		}
		return dao;
	}
}

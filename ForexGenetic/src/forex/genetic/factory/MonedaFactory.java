package forex.genetic.factory;

import java.sql.Connection;
import java.sql.SQLException;

import forex.genetic.dao.ParametroDAO;
import forex.genetic.entities.Moneda;
import forex.genetic.entities.MonedaEURJPY;
import forex.genetic.entities.MonedaUSDCAD;
import forex.genetic.util.jdbc.JDBCUtil;

public class MonedaFactory {

	private static Connection conn;
	private static ParametroDAO parametroDAO;
	private static boolean consultado = false;
	private static Moneda moneda;

	static {
		try {
			conn = JDBCUtil.getConnection();
			parametroDAO = new ParametroDAO(conn);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static Moneda getMoneda() {
		if (!consultado) {
			String nombreParametro = "MONEDA";
			String m;
			try {
				m = parametroDAO.getValorParametro(nombreParametro);
			} catch (SQLException e) {
				m = null;
				e.printStackTrace();
			}
			if ((m == null) || ("USDCAD".equals(m))) {
				moneda = new MonedaUSDCAD();
			} else if ("EURJPY".equals(m)) {
				moneda = new MonedaEURJPY();
			}
			consultado = true;
		}
		return moneda;
	}

}

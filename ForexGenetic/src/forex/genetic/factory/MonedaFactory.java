package forex.genetic.factory;

import java.sql.Connection;
import java.sql.SQLException;

import forex.genetic.dao.oracle.OracleParametroDAO;
import forex.genetic.entities.Moneda;
import forex.genetic.entities.MonedaEURJPY;
import forex.genetic.entities.MonedaUSDCAD;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.jdbc.JDBCUtil;

public class MonedaFactory {

//	private static Connection conn;
//	private static OracleParametroDAO parametroDAO;
	private static boolean consultado = false;
	private static Moneda moneda;

//	static {
//		try {
//			conn = JDBCUtil.getConnection();
//			parametroDAO = new OracleParametroDAO(conn);
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		}
//	}

	public static Moneda getMoneda() {
		if (!consultado) {
			String nombreParametro = "MONEDA";
			String m = "USDCAD";
//			if (parametroDAO != null) {
//				try {
//					m = parametroDAO.getValorParametro(nombreParametro);
//				} catch (GeneticDAOException e) {
//					m = null;
//					e.printStackTrace();
//				}
//			}
			moneda = getMoneda(m);
			consultado = true;
		}
		return moneda;
	}
	
	public static Moneda getMoneda(String m) {
		Moneda moneda = null;
		if ((m == null) || ("USDCAD".equals(m))) {
			moneda = new MonedaUSDCAD();
		} else if ("EURJPY".equals(m)) {
			moneda = new MonedaEURJPY();
		}
		return moneda;		
	}

}

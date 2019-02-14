package forex.genetic.factory;

import forex.genetic.dao.IParametroDAO;
import forex.genetic.entities.Moneda;
import forex.genetic.entities.MonedaEURJPY;
import forex.genetic.entities.MonedaGBPCHF;
import forex.genetic.entities.MonedaUSDCAD;
import forex.genetic.manager.PropertiesManager;

public class MonedaFactory {

//	private static Connection conn;
	private static IParametroDAO parametroDAO;
	private static boolean consultado = false;
	private static Moneda moneda;

//	static {
//		try {
//			parametroDAO = DriverDBFactory.createDataClients().get(0).getDaoParametro();
//		} catch (GeneticDAOException e) {
//			e.printStackTrace();
//		}
//			conn = JDBCUtil.getConnection();
		// parametroDAO = new OracleParametroDAO(conn);
//	}

	public static Moneda getMoneda() {
		if (!consultado) {
//			String nombreParametro = "MONEDA";
			String m = PropertiesManager.getPair();
//			if (parametroDAO != null) {
//				try {
//					m = DriverDBFactory.createDataClients().get(0).getDaoParametro().getValorParametro(nombreParametro);
//				} catch (GeneticDAOException e) {
//					m = "USDCAD";
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
		} else if ("GBPCHF".equals(m)) {
			moneda = new MonedaGBPCHF();
		}
		return moneda;
	}

}

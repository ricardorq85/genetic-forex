package forex.genetic.tendencia.manager;

import java.sql.Connection;
import java.sql.SQLException;

import forex.genetic.dao.TendenciaProcesoFiltroFechaTendenciaFechaCierreDAO;
import forex.genetic.util.jdbc.JDBCUtil;

public class ExportarTendenciaFiltroFechaTendenciaFechaCierreManager extends ExportarTendenciaManager {

	public ExportarTendenciaFiltroFechaTendenciaFechaCierreManager() throws ClassNotFoundException, SQLException {
		super(JDBCUtil.getConnection());
	}

	public ExportarTendenciaFiltroFechaTendenciaFechaCierreManager(Connection c) throws ClassNotFoundException, SQLException {
		super();
		super.dao = new TendenciaProcesoFiltroFechaTendenciaFechaCierreDAO(c);
	}
}

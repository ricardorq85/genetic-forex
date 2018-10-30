package forex.genetic.tendencia.manager;

import java.sql.Connection;
import java.sql.SQLException;

import forex.genetic.dao.TendenciaProcesoFiltroFechaTendenciaFechaBaseDAO;
import forex.genetic.util.jdbc.JDBCUtil;

public class ExportarTendenciaFiltroFechaTendenciaFechaBaseManager extends ExportarTendenciaManager {
	public ExportarTendenciaFiltroFechaTendenciaFechaBaseManager() throws ClassNotFoundException, SQLException {
		super(JDBCUtil.getConnection());
	}

	public ExportarTendenciaFiltroFechaTendenciaFechaBaseManager(Connection c) {
		super(c);
		super.tendenciaProcesoDAO = new TendenciaProcesoFiltroFechaTendenciaFechaBaseDAO(c);
	}

	@Override
	protected void procesarRegresion() throws SQLException {
		super.procesarRegresion();
	}

}

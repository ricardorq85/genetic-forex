package forex.genetic.tendencia.manager;

import java.sql.Connection;
import java.sql.SQLException;

import forex.genetic.dao.TendenciaProcesoFiltroFechaTendenciaFechaBaseDAO;
import forex.genetic.util.jdbc.JDBCUtil;

public class ExportarTendenciaMaximosMinimosManager extends ExportarTendenciaManager {

	public ExportarTendenciaMaximosMinimosManager() throws ClassNotFoundException, SQLException {
		super(JDBCUtil.getConnection());
	}

	public ExportarTendenciaMaximosMinimosManager(Connection c) throws ClassNotFoundException, SQLException {
		super(c);
		super.dao = new TendenciaProcesoFiltroFechaTendenciaFechaBaseDAO(c);
	}

}

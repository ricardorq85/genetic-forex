package forex.genetic.factory;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

import forex.genetic.dao.oracle.OracleParametroDAO;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.tendencia.manager.ProcesarTendenciasBuySellManager;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

public final class ProcesarTendenciasFactory {

	public static ProcesarTendenciasBuySellManager createManager() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, GeneticDAOException, SQLException {
		Connection conn = JDBCUtil.getConnection();
		OracleParametroDAO parametroDAO = new OracleParametroDAO(conn);
		String parametroTipoExportacion = parametroDAO.getValorParametro("TIPO_EXPORTACION_TENDENCIA");
		LogUtil.logTime(parametroTipoExportacion, 1);
		ProcesarTendenciasBuySellManager manager = create(parametroTipoExportacion);
		return manager;
	}

	private static final ProcesarTendenciasBuySellManager create(String tipoExportacion) throws ClassNotFoundException,
			NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Class<?> exporterClass = Class.forName(tipoExportacion);
		// Constructor<?> exporterConstructor =
		// exporterClass.getConstructor(java.sql.Connection.class);
		// ExportarTendenciaManager exporter = (ExportarTendenciaManager)
		// exporterConstructor.newInstance(this.conn);
		return (ProcesarTendenciasBuySellManager) exporterClass.newInstance();
	}
}

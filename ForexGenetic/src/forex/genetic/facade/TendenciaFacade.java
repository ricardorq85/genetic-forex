package forex.genetic.facade;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.ParametroDAO;
import forex.genetic.entities.TendenciaEstadistica;
import forex.genetic.tendencia.manager.TendenciaBuySellManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

public class TendenciaFacade implements IGeneticFacade {

	private ParametroDAO parametroDAO;
	protected Connection conn = null;

	private Date parametroFechaInicio;
	private int parametroStepTendencia, parametroFilasTendencia;

	public TendenciaFacade() throws ClassNotFoundException, SQLException {
		conn = JDBCUtil.getConnection();
		parametroDAO = new ParametroDAO(conn);
		parametroFechaInicio = parametroDAO.getDateValorParametro("FECHA_INICIO_TENDENCIA");
		parametroStepTendencia = parametroDAO.getIntValorParametro("STEP_TENDENCIA");
		parametroFilasTendencia = parametroDAO.getIntValorParametro("INDIVIDUOS_X_TENDENCIA");
	}

	public void procesarTendencias() throws ClassNotFoundException, SQLException {
		Date fechaBase = parametroFechaInicio;
		TendenciaBuySellManager tendenciaManager = new TendenciaBuySellManager();
		while (fechaBase.after(DateUtil.adicionarDias(fechaBase, -30))) {
			LogUtil.logTime("Fecha base=" + DateUtil.getDateString(fechaBase), 1);
			List<TendenciaEstadistica> tendencias = tendenciaManager.calcularTendencias(fechaBase, parametroFilasTendencia);
			fechaBase = DateUtil.adicionarMinutos(fechaBase, -parametroStepTendencia);
		}
	}
}

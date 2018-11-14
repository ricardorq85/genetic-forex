package forex.genetic.facade;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.ParametroDAO;
import forex.genetic.dao.oracle.OracleTendenciaDAO;
import forex.genetic.tendencia.manager.TendenciaBuySellManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

public class TendenciaFacade implements IGeneticFacade {

	private TendenciaBuySellManager tendenciaManager;
	private ParametroDAO parametroDAO;
	private OracleTendenciaDAO tendenciaDAO;
	protected Connection conn = null;
	private List<Date> fechasXCantidad;
	private Date parametroFechaInicio;
	private int parametroStepTendencia, parametroFilasTendencia, parametroMesesTendencia, parametroNumXCantidad;

	public TendenciaFacade() throws ClassNotFoundException, SQLException {
		conn = JDBCUtil.getConnection();
		tendenciaManager = new TendenciaBuySellManager();
		parametroDAO = new ParametroDAO(conn);
		tendenciaDAO = new OracleTendenciaDAO(conn);
		parametroFechaInicio = parametroDAO.getDateValorParametro("FECHA_INICIO_TENDENCIA");
		parametroStepTendencia = parametroDAO.getIntValorParametro("STEP_TENDENCIA");
		parametroFilasTendencia = parametroDAO.getIntValorParametro("INDIVIDUOS_X_TENDENCIA");
		try {
			parametroMesesTendencia = parametroDAO.getIntValorParametro("MESES_TENDENCIA");
		} catch (NumberFormatException ex) {
			parametroMesesTendencia = 0;
		}
		if (parametroMesesTendencia > 0) {
			fechasXCantidad = tendenciaDAO.consultarXCantidadFechaBase(parametroFechaInicio, parametroMesesTendencia);
			parametroNumXCantidad = parametroDAO.getIntValorParametro("NUM_TENDENCIA_X_CANTIDAD");
		}
	}

	public void procesarTendencias() throws ClassNotFoundException, SQLException {
		this.tendenciaManager.calcularTendencias(parametroFechaInicio, parametroFilasTendencia);
		if (parametroMesesTendencia > 0) {
			this.procesarTendenciasXCantidad();
		}
		this.procesarTendenciasXFecha();
	}

	private void procesarTendenciasXCantidad() throws ClassNotFoundException, SQLException {
		Date fechaBaseFinal = parametroFechaInicio;
		int minutosUnDia = 1 * 24 * 60;
		for (int i = 0; i < parametroNumXCantidad && i < fechasXCantidad.size(); i++) {
			Date fechaBaseInicial = fechasXCantidad.get(i);
			fechaBaseFinal = DateUtil.adicionarMinutos(fechaBaseInicial, minutosUnDia);
			// LogUtil.logEnter(1);
			LogUtil.logTime("Fecha base inicial=" + DateUtil.getDateString(fechaBaseInicial) + ", Fecha base final="
					+ DateUtil.getDateString(fechaBaseFinal), 1);
			tendenciaManager.calcularTendencias(2, fechaBaseInicial, fechaBaseFinal, parametroFilasTendencia);
		}
	}

	private void procesarTendenciasXFecha() throws ClassNotFoundException, SQLException {
		Date fechaBaseFinal = parametroFechaInicio;
		TendenciaBuySellManager tendenciaManager = new TendenciaBuySellManager();
		while (fechaBaseFinal.after(DateUtil.adicionarDias(fechaBaseFinal, -30))) {
			fechaBaseFinal = DateUtil.adicionarMinutos(fechaBaseFinal, -1);
			Date fechaBaseInicial = DateUtil.adicionarMinutos(fechaBaseFinal, -parametroStepTendencia);
			// LogUtil.logEnter(1);
			LogUtil.logTime("Fecha base inicial=" + DateUtil.getDateString(fechaBaseInicial) + ", Fecha base final="
					+ DateUtil.getDateString(fechaBaseFinal), 1);
			tendenciaManager.calcularTendencias(fechaBaseInicial, fechaBaseFinal, parametroFilasTendencia);
			fechaBaseFinal = fechaBaseInicial;
		}
	}
}

package forex.genetic.facade;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.ParametroDAO;
import forex.genetic.dao.TendenciaDAO;
import forex.genetic.tendencia.manager.TendenciaBuySellManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.LogUtil;
import forex.genetic.util.jdbc.JDBCUtil;

public class TendenciaFacade implements IGeneticFacade {

	private ParametroDAO parametroDAO;
	private TendenciaDAO tendenciaDAO;
	protected Connection conn = null;
	private List<Date> fechasXCantidad;
	private Date parametroFechaInicio;
	private int parametroStepTendencia, parametroFilasTendencia;

	public TendenciaFacade() throws ClassNotFoundException, SQLException {
		conn = JDBCUtil.getConnection();
		parametroDAO = new ParametroDAO(conn);
		tendenciaDAO = new TendenciaDAO(conn);				
		parametroFechaInicio = parametroDAO.getDateValorParametro("FECHA_INICIO_TENDENCIA");
		parametroStepTendencia = parametroDAO.getIntValorParametro("STEP_TENDENCIA");
		parametroFilasTendencia = parametroDAO.getIntValorParametro("INDIVIDUOS_X_TENDENCIA");
		fechasXCantidad = tendenciaDAO.consultarXCantidadFechaBase(parametroFechaInicio);
	}

	public void procesarTendencias() throws ClassNotFoundException, SQLException {
		Date fechaBaseFinal = parametroFechaInicio;
		TendenciaBuySellManager tendenciaManager = new TendenciaBuySellManager();
		tendenciaManager.calcularTendencias(fechaBaseFinal, parametroFilasTendencia);
		int minutosUnDia = 1 * 24 * 60;
		for (Date fecha : fechasXCantidad) {
			Date fechaBaseInicial = fecha;
			fechaBaseFinal = DateUtil.adicionarMinutos(fechaBaseInicial, minutosUnDia);
			LogUtil.logTime("Fecha base inicial=" + DateUtil.getDateString(fechaBaseInicial) + ", Fecha base final="
					+ DateUtil.getDateString(fechaBaseFinal), 1);
			tendenciaManager.calcularTendencias(fechaBaseInicial, fechaBaseFinal, parametroFilasTendencia);
		}
	}

	public void procesarTendencias2() throws ClassNotFoundException, SQLException {
		Date fechaBaseFinal = parametroFechaInicio;
		TendenciaBuySellManager tendenciaManager = new TendenciaBuySellManager();
		tendenciaManager.calcularTendencias(fechaBaseFinal, parametroFilasTendencia);
		while (fechaBaseFinal.after(DateUtil.adicionarDias(fechaBaseFinal, -30))) {
			fechaBaseFinal = DateUtil.adicionarMinutos(fechaBaseFinal, -1);
			Date fechaBaseInicial = DateUtil.adicionarMinutos(fechaBaseFinal, -parametroStepTendencia);
			LogUtil.logTime("Fecha base inicial=" + DateUtil.getDateString(fechaBaseInicial) + ", Fecha base final="
					+ DateUtil.getDateString(fechaBaseFinal), 1);
			tendenciaManager.calcularTendencias(fechaBaseInicial, fechaBaseFinal, parametroFilasTendencia);
			fechaBaseFinal = fechaBaseInicial;
		}
	}
}

package forex.genetic.tendencia.manager;

import java.sql.Connection;
import java.sql.SQLException;

import forex.genetic.dao.TendenciaProcesoFiltradaDAO;
import forex.genetic.entities.Regresion;
import forex.genetic.util.Constants.OperationType;
import forex.genetic.util.jdbc.JDBCUtil;

public class ExportarTendenciaFiltradaManager extends ExportarTendenciaManager {

	private static final double MIN_R2 = 0.2D;
	private static final double MAX_R2 = 1.1D;
	private static final double MIN_PENDIENTE = 0.001;
	private static final double MAX_PENDIENTE = 1.1D;
	private static final double MIN_PORCENTAJE_CANTIDAD_REGRESION = 0.5D;
	private static final double MAX_DESVIACION = 10000.0D;

	public ExportarTendenciaFiltradaManager() throws ClassNotFoundException, SQLException {
		super(JDBCUtil.getConnection());
	}

	public ExportarTendenciaFiltradaManager(Connection c) {
		super(c);
		super.dao = new TendenciaProcesoFiltradaDAO(c);
	}

	protected void procesarRegresion(Regresion regresion, Regresion regresionFiltrada) throws SQLException {
		if ((regresion.getPendiente() * regresionFiltrada.getPendiente()) > 0) {
			if (regresion.isRegresionValida() && regresionFiltrada.isRegresionValida()) {
				procesoTendencia.setRegresion(regresionFiltrada);
				if (regresionFiltrada.getPendiente() < 0) {
					procesoTendencia.setTipoOperacion(OperationType.SELL);
				} else if (regresionFiltrada.getPendiente() > 0) {
					procesoTendencia.setTipoOperacion(OperationType.BUY);
				}
			}
		}
	}

	@Override
	protected void procesarRegresion() throws SQLException {
		Regresion regresion = dao.consultarRegresion(procesoTendencia);
		this.setParametrosRegresion(regresion);
		String sqlRegresion = "SELECT PARAM.PERIODO PERIODO, REG.*  FROM PARAMETROS PARAM, REGRESION_FILTRADA REG";
		Regresion regresionFiltrada = dao.consultarRegresion(procesoTendencia, sqlRegresion);
		this.setParametrosRegresion(regresionFiltrada);
		this.procesarRegresion(regresion, regresionFiltrada);
	}

	@Override
	protected void setParametrosRegresion(Regresion regresion) {
		if (regresion != null) {
			regresion.setMinimoR2(MIN_R2);
			regresion.setMaximoR2(MAX_R2);
			regresion.setMinimoPendiente(MIN_PENDIENTE);
			regresion.setMaximoPendiente(MAX_PENDIENTE);
			regresion.setMinimoPorcetnajeCantidadRegresion(MIN_PORCENTAJE_CANTIDAD_REGRESION);
			regresion.setMaximoDesviacion(MAX_DESVIACION);
		}
	}

}

package forex.genetic.tendencia.manager;

import java.sql.Connection;
import java.sql.SQLException;

import forex.genetic.dao.oracle.OracleTendenciaProcesoFiltradaDAO;
import forex.genetic.entities.Regresion;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.tendencia.manager.oracle.OracleExportarTendenciaManager;
import forex.genetic.util.Constants.OperationType;

public class ExportarTendenciaFiltradaManager extends OracleExportarTendenciaManager {

	private static final double MIN_R2 = 0.2D;
	private static final double MAX_R2 = 1.1D;
	private static final double MIN_PENDIENTE = 0.001;
	private static final double MAX_PENDIENTE = 1.1D;
	private static final double MIN_PORCENTAJE_CANTIDAD_REGRESION = 0.5D;
	private static final double MAX_DESVIACION = 10000.0D;

	public ExportarTendenciaFiltradaManager() throws GeneticBusinessException {
		this(null);
	}

	public ExportarTendenciaFiltradaManager(Connection c) throws GeneticBusinessException {
		super(c);
		super.tendenciaProcesoDAO = new OracleTendenciaProcesoFiltradaDAO(c);
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
	protected void procesarRegresion() throws GeneticBusinessException {
		try {
			Regresion regresion;
			regresion = tendenciaProcesoDAO.consultarRegresion(procesoTendencia);
			this.setParametrosRegresion(regresion);
			String sqlRegresion = "SELECT PARAM.PERIODO PERIODO, REG.*  FROM PARAMETROS PARAM, REGRESION_FILTRADA REG";
			Regresion regresionFiltrada = tendenciaProcesoDAO.consultarRegresion(procesoTendencia, sqlRegresion);
			this.setParametrosRegresion(regresionFiltrada);
			this.procesarRegresion(regresion, regresionFiltrada);
		} catch (SQLException e) {
			throw new GeneticBusinessException(null, e);
		}
	}

	@Override
	protected void setParametrosRegresion(Regresion regresion) {
		if (regresion != null) {
			regresion.setMinimoR2(MIN_R2);
			regresion.setMaximoR2(MAX_R2);
			regresion.setMinimoPendiente(MIN_PENDIENTE);
			regresion.setMaximoPendiente(MAX_PENDIENTE);
			regresion.setMinimoPorcentajeCantidadRegresion(MIN_PORCENTAJE_CANTIDAD_REGRESION);
			regresion.setMaximoDesviacion(MAX_DESVIACION);
		}
	}

}

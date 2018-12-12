package forex.genetic.tendencia.manager.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.oracle.OracleTendenciaProcesoBuySellDAO;
import forex.genetic.dao.oracle.OracleTendenciaProcesoFiltradaDAO;
import forex.genetic.dao.oracle.OracleTendenciaProcesoFiltradaUltimosDatosDAO;
import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.tendencia.manager.ExportarTendenciaGrupalManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.DataClient;

public class OracleExportarTendenciaGrupalManager extends ExportarTendenciaGrupalManager {

	private OracleTendenciaProcesoBuySellDAO tendenciaProcesoDAO;
	private OracleTendenciaProcesoBuySellDAO tendenciaProcesoCompletaDAO;

	public OracleExportarTendenciaGrupalManager(DataClient dc) {
		this(dc, null);
	}

	public OracleExportarTendenciaGrupalManager(DataClient dc, Date fechaBase) {
		super(dc);
		if (DateUtil.cumpleFechaParaTendenciaUltimosDatos(fechaBase)) {
			this.tendenciaProcesoDAO = new OracleTendenciaProcesoFiltradaUltimosDatosDAO((Connection) dc.getClient());
			this.tendenciaProcesoCompletaDAO = new OracleTendenciaProcesoFiltradaUltimosDatosDAO(
					(Connection) dc.getClient()) {
				@Override
				protected String getTablaTendenciaFiltrada() {
					return "TENDENCIA_CALCULADA";
				}
			};
		} else {
			this.tendenciaProcesoDAO = new OracleTendenciaProcesoFiltradaDAO((Connection) dc.getClient());
			this.tendenciaProcesoCompletaDAO = new OracleTendenciaProcesoFiltradaDAO((Connection) dc.getClient()) {
				@Override
				protected String getTablaTendenciaFiltrada() {
					return "TENDENCIA_CALCULADA";
				}
			};
		}
	}

	@Override
	protected void procesarRegresion() throws GeneticBusinessException {
		Regresion regresion;
		try {
			regresion = tendenciaProcesoDAO.consultarRegresion(procesoTendencia);
			this.setParametrosRegresion(regresion);
			String sqlRegresion = "SELECT PARAM.PERIODO PERIODO, PRITEN.PRECIO_CALCULADO PRIMERA_TENDENCIA, REG.*  FROM PARAMETROS PARAM, REGRESION_FILTRADA REG"
					+ " LEFT JOIN PRIMERA_TENDENCIA PRITEN ON 1=1";
			Regresion regresionFiltrada = tendenciaProcesoDAO.consultarRegresion(procesoTendencia, sqlRegresion);
			this.setParametrosRegresion(regresionFiltrada);
			this.procesarRegresion(regresion, regresionFiltrada);

			this.procesarRegresionParaCalculoJava();
		} catch (SQLException e) {
			throw new GeneticBusinessException(null, e);
		}
	}

	@Override
	protected List<TendenciaParaOperar> consultarTendenciasSinFiltrar() throws GeneticBusinessException {
		try {
			return this.tendenciaProcesoCompletaDAO.consultarTendencias(procesoTendencia);
		} catch (SQLException e) {
			throw new GeneticBusinessException(null, e);
		}
	}

	@Override
	protected List<TendenciaParaOperar> consultarTendenciasFiltradas() throws GeneticBusinessException {
		return consultarTendencias();
	}

	@Override
	protected List<TendenciaParaOperar> consultarTendencias() throws GeneticBusinessException {
		List<TendenciaParaOperar> tendencias;
		try {
			tendencias = tendenciaProcesoDAO.consultarTendencias(procesoTendencia);
		} catch (SQLException e) {
			throw new GeneticBusinessException(e);
		}
		return tendencias;
	}
}

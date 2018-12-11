package forex.genetic.tendencia.manager.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.oracle.OracleDatoHistoricoDAO;
import forex.genetic.dao.oracle.OracleTendenciaProcesoBuySellDAO;
import forex.genetic.dao.oracle.OracleTendenciaProcesoFiltradaDAO;
import forex.genetic.dao.oracle.OracleTendenciaProcesoFiltradaUltimosDatosDAO;
import forex.genetic.entities.Point;
import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.tendencia.manager.ExportarTendenciaGrupalManager;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.JDBCUtil;

public class OracleExportarTendenciaGrupalManager extends ExportarTendenciaGrupalManager {

	private Connection conn = null;
	private OracleTendenciaProcesoBuySellDAO tendenciaProcesoDAO;
	private OracleTendenciaProcesoBuySellDAO tendenciaProcesoCompletaDAO;

	public OracleExportarTendenciaGrupalManager() throws ClassNotFoundException, SQLException {
		this(JDBCUtil.getConnection(), null);
	}

	public OracleExportarTendenciaGrupalManager(Connection c, Date fechaBase) {
		super();
		if (DateUtil.cumpleFechaParaTendenciaUltimosDatos(fechaBase)) {
			this.tendenciaProcesoDAO = new OracleTendenciaProcesoFiltradaUltimosDatosDAO(c);
			this.tendenciaProcesoCompletaDAO = new OracleTendenciaProcesoFiltradaUltimosDatosDAO(c) {
				@Override
				protected String getTablaTendenciaFiltrada() {
					return "TENDENCIA_CALCULADA";
				}
			};
		} else {
			this.tendenciaProcesoDAO = new OracleTendenciaProcesoFiltradaDAO(c);
			this.tendenciaProcesoCompletaDAO = new OracleTendenciaProcesoFiltradaDAO(c) {
				@Override
				protected String getTablaTendenciaFiltrada() {
					return "TENDENCIA_CALCULADA";
				}
			};
		}
	}

	@Override
	protected void procesarRegresion() throws GeneticDAOException {
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
			throw new GeneticDAOException(null, e);
		}
	}

	@Override
	protected List<TendenciaParaOperar> consultarTendenciasSinFiltrar() throws GeneticDAOException {
		try {
			return this.tendenciaProcesoCompletaDAO.consultarTendencias(procesoTendencia);
		} catch (SQLException e) {
			throw new GeneticDAOException(null, e);
		}
	}

	@Override
	protected List<TendenciaParaOperar> consultarTendenciasFiltradas() throws GeneticDAOException {
		return consultarTendencias();
	}

	@Override
	protected List<TendenciaParaOperar> consultarTendencias() throws GeneticDAOException {
		List<TendenciaParaOperar> tendencias;
		try {
			tendencias = tendenciaProcesoDAO.consultarTendencias(procesoTendencia);
		} catch (SQLException e) {
			throw new GeneticDAOException(null, e);
		}
		return tendencias;
	}

	@Override
	protected void calcularPuntosDiferenciaInicial(List<TendenciaParaOperar> tendencias) throws GeneticDAOException {
		TendenciaParaOperar op = tendencias.get(0);
		OracleDatoHistoricoDAO datoHistoricoDAO = new OracleDatoHistoricoDAO(conn);
		Date fechaConsultaHistorico = datoHistoricoDAO.getFechaHistoricaMaxima(procesoTendencia.getFechaBase());
		List<Point> historico = datoHistoricoDAO.consultarHistorico(fechaConsultaHistorico, fechaConsultaHistorico);
		Point point = null;
		if ((historico != null) && (!historico.isEmpty())) {
			point = historico.get(0);
		}
		double precioHistorico = (point.getClose() + point.getHigh() + point.getLow() + point.getOpen()) / 4;
		double precioCalculado = op.getPrecioCalculado();
		double diff = precioHistorico - precioCalculado;
		procesoTendencia.setPuntosDiferenciaInicial(diff);
	}
}

package forex.genetic.tendencia.manager.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.oracle.OracleDatoHistoricoDAO;
import forex.genetic.dao.oracle.OracleTendenciaProcesoBuySellDAO;
import forex.genetic.entities.Point;
import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.tendencia.manager.ExportarTendenciaManager;
import forex.genetic.util.jdbc.JDBCUtil;

public class OracleExportarTendenciaManager extends ExportarTendenciaManager {

	private Connection conn = null;
	protected OracleTendenciaProcesoBuySellDAO tendenciaProcesoDAO;

	public OracleExportarTendenciaManager() throws GeneticBusinessException {
		this(null);
	}

	public OracleExportarTendenciaManager(Connection c) throws GeneticBusinessException {
		super();
		if (c != null) {
			this.conn = c;
		} else {
			try {
				this.conn = JDBCUtil.getConnection();
			} catch (ClassNotFoundException | SQLException e) {
				throw new GeneticBusinessException(null, e);
			}
		}
	}

	protected List<TendenciaParaOperar> consultarTendencias() throws GeneticBusinessException {
		List<TendenciaParaOperar> tendencias;
		try {
			tendencias = tendenciaProcesoDAO.consultarTendencias(procesoTendencia);
		} catch (SQLException e) {
			throw new GeneticBusinessException(null, e);
		}
		return tendencias;
	}

	protected void calcularPuntosDiferenciaInicial(List<TendenciaParaOperar> tendencias)
			throws GeneticBusinessException {
		try {
			TendenciaParaOperar op = tendencias.get(0);
			OracleDatoHistoricoDAO datoHistoricoDAO = new OracleDatoHistoricoDAO(conn);
			Date fechaConsultaHistorico;
			fechaConsultaHistorico = datoHistoricoDAO.getFechaHistoricaMaxima(procesoTendencia.getFechaBase());
			List<Point> historico = datoHistoricoDAO.consultarHistorico(fechaConsultaHistorico, fechaConsultaHistorico);
			Point point = null;
			if ((historico != null) && (!historico.isEmpty())) {
				point = historico.get(0);
			}
			double precioHistorico = (point.getClose() + point.getHigh() + point.getLow() + point.getOpen()) / 4;
			double precioCalculado = op.getPrecioCalculado();
			double diff = precioHistorico - precioCalculado;
			procesoTendencia.setPuntosDiferenciaInicial(diff);
		} catch (GeneticDAOException e) {
			throw new GeneticBusinessException(e);
		}
	}

	protected void procesarRegresion() throws GeneticBusinessException {
		Regresion regresion;
		try {
			regresion = tendenciaProcesoDAO.consultarRegresion(procesoTendencia);
			procesarRegresion(regresion);
		} catch (SQLException e) {
			throw new GeneticBusinessException(null, e);
		}
	}
}

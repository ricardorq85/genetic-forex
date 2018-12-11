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
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.tendencia.manager.ExportarTendenciaManager;
import forex.genetic.util.jdbc.JDBCUtil;

public class OracleExportarTendenciaManager extends ExportarTendenciaManager {

	private Connection conn = null;
	protected OracleTendenciaProcesoBuySellDAO tendenciaProcesoDAO;

	public OracleExportarTendenciaManager() throws ClassNotFoundException, SQLException {
		this(JDBCUtil.getConnection());
	}

	public OracleExportarTendenciaManager(Connection c) {
		super();
		this.conn = c;
	}

	protected List<TendenciaParaOperar> consultarTendencias() throws GeneticDAOException {
		List<TendenciaParaOperar> tendencias;
		try {
			tendencias = tendenciaProcesoDAO.consultarTendencias(procesoTendencia);
		} catch (SQLException e) {
			throw new GeneticDAOException(null, e);
		}
		return tendencias;
	}

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

	protected void procesarRegresion() throws GeneticDAOException {
		Regresion regresion;
		try {
			regresion = tendenciaProcesoDAO.consultarRegresion(procesoTendencia);
		} catch (SQLException e) {
			throw new GeneticDAOException(null, e);
		}
		procesarRegresion(regresion);
	}
}

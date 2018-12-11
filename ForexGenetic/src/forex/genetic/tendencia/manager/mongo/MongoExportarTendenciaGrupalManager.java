package forex.genetic.tendencia.manager.mongo;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.mongodb.MongoTendenciaDAO;
import forex.genetic.dao.mongodb.MongoTendenciaUltimosDatosDAO;
import forex.genetic.dao.oracle.OracleDatoHistoricoDAO;
import forex.genetic.entities.Point;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.tendencia.manager.ExportarTendenciaGrupalManager;
import forex.genetic.util.DateUtil;

public class MongoExportarTendenciaGrupalManager extends ExportarTendenciaGrupalManager {

	private MongoTendenciaDAO tendenciaProcesoUltimosDatosDAO;
	private MongoTendenciaDAO tendenciaProcesoCompletaDAO;

	public MongoExportarTendenciaGrupalManager() {
		this(null);
	}

	public MongoExportarTendenciaGrupalManager(Date fechaBase) {
		super();
		if (DateUtil.cumpleFechaParaTendenciaUltimosDatos(fechaBase)) {
			this.tendenciaProcesoUltimosDatosDAO = new MongoTendenciaUltimosDatosDAO();
			this.tendenciaProcesoCompletaDAO = new MongoTendenciaDAO();
		} else {
			// TODO rrojasq Hacer filtrada
			this.tendenciaProcesoUltimosDatosDAO = new MongoTendenciaUltimosDatosDAO();
			this.tendenciaProcesoCompletaDAO = new MongoTendenciaDAO();
		}
	}

	@Override
	protected void procesarRegresion() throws GeneticDAOException {
		this.procesarRegresionParaCalculoJava();
	}

	@Override
	protected List<TendenciaParaOperar> consultarTendenciasSinFiltrar() throws GeneticDAOException {
		return this.tendenciaProcesoCompletaDAO.consultarTendencias(procesoTendencia);
	}

	@Override
	protected List<TendenciaParaOperar> consultarTendenciasFiltradas() throws GeneticDAOException {
		return this.tendenciaProcesoUltimosDatosDAO.consultarTendencias(procesoTendencia);
	}

	@Override
	protected List<TendenciaParaOperar> consultarTendencias() throws GeneticDAOException {
		return tendenciaProcesoUltimosDatosDAO.consultarTendencias(procesoTendencia);
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

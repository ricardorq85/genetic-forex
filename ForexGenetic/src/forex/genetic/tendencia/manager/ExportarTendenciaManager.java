package forex.genetic.tendencia.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.TendenciaProcesoBuySellDAO;
import forex.genetic.dao.oracle.OracleDatoHistoricoDAO;
import forex.genetic.entities.Point;
import forex.genetic.entities.ProcesoTendenciaBuySell;
import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.Constants.OperationType;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.JDBCUtil;

public class ExportarTendenciaManager {

	protected TendenciaProcesoBuySellDAO tendenciaProcesoDAO;
	protected ProcesoTendenciaBuySell procesoTendencia;
	private static int index = 0;

	public ExportarTendenciaManager() throws ClassNotFoundException, SQLException {
		this(JDBCUtil.getConnection());
	}

	public ExportarTendenciaManager(Connection c) {
		super();
		this.conn = c;
	}

	public void export() {
		List<TendenciaParaOperar> tendencias = procesoTendencia.getTendencias();
		if (tendencias != null) {
			tendencias.stream().forEach((ten) -> {
				index++;
				// System.out.println("INDEX=" + (index)+ "," + ten.toString());
				System.out.println(ten.toString());
			});
		}
	}

	public void procesar() throws ClassNotFoundException, SQLException, GeneticDAOException {
		this.procesarRegresion();
		if ((this.procesoTendencia.getRegresion() != null) && (this.procesoTendencia.isRegresionValida())) {
			this.procesarTendencia();
		}
	}

	protected void procesarTendencia() throws SQLException, GeneticDAOException {
		List<TendenciaParaOperar> tendencias = this.consultarTendencias();
		if ((tendencias != null) && (!tendencias.isEmpty())) {
			this.calcularPuntosDiferenciaInicial(tendencias);
			tendencias.stream().forEach((ten) -> {
				ten.setPuntosDiferenciaInicial(procesoTendencia.getPuntosDiferenciaInicial());
				ten.setRegresion(procesoTendencia.getRegresion());
				ten.setTipoOperacion(procesoTendencia.getTipoOperacion());
				ten.setVigenciaLower(ten.getFechaTendencia());
				ten.setVigenciaHigher(DateUtil.adicionarMinutos(ten.getFechaTendencia(), 121));
				if (procesoTendencia.getTipoOperacion().equals(OperationType.BUY)) {
					ten.setTp(procesoTendencia.getRegresion().getMaxPrecio());
					ten.setSl(procesoTendencia.getRegresion().getMinPrecio());
				} else {
					ten.setTp(procesoTendencia.getRegresion().getMinPrecio());
					ten.setSl(procesoTendencia.getRegresion().getMaxPrecio());
				}
			});
			procesoTendencia.setTendencias(tendencias);
		}
	}

	protected List<TendenciaParaOperar> consultarTendencias() throws SQLException {
		List<TendenciaParaOperar> tendencias = tendenciaProcesoDAO.consultarTendencias(procesoTendencia);
		return tendencias;
	}

	private void calcularPuntosDiferenciaInicial(List<TendenciaParaOperar> tendencias) throws SQLException, GeneticDAOException {
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

	protected void procesarRegresion(Regresion regresion) throws SQLException {
		procesoTendencia.setRegresion(regresion);
		if (regresion.getPendiente() < 0) {
			procesoTendencia.setTipoOperacion(OperationType.SELL);
		} else if (regresion.getPendiente() > 0) {
			procesoTendencia.setTipoOperacion(OperationType.BUY);
		}
	}

	protected void procesarRegresion() throws SQLException {
		Regresion regresion = tendenciaProcesoDAO.consultarRegresion(procesoTendencia);
		procesarRegresion(regresion);
	}
	
	protected void setParametrosRegresion(Regresion regresion) {
		
	}

	public ProcesoTendenciaBuySell getProcesoTendencia() {
		return procesoTendencia;
	}

	public void setProcesoTendencia(ProcesoTendenciaBuySell procesoTendencia) {
		this.procesoTendencia = procesoTendencia;
	}

}

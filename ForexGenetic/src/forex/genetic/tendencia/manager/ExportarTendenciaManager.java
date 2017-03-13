package forex.genetic.tendencia.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import forex.genetic.dao.DatoHistoricoDAO;
import forex.genetic.dao.TendenciaProcesoBuySellDAO;
import forex.genetic.entities.Point;
import forex.genetic.entities.ProcesoTendenciaBuySell;
import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.util.Constants.OperationType;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.JDBCUtil;

public class ExportarTendenciaManager {

	private Connection conn = null;
	private TendenciaProcesoBuySellDAO dao;
	private ProcesoTendenciaBuySell paraProcesar;

	public ExportarTendenciaManager() throws ClassNotFoundException, SQLException {
		this(JDBCUtil.getConnection());
	}

	public ExportarTendenciaManager(Connection c) throws ClassNotFoundException, SQLException {
		super();
		this.conn = c;
		this.dao = new TendenciaProcesoBuySellDAO(conn);
	}

	public void export() {
		List<TendenciaParaOperar> tendencias = paraProcesar.getTendencias();
		if (tendencias != null) {
			tendencias.stream().forEach((ten) -> {
				System.out.println(ten.toString());
			});
		}
	}

	public void procesar() throws ClassNotFoundException, SQLException {
		this.procesarRegresion();
		if (this.paraProcesar.isRegresionValida()) {
			this.procesarTendencia();
		}
	}

	private void procesarTendencia() throws SQLException {
		List<TendenciaParaOperar> tendencias = dao.consultarTendencias(paraProcesar);
		this.calcularPuntosDiferenciaInicial(tendencias);
		tendencias.stream().forEach((ten) -> {
			ten.setPuntosDiferenciaInicial(paraProcesar.getPuntosDiferenciaInicial());
			ten.setRegresion(paraProcesar.getRegresion());
			ten.setTipoOperacion(paraProcesar.getTipoOperacion());
			ten.setVigenciaLower(ten.getFechaTendencia());
			ten.setVigenciaHigher(DateUtil.adicionarMinutos(ten.getFechaTendencia(), 121));
			if (paraProcesar.getTipoOperacion().equals(OperationType.BUY)) {
				ten.setTp(paraProcesar.getRegresion().getMaxPrecio());
				ten.setSl(paraProcesar.getRegresion().getMinPrecio());
			} else {
				ten.setTp(paraProcesar.getRegresion().getMinPrecio());
				ten.setSl(paraProcesar.getRegresion().getMaxPrecio());
			}
		});
		paraProcesar.setTendencias(tendencias);
	}

	private void calcularPuntosDiferenciaInicial(List<TendenciaParaOperar> tendencias) throws SQLException {
		TendenciaParaOperar op = tendencias.get(0);
		DatoHistoricoDAO datoHistoricoDAO = new DatoHistoricoDAO(conn);
		Date fechaConsultaHistorico  = datoHistoricoDAO.getFechaHistoricaMaxima(paraProcesar.getFechaBase());
		List<Point> historico = datoHistoricoDAO.consultarHistorico(fechaConsultaHistorico,
				fechaConsultaHistorico);
		Point point = null;
		if ((historico != null) && (!historico.isEmpty())) {
			point = historico.get(0);
		}
		double precioHistorico = (point.getClose() + point.getHigh() + point.getLow() + point.getOpen()) / 4;
		double precioCalculado = op.getPrecioCalculado();
		double diff = precioHistorico - precioCalculado;
		paraProcesar.setPuntosDiferenciaInicial(diff);
	}

	private void procesarRegresion() throws SQLException {
		Regresion regresion = dao.consultarRegresion(paraProcesar);
		paraProcesar.setRegresion(regresion);
		if (regresion.getPendiente() < 0) {
			paraProcesar.setTipoOperacion(OperationType.SELL);
		} else if (regresion.getPendiente() > 0) {
			paraProcesar.setTipoOperacion(OperationType.BUY);
		}
	}

	public ProcesoTendenciaBuySell getParaProcesar() {
		return paraProcesar;
	}

	public void setParaProcesar(ProcesoTendenciaBuySell paraProcesar) {
		this.paraProcesar = paraProcesar;
	}

}

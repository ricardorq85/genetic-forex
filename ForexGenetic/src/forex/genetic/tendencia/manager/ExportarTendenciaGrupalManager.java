package forex.genetic.tendencia.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import forex.genetic.dao.TendenciaProcesoBuySellDAO;
import forex.genetic.dao.TendenciaProcesoFiltradaDAO;
import forex.genetic.dao.TendenciaProcesoFiltradaUltimosDatosDAO;
import forex.genetic.dao.helper.TendenciaProcesoBuySellHelper;
import forex.genetic.entities.ProcesoTendenciaFiltradaBuySell;
import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.util.Constants.OperationType;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.JDBCUtil;

public class ExportarTendenciaGrupalManager extends ExportarTendenciaManager {

	private static final double MIN_R2 = 0.1D; // 0.5D;
	private static final double MAX_R2 = 1.1D;
	private static final double MIN_PENDIENTE = 0.0001D;
	private static final double MAX_PENDIENTE = 1.1D;
	private static final double MIN_PORCENTAJE_CANTIDAD_REGRESION = 0.5D;
	private static final double MAX_DESVIACION = 10000.0D;
	private TendenciaProcesoBuySellDAO tendenciaProcesoCompletaDAO;

	public ExportarTendenciaGrupalManager() throws ClassNotFoundException, SQLException {
		this(JDBCUtil.getConnection(), null);
	}

	public ExportarTendenciaGrupalManager(Connection c, Date fechaBase) {
		super(c);
		if (DateUtil.cumpleFechaParaTendenciaUltimosDatos(fechaBase)) {
			super.tendenciaProcesoDAO = new TendenciaProcesoFiltradaUltimosDatosDAO(c);
			this.tendenciaProcesoCompletaDAO = new TendenciaProcesoFiltradaUltimosDatosDAO(c) {
				@Override
				protected String getTablaTendenciaFiltrada() {
					return "TENDENCIA_CALCULADA";
				}
			};
		} else {
			super.tendenciaProcesoDAO = new TendenciaProcesoFiltradaDAO(c);
			this.tendenciaProcesoCompletaDAO = new TendenciaProcesoFiltradaDAO(c) {
				@Override
				protected String getTablaTendenciaFiltrada() {
					return "TENDENCIA_CALCULADA";
				}
			};
		}
	}

	protected void procesarRegresion(Regresion regresion, Regresion regresionFiltrada) throws SQLException {
		procesoTendencia.setRegresion(regresion);
		procesoTendencia.setRegresionFiltrada(regresionFiltrada);
		if (regresion != null) {
			((ProcesoTendenciaFiltradaBuySell) procesoTendencia).setRegresionFiltrada(regresionFiltrada);
			if (procesoTendencia.getRegresion().getPendiente() < 0) {
				procesoTendencia.setTipoOperacion(OperationType.SELL);
			} else if (procesoTendencia.getRegresion().getPendiente() > 0) {
				procesoTendencia.setTipoOperacion(OperationType.BUY);
			}
			if ((procesoTendencia.isRegresionValida())
					&& (regresion.getPendiente() * regresionFiltrada.getPendiente() > 0)) {
				procesoTendencia.setValida(1);
			} else {
				procesoTendencia.setValida(0);
			}
		}
	}

	@Override
	protected void procesarRegresion() throws SQLException {
		Regresion regresion = tendenciaProcesoDAO.consultarRegresion(procesoTendencia);
		this.setParametrosRegresion(regresion);
		String sqlRegresion = "SELECT PARAM.PERIODO PERIODO, PRITEN.PRECIO_CALCULADO PRIMERA_TENDENCIA, REG.*  FROM PARAMETROS PARAM, REGRESION_FILTRADA REG"
				+ " LEFT JOIN PRIMERA_TENDENCIA PRITEN ON 1=1";
		Regresion regresionFiltrada = tendenciaProcesoDAO.consultarRegresion(procesoTendencia, sqlRegresion);
		this.setParametrosRegresion(regresionFiltrada);
		this.procesarRegresion(regresion, regresionFiltrada);

		this.procesarRegresionParaCalculoJava();
	}

	// @Override
	// protected List<TendenciaParaOperar> consultarTendencias() throws SQLException
	// {
	// return null;
	// }

	private void procesarRegresionParaCalculoJava() throws SQLException {
		SimpleRegression simpleRegressionProcessorSinFiltrar = new SimpleRegression();

		List<TendenciaParaOperar> tendenciasSinFiltrar = this.tendenciaProcesoCompletaDAO
				.consultarTendencias(procesoTendencia);
		Regresion regSinFiltrarJava = null;
		if ((tendenciasSinFiltrar != null) && (!tendenciasSinFiltrar.isEmpty())) {
			tendenciasSinFiltrar.stream().forEach((ten) -> {
				float diffDias = DateUtil.diferenciaMinutos(ten.getFechaBase(), ten.getFechaTendencia()) / 60.0F
						/ 24.0F;
				simpleRegressionProcessorSinFiltrar.addData(diffDias, ten.getPrecioCalculado());
			});
			regSinFiltrarJava = TendenciaProcesoBuySellHelper.helpRegresion(procesoTendencia,
					simpleRegressionProcessorSinFiltrar);
		}

		SimpleRegression simpleRegressionProcessorFiltrada = new SimpleRegression();
		List<TendenciaParaOperar> tendenciasFiltradas = super.tendenciaProcesoDAO.consultarTendencias(procesoTendencia);
		Regresion regFiltradaJava = null;
		if ((tendenciasFiltradas != null) && (!tendenciasFiltradas.isEmpty())) {
			tendenciasFiltradas.stream().forEach((ten) -> {
				float diffDias = DateUtil.diferenciaMinutos(ten.getFechaBase(), ten.getFechaTendencia()) / 60.0F
						/ 24.0F;
				simpleRegressionProcessorFiltrada.addData(diffDias, ten.getPrecioCalculado());
			});
			regFiltradaJava = TendenciaProcesoBuySellHelper.helpRegresion(procesoTendencia,
					simpleRegressionProcessorSinFiltrar);
		}
		procesoTendencia.setRegresionJava(regSinFiltrarJava);
		procesoTendencia.setRegresionFiltradaJava(regFiltradaJava);
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

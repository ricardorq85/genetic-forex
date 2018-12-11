package forex.genetic.tendencia.manager;

import java.util.Date;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import forex.genetic.dao.helper.TendenciaProcesoBuySellHelper;
import forex.genetic.entities.ProcesoTendenciaFiltradaBuySell;
import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.util.Constants.OperationType;
import forex.genetic.util.DateUtil;

public abstract class ExportarTendenciaGrupalManager extends ExportarTendenciaManager {

	protected static final double MIN_R2 = 0.1D; // 0.5D;
	protected static final double MAX_R2 = 1.1D;
	protected static final double MIN_PENDIENTE = 0.0001D;
	protected static final double MAX_PENDIENTE = 1.1D;
	protected static final double MIN_PORCENTAJE_CANTIDAD_REGRESION = 0.5D;
	protected static final double MAX_DESVIACION = 10000.0D;

	public ExportarTendenciaGrupalManager() {
	}

	public ExportarTendenciaGrupalManager(Date fechaBase) {
		super();
	}

	protected abstract List<TendenciaParaOperar> consultarTendenciasSinFiltrar() throws GeneticDAOException;

	protected abstract List<TendenciaParaOperar> consultarTendenciasFiltradas() throws GeneticDAOException;

	protected void procesarRegresion(Regresion regresion, Regresion regresionFiltrada) {
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

	protected void procesarRegresionParaCalculoJava() throws GeneticDAOException {
		SimpleRegression simpleRegressionProcessorSinFiltrar = new SimpleRegression();
		StandardDeviation standardDeviationSinFiltrar = new StandardDeviation();

		List<TendenciaParaOperar> tendenciasSinFiltrar = consultarTendenciasSinFiltrar();
		Regresion regSinFiltrarJava = null;
		double[] sdDataSinFiltrar = new double[tendenciasSinFiltrar.size()];

		if ((tendenciasSinFiltrar != null) && (!tendenciasSinFiltrar.isEmpty())) {
			for (int i = 0; i < tendenciasSinFiltrar.size(); i++) {
				TendenciaParaOperar ten = tendenciasSinFiltrar.get(i);
				float diffDias = DateUtil.diferenciaMinutos(ten.getFechaBase(), ten.getFechaTendencia()) / 60.0F
						/ 24.0F;
				simpleRegressionProcessorSinFiltrar.addData(diffDias, ten.getPrecioCalculado());
				sdDataSinFiltrar[i] = ten.getPrecioCalculado();
			}
			standardDeviationSinFiltrar.setData(sdDataSinFiltrar);
			regSinFiltrarJava = TendenciaProcesoBuySellHelper.helpRegresion(procesoTendencia,
					simpleRegressionProcessorSinFiltrar, standardDeviationSinFiltrar);
		}

		SimpleRegression simpleRegressionProcessorFiltrada = new SimpleRegression();
		StandardDeviation standardDeviationFiltrada = new StandardDeviation();

		List<TendenciaParaOperar> tendenciasFiltradas = consultarTendenciasFiltradas();
		Regresion regFiltradaJava = null;
		double[] sdDataFiltrada = new double[tendenciasFiltradas.size()];

		if ((tendenciasFiltradas != null) && (!tendenciasFiltradas.isEmpty())) {
			for (int i = 0; i < tendenciasFiltradas.size(); i++) {
				TendenciaParaOperar ten = tendenciasFiltradas.get(i);
				float diffDias = DateUtil.diferenciaMinutos(ten.getFechaBase(), ten.getFechaTendencia()) / 60.0F
						/ 24.0F;
				simpleRegressionProcessorFiltrada.addData(diffDias, ten.getPrecioCalculado());
				sdDataFiltrada[i] = ten.getPrecioCalculado();
			}
			standardDeviationFiltrada.setData(sdDataFiltrada);
			regFiltradaJava = TendenciaProcesoBuySellHelper.helpRegresion(procesoTendencia,
					simpleRegressionProcessorFiltrada, standardDeviationFiltrada);
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

	@Override
	protected void procesarTendencia() {
		return;
	}
}

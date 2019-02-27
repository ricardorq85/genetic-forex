package forex.genetic.tendencia.manager;

import java.util.List;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import forex.genetic.dao.helper.TendenciaProcesoBuySellHelper;
import forex.genetic.entities.ProcesoTendenciaFiltradaBuySell;
import forex.genetic.entities.Regresion;
import forex.genetic.entities.TendenciaParaOperar;
import forex.genetic.exception.GeneticBusinessException;
import forex.genetic.util.Constants.OperationType;
import forex.genetic.util.DateUtil;
import forex.genetic.util.jdbc.DataClient;

public abstract class ExportarTendenciaGrupalManager extends ExportarTendenciaManager {

	protected static final double MIN_R2 = 0.1D; // 0.5D;
	protected static final double MAX_R2 = 1.1D;
	protected static final double MIN_PENDIENTE = 0.0001D;
	protected static final double MAX_PENDIENTE = 1.1D;
	protected static final double MIN_PORCENTAJE_CANTIDAD_REGRESION = 0.5D;
	protected static final double MAX_DESVIACION = 10000.0D;

	public ExportarTendenciaGrupalManager(DataClient dc) {
		super(dc);
	}

	protected abstract List<TendenciaParaOperar> consultarTendenciasSinFiltrar() throws GeneticBusinessException;

	protected abstract List<TendenciaParaOperar> consultarTendenciasFiltradas() throws GeneticBusinessException;

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

	protected void procesarRegresionParaCalculoJava() throws GeneticBusinessException {
		List<TendenciaParaOperar> tendenciasSinFiltrar = consultarTendenciasSinFiltrar();
		List<TendenciaParaOperar> tendenciasFiltradas = consultarTendenciasFiltradas();
		
		this.setParametrosRegresion(procesoTendencia.getRegresionJava());
		this.setParametrosRegresion(procesoTendencia.getRegresionFiltradaJava());

		this.calcularRegresionJava(procesoTendencia.getRegresionJava(), tendenciasSinFiltrar);
		this.calcularRegresionJava(procesoTendencia.getRegresionFiltradaJava(), tendenciasFiltradas);
	}

	private void calcularRegresionJava(Regresion regresion, List<TendenciaParaOperar> listaTendencias) {
		SimpleRegression simpleRegressionProcessor = new SimpleRegression();
		StandardDeviation standardDeviation = new StandardDeviation();

		double[] sdData = new double[listaTendencias.size()];

		if ((listaTendencias != null) && (!listaTendencias.isEmpty())) {
			for (int i = 0; i < listaTendencias.size(); i++) {
				TendenciaParaOperar tendenciaParaOperar = listaTendencias.get(i);
				float diffDias = DateUtil.diferenciaMinutos(tendenciaParaOperar.getFechaBase(),
						tendenciaParaOperar.getFechaTendencia()) / 60.0F / 24.0F;
				simpleRegressionProcessor.addData(diffDias, tendenciaParaOperar.getPrecioCalculado());
				sdData[i] = tendenciaParaOperar.getPrecioCalculado();
			}
			standardDeviation.setData(sdData);
			TendenciaProcesoBuySellHelper.helpRegresion(regresion, simpleRegressionProcessor,
					standardDeviation);
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

	@Override
	protected void procesarTendencia() {
		return;
	}
}

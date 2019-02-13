package forex.genetic.tendencia.manager;

import java.util.Date;

import forex.genetic.entities.Extremos;
import forex.genetic.entities.TendenciaParaOperarMaxMin;
import forex.genetic.exception.GeneticDAOException;
import forex.genetic.manager.PropertiesManager;
import forex.genetic.util.Constants.OperationType;
import forex.genetic.util.jdbc.DataClient;

public class AgrupadorTendenciaFactorDatosManager extends AgrupadorTendenciaManager {

	public AgrupadorTendenciaFactorDatosManager(Date fechaBase, Date maxFechaProceso, DataClient dc)
			throws GeneticDAOException {
		super(fechaBase, maxFechaProceso, dc);
	}

	@Override
	public void procesarExtremos(Extremos extremos) throws GeneticDAOException {
		super.procesarExtremos(extremos);
		this.setFactorDatos();
		this.actualizarTPO();
	}

	private void setFactorDatos() {
		super.tendenciasResultado.forEach(item -> {
			if ((!item.getTipoTendencia().startsWith("EXTREMO"))) {
				double factorDatos = item.getRegresionFiltrada().getCantidad() / (24.0D * 13.0D + 1.0D);
				if (factorDatos > this.adicionalTPO.getFactorDatos()) {
					this.adicionalTPO.setFactorDatos(factorDatos);
				}
			}

		});
	}

	private void actualizarTPO() {
		super.tendenciasResultado.forEach(item -> {
			if ((!item.getTipoTendencia().startsWith("EXTREMO"))) {
				setValores(item);
				activarInactivar(item);
			}
		});
	}

	private void activarInactivar(TendenciaParaOperarMaxMin item) {
		activarMejorTendencia(item);
		inactivarPorPrecioInvalidoConTakeProfit(item);
	}

	private void inactivarPorPrecioInvalidoConTakeProfit(TendenciaParaOperarMaxMin item) {
		if ((item.getPrecioCalculado() >= item.getTp()) && (item.getTipoOperacion().equals(OperationType.BUY))) {
			item.setActiva(0);
		} else if ((item.getPrecioCalculado() <= item.getTp())
				&& (item.getTipoOperacion().equals(OperationType.SELL))) {
			item.setActiva(0);
		} else if ((Math.abs(item.getPrecioCalculado() - item.getTp()) * PropertiesManager.getPairFactor()) < 20) {
			item.setActiva(0);
		}
	}

	private void activarMejorTendencia(TendenciaParaOperarMaxMin item) {
		if ("MEJOR_TENDENCIA".equals(item.getTipoTendencia())) {
			item.setActiva(1);
		}
	}

	private void setValores(TendenciaParaOperarMaxMin item) {
		double factorDatos = this.adicionalTPO.getFactorDatos();
		double precioAntesDe = item.getPrecioCalculado();
		double stopAperturaAntesDe = item.getStopApertura();
		double takeProfitAntesDe = item.getTp();
		double stopLossAntesDe = item.getSl();
		double minPrecioSinFiltrar = item.getRegresion().getMinPrecio();
		double minPrecioFiltrado = item.getRegresionFiltrada().getMinPrecio();

		double maxPrecioSinFiltrar = item.getRegresion().getMaxPrecio();
		double maxPrecioFiltrado = item.getRegresionFiltrada().getMaxPrecio();

		double valorMinimo = 1000.0D / PropertiesManager.getPairFactor();
		double multiplicador = 100.0D / PropertiesManager.getPairFactor();
		double multiplicadorFactorDatosParaLimitApertura = 0.20D * 10.0D;
		double multiplicadorFactorDatosParaPrecio = 0.25D * 10.0D;
		double multiplicadorFactorDatosParaStopApertura = 0.15D * 10.0D;
		double multiplicadorFactorDatosParaTakeProfit = 0.15D * 10.0D;
		double multiplicadorFactorDatosParaStopLoss = 0.25D * 10.0D;

		double baseLimitApertura, basePrecio, baseStopApertura, baseTakeProfit, baseStopLoss;
		if (factorDatos > 0) {
			baseLimitApertura = (1 - factorDatos) * multiplicadorFactorDatosParaLimitApertura;
			basePrecio = (1 - factorDatos) * multiplicadorFactorDatosParaPrecio;
			baseStopApertura = (1 - factorDatos) * multiplicadorFactorDatosParaStopApertura;
			baseTakeProfit = (1 - factorDatos) * multiplicadorFactorDatosParaTakeProfit;
			baseStopLoss = (1 - factorDatos) * multiplicadorFactorDatosParaStopLoss;
		} else {
			baseLimitApertura = 1.0D / 0.1D;
			basePrecio = 1.0D / 0.1D;
			baseStopApertura = 1.0D / 0.1D;
			baseTakeProfit = 1.0D / 0.1D;
			baseStopLoss = 1.0D / 0.1D;
		}
		double calculoLimitApertura = Math.min(valorMinimo, (multiplicador) * (baseLimitApertura));
		double calculoPrecio = Math.min(valorMinimo, (multiplicador) * (basePrecio));
		double calculoStopApertura = Math.min(valorMinimo, (multiplicador) * (baseStopApertura));
		double calculoTakeProfit = Math.min(valorMinimo, (multiplicador) * (baseTakeProfit));
		double calculoStopLoss = Math.min(valorMinimo, (multiplicador) * (baseStopLoss));

		double nuevoPrecio;
		if (item.getTipoOperacion().equals(OperationType.BUY)) {
			double diffFiltradoVsSinFiltrar = (minPrecioFiltrado - minPrecioSinFiltrar) * factorDatos;
			calculoLimitApertura = Math.min(calculoLimitApertura, diffFiltradoVsSinFiltrar);

			item.setLimitApertura(precioAntesDe - calculoLimitApertura);
			nuevoPrecio = precioAntesDe + calculoPrecio;
			item.setPrecioCalculado(nuevoPrecio);
			item.setStopApertura(stopAperturaAntesDe - calculoStopApertura);
			item.setTp(takeProfitAntesDe - calculoTakeProfit);
			item.setSl(stopLossAntesDe - calculoStopLoss);
		} else if (item.getTipoOperacion().equals(OperationType.SELL)) {
			double diffFiltradoVsSinFiltrar = (maxPrecioSinFiltrar - maxPrecioFiltrado) * factorDatos;
			calculoLimitApertura = Math.max(calculoLimitApertura, diffFiltradoVsSinFiltrar);

			item.setLimitApertura(precioAntesDe + calculoLimitApertura);
			nuevoPrecio = precioAntesDe - calculoPrecio;
			item.setPrecioCalculado(nuevoPrecio);
			item.setStopApertura(stopAperturaAntesDe + calculoStopApertura);
			item.setTp(takeProfitAntesDe + calculoTakeProfit);
			item.setSl(stopLossAntesDe + calculoStopLoss);
		}
	}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

/**
 *
 * @author ricardorq85
 */
public class CalculoTendenciaEstadistica extends CalculoTendencia {

	double baseProbabilidadXDuracion = 0.1D;
	double baseProbabilidadXCantidad = 0.4D;
	double baseProbabilidadXPips = 0.3D;
	double baseProbabilidadXPipsFaltantes = 0.05D;
	double baseProbabilidadXPipsRetroceso = 0.15D;

	private Estadistica estadistica = null;
	private Order ordenActual;

	public CalculoTendenciaEstadistica(Estadistica estadisticaIndividuo, Order ordenActual) {
		this.estadistica = estadisticaIndividuo;
		this.ordenActual = ordenActual;
	}

	public void calcular() {
		this.calcularPips();
		this.calcularDuracion();
	}

	public void calcularProbabilidades() {
		double probPositivosXCantidad = this.calcularProbabilidadXCantidadPositivos() * baseProbabilidadXCantidad;
		double probNegativosXCantidad = this.calcularProbabilidadXCantidadNegativos() * baseProbabilidadXCantidad;

		double probPositivosXPips = this.calcularProbabilidadXPipsPositivos() * baseProbabilidadXPips;
		double probNegativosXPips = this.calcularProbabilidadXPipsNegativos() * baseProbabilidadXPips;

		double probPositivosXPipsFaltantes = this.calcularProbabilidadXPipsPositivosFaltantes()
				* baseProbabilidadXPipsFaltantes;
		double probNegativosXPipsFaltantes = this.calcularProbabilidadXPipsNegativosFaltantes()
				* baseProbabilidadXPipsFaltantes;

		double probPositivosXDuracion = this.calcularProbabilidadXDuracionPositivos() * baseProbabilidadXDuracion;
		double probNegativosXDuracion = this.calcularProbabilidadXDuracionNegativos() * baseProbabilidadXDuracion;

		double probPositivosXRetroceso = 0.5D;
		double probNegativosXRetroceso = 0.5D;
		if (this.ordenActual.getPips() > 0) {
			probPositivosXRetroceso = this.calcularProbabilidadXRetrocesosPositivos();
			probNegativosXRetroceso = (1 - probPositivosXRetroceso);
		} else if (this.ordenActual.getPips() < 0) {
			probNegativosXRetroceso = this.calcularProbabilidadXRetrocesosNegativos();
			probPositivosXRetroceso = (1 - probNegativosXRetroceso);
		}
		probPositivosXRetroceso *= baseProbabilidadXPipsRetroceso;
		probNegativosXRetroceso *= baseProbabilidadXPipsRetroceso;

		double probPositivos = (probPositivosXCantidad + probPositivosXPips + probPositivosXPipsFaltantes
				+ probPositivosXDuracion + probPositivosXRetroceso);
		double probNegativos = (probNegativosXCantidad + probNegativosXPips + probNegativosXPipsFaltantes
				+ probNegativosXDuracion + probNegativosXRetroceso);

		this.setProbabilidadPositivos(probPositivos);
		this.setProbabilidadNegativos(probNegativos);
	}

	private double calcularProbabilidadXCantidad(int cantidad) {
		double probabilidad = 0.0D;
		if (estadistica.getCantidadTotal() > 0) {
			probabilidad = (cantidad / (double) estadistica.getCantidadTotal());
		}
		return (probabilidad);
	}

	private double calcularProbabilidadXPips(double pips) {
		double probabilidad = 0.0D;
		if (estadistica.getSumaPipsAbs() > 0.0D) {
			probabilidad = (Math.abs(pips) / (estadistica.getSumaPipsAbs()));
		}
		return (probabilidad);
	}

	private double calcularProbabilidadXPipsFaltantes(double pips) {
		double probabilidad = 0.0D;
		if (estadistica.getSumaDiferenciaPipsPromedio(ordenActual.getPips()) > 0.0D) {
			probabilidad = (1.0D
					- (Math.abs(pips) / (estadistica.getSumaDiferenciaPipsPromedio(ordenActual.getPips()))));
		}
		return (probabilidad);
	}

	private double calcularProbabilidadXDuracion(double duracion) {
		double probabilidad = 0.0D;
		if ((estadistica.getSumaDiferenciaDuracionPromedio(ordenActual.getDuracionMinutos())) > 0) {
			probabilidad = (1.0D - (Math.abs(ordenActual.getDuracionMinutos() - duracion)
					/ (estadistica.getSumaDiferenciaDuracionPromedio(ordenActual.getDuracionMinutos()))));
		}
		return (probabilidad);
	}

	public double calcularProbabilidadXRetrocesos(double limiteExtremo, double promedio) {
		double probabilidad = 0.5D;
		if (Math.abs(promedio) > 0) {
			if (Math.abs(ordenActual.getPips()) > Math.abs(promedio)) {
				probabilidad = 0.8D;
			}
		}
		if (Math.abs(limiteExtremo) > 0) {
			if (Math.abs(ordenActual.getPips()) > Math.abs(limiteExtremo)) {
				probabilidad = 1.0D;
			}
		}
		return (probabilidad);
	}

	private void calcularPips() {
		double pips = 0.0D;
		if (this.getProbabilidadPositivos() > this.getProbabilidadNegativos()) {
			pips = calcularPips(estadistica.getPipsPromedioPositivos());
		} else {
			pips = calcularPips(estadistica.getPipsPromedioNegativos());
		}
		this.setPips(pips);
	}

	private double calcularPips(double pips) {
		return (pips - ordenActual.getPips());
	}

	private void calcularDuracion() {
		double pipsPromedio, duracionPromedio;
		long calculoDuracion = 0L;
		if (this.getProbabilidadPositivos() > this.getProbabilidadNegativos()) {
			pipsPromedio = estadistica.getPipsPromedioPositivos();
			duracionPromedio = estadistica.getDuracionPromedioPositivos();
		} else {
			pipsPromedio = estadistica.getPipsPromedioNegativos();
			duracionPromedio = estadistica.getDuracionPromedioNegativos();
		}
		double pipsXMinuto = Math.abs(pipsPromedio / (duracionPromedio + 1));
		double pipsXMinutoOrdenActual = Math.abs(ordenActual.getPips() / (ordenActual.getDuracionMinutos() + 1));
		calculoDuracion = (long) Math
				.ceil((Math.abs(this.getPips()) / (pipsXMinuto * 0.3 + pipsXMinutoOrdenActual * 0.7)));

		this.setDuracion(calculoDuracion);
	}

	private double calcularProbabilidadXDuracionNegativos() {
		return this.calcularProbabilidadXDuracion(estadistica.getDuracionPromedioNegativos());
	}

	private double calcularProbabilidadXDuracionPositivos() {
		return this.calcularProbabilidadXDuracion(estadistica.getDuracionPromedioPositivos());
	}

	private double calcularProbabilidadXPipsPositivos() {
		return this.calcularProbabilidadXPips(estadistica.getPipsPositivos());
	}

	private double calcularProbabilidadXPipsNegativos() {
		return this.calcularProbabilidadXPips(estadistica.getPipsNegativos());
	}

	private double calcularProbabilidadXPipsNegativosFaltantes() {
		return this.calcularProbabilidadXPipsFaltantes(calcularPips(estadistica.getPipsPromedioNegativos()));
	}

	private double calcularProbabilidadXPipsPositivosFaltantes() {
		return this.calcularProbabilidadXPipsFaltantes(calcularPips(estadistica.getPipsPromedioPositivos()));
	}

	private double calcularProbabilidadXCantidadPositivos() {
		return this.calcularProbabilidadXCantidad(estadistica.getCantidadPositivos());
	}

	private double calcularProbabilidadXCantidadNegativos() {
		return this.calcularProbabilidadXCantidad(estadistica.getCantidadNegativos());
	}

	public double calcularProbabilidadXRetrocesosPositivos() {
		return (calcularProbabilidadXRetrocesos(estadistica.getPipsMaximosRetrocesoNegativos(),
				estadistica.getPipsPromedioRetrocesoNegativos()));
	}

	public double calcularProbabilidadXRetrocesosNegativos() {
		return (calcularProbabilidadXRetrocesos(estadistica.getPipsMinimosRetrocesoPositivos(),
				estadistica.getPipsPromedioRetrocesoPositivos()));
	}

	public Estadistica getEstadistica() {
		return estadistica;
	}

	public void setEstadistica(Estadistica estadistica) {
		this.estadistica = estadistica;
	}

	public Order getOrdenActual() {
		return ordenActual;
	}

	public void setOrdenActual(Order ordenActual) {
		this.ordenActual = ordenActual;
	}

	@Override
	public String toString() {
		return "CalculoTendenciaEstadistica [getProbabilidadNegativos()=" + getProbabilidadNegativos()
				+ ", getProbabilidadPositivos()=" + getProbabilidadPositivos() + ", getDuracion()=" + getDuracion()
				+ ", getPips()=" + getPips() + "]";
	}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.io.Serializable;
import java.util.Date;

import forex.genetic.util.Constants;
import forex.genetic.util.DateUtil;
import forex.genetic.util.NumberUtil;

/**
 *
 * @author ricardorq85
 */
public class TendenciaEstadistica extends Tendencia implements Serializable {

	private static final long serialVersionUID = -5150246419351280437L;
	private CalculoTendenciaEstadistica calculoTendencia = null;
	private CalculoTendenciaEstadistica calculoTendenciaFiltradaActual = null;
	private Order ordenActual;
	private double porcentajeEstadisticaIndividuo;
	private double porcentajeEstadisticaFiltrada;

	public TendenciaEstadistica(Estadistica estadisticaIndividuo, Estadistica estadisticaFiltradaActual,
			Order ordenActual) {
		if ((estadisticaIndividuo == null) || (estadisticaFiltradaActual == null)) {
			throw new IllegalArgumentException("Las estadisticas no pueden ser null");
		}
		this.ordenActual = ordenActual;
		this.calculoTendencia = new CalculoTendenciaEstadistica(estadisticaIndividuo, ordenActual);
		this.calculoTendenciaFiltradaActual = new CalculoTendenciaEstadistica(estadisticaFiltradaActual, ordenActual);
		this.setTipoTendencia(Constants.TIPO_TENDENCIA);
	}

	public void procesarTendencia() {
		this.calculoTendencia.calcularProbabilidades();
		this.calculoTendenciaFiltradaActual.calcularProbabilidades();
		consolidar();
	}

	protected void consolidar() {
		this.consolidarPorcentajeEstadistica();
		this.consolidarProbabilidades();
		this.recalcularCalculoTendencia();
		this.consolidarPips();
		this.consolidarDuracion();
		this.consolidarFecha();
	}

	private void recalcularCalculoTendencia() {
		this.calculoTendencia.setProbabilidadPositivos(this.getProbabilidadPositivos());
		this.calculoTendenciaFiltradaActual.setProbabilidadPositivos(this.getProbabilidadPositivos());

		this.calculoTendencia.setProbabilidadNegativos(this.getProbabilidadNegativos());
		this.calculoTendenciaFiltradaActual.setProbabilidadNegativos(this.getProbabilidadNegativos());

		this.calculoTendencia.calcular();
		this.calculoTendenciaFiltradaActual.calcular();
	}

	private void consolidarPorcentajeEstadistica() {
		porcentajeEstadisticaIndividuo = this
				.consolidarPorcentajeEstadistica(calculoTendencia.getEstadistica().getCantidadTotal());
		porcentajeEstadisticaFiltrada = this
				.consolidarPorcentajeEstadistica(calculoTendenciaFiltradaActual.getEstadistica().getCantidadTotal());
	}

	private void consolidarFecha() {
		Date probDate = DateUtil.calcularFechaXDuracion(this.getDuracion(), this.getFechaBase());
		this.setFechaTendencia(probDate);
		this.setDuracionMinutos(DateUtil.calcularDuracionMinutos(getFechaBase(), getFechaTendencia()));
	}

	private void consolidarDuracion() {
		long duracionConsolidada = (long) (calculoTendencia.getDuracion() * porcentajeEstadisticaIndividuo
				+ calculoTendenciaFiltradaActual.getDuracion() * porcentajeEstadisticaFiltrada);
		this.setDuracion(duracionConsolidada);
	}

	private void consolidarPips() {
		double pipsConsolidados;
		pipsConsolidados = (calculoTendencia.getPips() * porcentajeEstadisticaIndividuo
				+ calculoTendenciaFiltradaActual.getPips() * porcentajeEstadisticaFiltrada);
		this.setPips(NumberUtil.round(pipsConsolidados));
	}

	private double consolidarPorcentajeEstadistica(double cantidad) {
		int cantidadTotal = this.getCantidadTotalEstadisticas();
		double porcentajeEstadistica = cantidad / cantidadTotal;
		return porcentajeEstadistica;
	}

	private int getCantidadTotalEstadisticas() {
		return (this.calculoTendencia.getEstadistica().getCantidadTotal()
				+ this.calculoTendenciaFiltradaActual.getEstadistica().getCantidadTotal());
	}

	private void consolidarProbabilidades() {
		double probPositivos = (this.calculoTendencia.getProbabilidadPositivos() * porcentajeEstadisticaIndividuo)
				+ (this.calculoTendenciaFiltradaActual.getProbabilidadPositivos() * porcentajeEstadisticaFiltrada);
		double probNegativos = (this.calculoTendencia.getProbabilidadNegativos() * porcentajeEstadisticaIndividuo)
				+ (this.calculoTendenciaFiltradaActual.getProbabilidadNegativos() * porcentajeEstadisticaFiltrada);

		this.setProbabilidadPositivos(probPositivos);
		this.setProbabilidadNegativos(probNegativos);
		this.setProbabilidad(Math.max(probPositivos, probNegativos));
	}

	public CalculoTendenciaEstadistica getCalculoTendencia() {
		return calculoTendencia;
	}

	public void setCalculoTendencia(CalculoTendenciaEstadistica calculoTendencia) {
		this.calculoTendencia = calculoTendencia;
	}

	public CalculoTendenciaEstadistica getCalculoTendenciaFiltradaActual() {
		return calculoTendenciaFiltradaActual;
	}

	public void setCalculoTendenciaFiltradaActual(CalculoTendenciaEstadistica calculoTendenciaFiltradaActual) {
		this.calculoTendenciaFiltradaActual = calculoTendenciaFiltradaActual;
	}

	public Order getOrdenActual() {
		return ordenActual;
	}

	public void setOrdenActual(Order ordenActual) {
		this.ordenActual = ordenActual;
	}

	@Override
	public String toString() {
		return "TendenciaEstadistica [getIndividuo()=" + getIndividuo().getId() + ", getFechaCierre()="
				+ getFechaCierre() + ", getFecha()=" + getFecha() + ", getProbabilidad()=" + getProbabilidad()
				+ ", getDuracionActual()=" + getDuracionActual() + ", getPipsActuales()=" + getPipsActuales()
				+ ", getDuracion()=" + getDuracion() + ", getTipoTendencia()=" + getTipoTendencia()
				+ ", getTendencia()=" + getTendencia() + ", getFechaBase()=" + getFechaBase() + ", getFechaTendencia()="
				+ getFechaTendencia() + ", getPips()=" + getPips() + ", getPrecioBase()=" + getPrecioBase()
				+ ", getPrecioCalculado()=" + getPrecioCalculado() + ", getFechaApertura()=" + getFechaApertura()
				+ ", getPrecioApertura()=" + getPrecioApertura() + ", getProbabilidadNegativos()="
				+ getProbabilidadNegativos() + ", getProbabilidadPositivos()=" + getProbabilidadPositivos()
				+ ", getPipsReales()=" + getPipsReales() + ", getTipoCalculo()=" + getTipoCalculo() + "]";
	}

}

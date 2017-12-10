/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import forex.genetic.entities.indicator.Indicator;

/**
 *
 * @author ricardorq85
 */
public class RangoOperacionIndividuoIndicador {

	private Indicator indicator;
	private double promedio;
	private double porcentajeCumplimiento;

	public boolean cumplePorcentajeIndicador() {
		return (porcentajeCumplimiento > 0.2);
		/*
		 * && (porcentajeCumplimiento < 0.8)
		 */
	}

	public double getPorcentajeCumplimiento() {
		return porcentajeCumplimiento;
	}

	public void setPorcentajeCumplimiento(double porcentajeCumplimiento) {
		this.porcentajeCumplimiento = porcentajeCumplimiento;
	}

	public Indicator getIndicator() {
		return indicator;
	}

	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}

	@Override
	public String toString() {
		return "RangoOperacionIndividuoIndicador [porcentajeCumplimiento=" + porcentajeCumplimiento + ", promedio="
				+ promedio + ", indicator=" + indicator + "]";
	}

	public double getPromedio() {
		return promedio;
	}

	public void setPromedio(double promedio) {
		this.promedio = promedio;
	}

}

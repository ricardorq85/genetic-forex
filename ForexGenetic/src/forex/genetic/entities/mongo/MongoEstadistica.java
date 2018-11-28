/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.mongo;

import java.util.Date;

import forex.genetic.entities.Estadistica;

/**
 *
 * @author ricardorq85
 */
public class MongoEstadistica extends Estadistica {

	private String idIndividuo;
	private Date fechaInicial;
	private Date fechaFinal;

	private double duracionTotal;
	private double duracionTotalPositivos;
	private double duracionTotalNegativos;

	public MongoEstadistica() {
		this.setDuracionMinima(Double.MAX_VALUE);
		this.setDuracionMinimaNegativos(Double.MAX_VALUE);
		this.setDuracionMinimaPositivos(Double.MAX_VALUE);
		
		this.setPipsMinimos(Double.MAX_VALUE);
		this.setPipsMinimosNegativos(Double.MAX_VALUE);
		this.setPipsMinimosPositivos(Double.MAX_VALUE);

		this.setPipsMinimosRetroceso(Double.MAX_VALUE);
		this.setPipsMinimosRetrocesoNegativos(Double.MAX_VALUE);
		this.setPipsMinimosRetrocesoPositivos(Double.MAX_VALUE);
	}

	public String getIdIndividuo() {
		return idIndividuo;
	}

	public void setIdIndividuo(String idIndividuo) {
		this.idIndividuo = idIndividuo;
	}

	public Date getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public double getDuracionTotal() {
		return duracionTotal;
	}

	public void setDuracionTotal(double duracionTotal) {
		this.duracionTotal = duracionTotal;
	}

	public double getDuracionTotalPositivos() {
		return duracionTotalPositivos;
	}

	public void setDuracionTotalPositivos(double duracionTotalPositivos) {
		this.duracionTotalPositivos = duracionTotalPositivos;
	}

	public double getDuracionTotalNegativos() {
		return duracionTotalNegativos;
	}

	public void setDuracionTotalNegativos(double duracionTotalNegativos) {
		this.duracionTotalNegativos = duracionTotalNegativos;
	}

}

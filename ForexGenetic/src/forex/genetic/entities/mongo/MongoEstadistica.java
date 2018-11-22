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
}

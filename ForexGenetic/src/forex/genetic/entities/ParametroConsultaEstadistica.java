package forex.genetic.entities;

import java.util.Date;

public class ParametroConsultaEstadistica {
	private Date fecha;
	private Double retroceso;
	private Long duracion;
	private Individuo individuo;

	public ParametroConsultaEstadistica(Date fecha, Double retroceso, Long duracion, Individuo individuo) {
		super();
		this.fecha = fecha;
		this.retroceso = retroceso;
		this.duracion = duracion;
		this.individuo = individuo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Double getRetroceso() {
		return retroceso;
	}

	public void setRetroceso(Double retroceso) {
		this.retroceso = retroceso;
	}

	public Long getDuracion() {
		return duracion;
	}

	public void setDuracion(Long duracion) {
		this.duracion = duracion;
	}

	public Individuo getIndividuo() {
		return individuo;
	}

	public void setIndividuo(Individuo individuo) {
		this.individuo = individuo;
	}

}

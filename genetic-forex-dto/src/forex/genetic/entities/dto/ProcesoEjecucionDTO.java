package forex.genetic.entities.dto;

import java.util.Date;

public class ProcesoEjecucionDTO {

	private Date maxFechaHistorico;
	private Date fechaAperturaActiva;

	public Date getMaxFechaHistorico() {
		return maxFechaHistorico;
	}

	public void setMaxFechaHistorico(Date maxFechaHistorico) {
		this.maxFechaHistorico = maxFechaHistorico;
	}

	public Date getFechaAperturaActiva() {
		return fechaAperturaActiva;
	}

	public void setFechaAperturaActiva(Date fechaAperturaActiva) {
		this.fechaAperturaActiva = fechaAperturaActiva;
	}

}

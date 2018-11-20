package forex.genetic.entities.dto;

import java.util.Date;

public class ProcesoEjecucionDTO {

	private Date maxFechaHistorico;
	private Date fechaProceso;

	public Date getMaxFechaHistorico() {
		return maxFechaHistorico;
	}

	public void setMaxFechaHistorico(Date maxFechaHistorico) {
		this.maxFechaHistorico = maxFechaHistorico;
	}

	public Date getFechaProceso() {
		return fechaProceso;
	}

	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}
}

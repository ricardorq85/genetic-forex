package forex.genetic.entities.dto;

import java.util.Date;

public class ProcesoEjecucionDTO {

	private Date maxFechaHistorico;
	private Date fechaAperturaActiva;
	private Date fechaProceso;
	private double spreadActiva;
	private double openPriceActiva;
	private String tipoOperacionActiva;

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

	public Date getFechaProceso() {
		return fechaProceso;
	}

	public void setFechaProceso(Date fechaProceso) {
		this.fechaProceso = fechaProceso;
	}

	public double getSpreadActiva() {
		return spreadActiva;
	}

	public void setSpreadActiva(double spreadActiva) {
		this.spreadActiva = spreadActiva;
	}

	public double getOpenPriceActiva() {
		return openPriceActiva;
	}

	public void setOpenPriceActiva(double openPriceActiva) {
		this.openPriceActiva = openPriceActiva;
	}

	public String getTipoOperacionActiva() {
		return tipoOperacionActiva;
	}

	public void setTipoOperacionActiva(String tipoOperacionActiva) {
		this.tipoOperacionActiva = tipoOperacionActiva;
	}

}

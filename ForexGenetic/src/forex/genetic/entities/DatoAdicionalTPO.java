package forex.genetic.entities;

import java.util.Date;

public class DatoAdicionalTPO {

	private Date fechaBase = null;

	private double r2Promedio, pendientePromedio, probabilidadPromedio, diferenciaPrecioSuperior,
			diferenciaPrecioInferior;

	private int numeroTendencias, cantidadTotalTendencias, numeroPendientesPositivas, numeroPendientesNegativas;

	public Date getFechaBase() {
		return fechaBase;
	}

	public void setFechaBase(Date fechaBase) {
		this.fechaBase = fechaBase;
	}

	public double getPendientePromedio() {
		return pendientePromedio;
	}

	public void setPendientePromedio(double pendientePromedio) {
		this.pendientePromedio = pendientePromedio;
	}

	public double getProbabilidadPromedio() {
		return probabilidadPromedio;
	}

	public void setProbabilidadPromedio(double probabilidadPromedio) {
		this.probabilidadPromedio = probabilidadPromedio;
	}

	public double getR2Promedio() {
		return r2Promedio;
	}

	public void setR2Promedio(double r2Promedio) {
		this.r2Promedio = r2Promedio;
	}

	public int getCantidadTotalTendencias() {
		return cantidadTotalTendencias;
	}

	public void setCantidadTotalTendencias(int cantidadTotalTendencias) {
		this.cantidadTotalTendencias = cantidadTotalTendencias;
	}

	public int getNumeroTendencias() {
		return numeroTendencias;
	}

	public void setNumeroTendencias(int numeroTendencias) {
		this.numeroTendencias = numeroTendencias;
	}

	public int getNumeroPendientesPositivas() {
		return numeroPendientesPositivas;
	}

	public void setNumeroPendientesPositivas(int numeroPendientesPositivas) {
		this.numeroPendientesPositivas = numeroPendientesPositivas;
	}

	public int getNumeroPendientesNegativas() {
		return numeroPendientesNegativas;
	}

	public void setNumeroPendientesNegativas(int numeroPendientesNegativas) {
		this.numeroPendientesNegativas = numeroPendientesNegativas;
	}

	public double getDiferenciaPrecioSuperior() {
		return diferenciaPrecioSuperior;
	}

	public void setDiferenciaPrecioSuperior(double diferenciaPrecioSuperior) {
		this.diferenciaPrecioSuperior = diferenciaPrecioSuperior;
	}

	public double getDiferenciaPrecioInferior() {
		return diferenciaPrecioInferior;
	}

	public void setDiferenciaPrecioInferior(double diferenciaPrecioInferior) {
		this.diferenciaPrecioInferior = diferenciaPrecioInferior;
	}

}

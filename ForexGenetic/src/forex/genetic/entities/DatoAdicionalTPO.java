package forex.genetic.entities;

import java.util.Date;

import forex.genetic.util.Constants.OperationType;

public class DatoAdicionalTPO {

	private Date fechaBase = null;

	private double r2Promedio, pendientePromedio, probabilidadPromedio, diferenciaPrecioPromedio;

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

	public double getDiferenciaPrecioPromedio() {
		return diferenciaPrecioPromedio;
	}

	public void setDiferenciaPrecioPromedio(double diferenciaPrecioPromedio) {
		this.diferenciaPrecioPromedio = diferenciaPrecioPromedio;
	}

}

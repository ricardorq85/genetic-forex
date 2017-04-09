package forex.genetic.entities;

import java.util.Date;

public class Regresion {

	private static final double MIN_R2 = 0.5D;
	private static final double MAX_R2 = 1.1D;
	private static final double MIN_PENDIENTE = 0.0001;
	private static final double MAX_PENDIENTE = 1.1D;
	private static final double MIN_PORCENTAJE_CANTIDAD_REGRESION = 0.2D;
	private static final double MAX_DESVIACION = 10000.0D;

	private double tiempoTendencia;
	private double r2;
	private double pendiente;
	private double desviacion;
	private double minPrecio;
	private double maxPrecio;
	private int cantidad;
	private Date minFechaTendencia;
	private Date maxFechaTendencia;

	protected boolean isR2Valido() {
		return (this.getR2() > MIN_R2 && this.getR2() < MAX_R2);
	}

	protected boolean isPendienteValida() {
		return (Math.abs(this.getPendiente()) > MIN_PENDIENTE && Math.abs(this.getPendiente()) < MAX_PENDIENTE);
	}

	private int getCantidadMinimaRegresion() {
		return (int) (((this.getTiempoTendencia() / 60.0D)) * MIN_PORCENTAJE_CANTIDAD_REGRESION);
	}

	public boolean isRegresionValida() {
		return isR2Valido() && isPendienteValida() && isCantidadValida() && isDesviacionValida();
	}

	private boolean isDesviacionValida() {
		return (this.getDesviacion() < MAX_DESVIACION);
	}

	private boolean isCantidadValida() {
		return (this.getCantidad() > getCantidadMinimaRegresion());
	}

	public double getPendiente() {
		return pendiente;
	}

	public void setPendiente(double pendiente) {
		this.pendiente = pendiente;
	}

	public double getR2() {
		return r2;
	}

	public void setR2(double r2) {
		this.r2 = r2;
	}

	public double getMinPrecio() {
		return minPrecio;
	}

	public void setMinPrecio(double minPrecio) {
		this.minPrecio = minPrecio;
	}

	public double getMaxPrecio() {
		return maxPrecio;
	}

	public void setMaxPrecio(double maxPrecio) {
		this.maxPrecio = maxPrecio;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public double getDesviacion() {
		return desviacion;
	}

	public void setDesviacion(double desviacion) {
		this.desviacion = desviacion;
	}

	public double getTiempoTendencia() {
		return tiempoTendencia;
	}

	public void setTiempoTendencia(double tiempoTendencia) {
		this.tiempoTendencia = tiempoTendencia;
	}

	public Date getMinFechaTendencia() {
		return minFechaTendencia;
	}

	public void setMinFechaTendencia(Date minFechaTendencia) {
		this.minFechaTendencia = minFechaTendencia;
	}

	public Date getMaxFechaTendencia() {
		return maxFechaTendencia;
	}

	public void setMaxFechaTendencia(Date maxFechaTendencia) {
		this.maxFechaTendencia = maxFechaTendencia;
	}

}

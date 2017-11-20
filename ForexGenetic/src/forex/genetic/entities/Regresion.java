package forex.genetic.entities;

import java.util.Date;

public class Regresion {
	private double tiempoTendencia;
	private double r2;
	private double pendiente;
	private double desviacion;
	private double probabilidad;
	private double minPrecio, minPrecioExtremo;
	private double maxPrecio, maxPrecioExtremo;
	private int cantidad, cantidadTotal;
	private Date minFechaTendencia;
	private Date maxFechaTendencia;
	private double minimoR2;
	private double maximoR2;
	private double minimoPendiente, minimoValidacionPendiente;
	private double maximoPendiente;
	private double minimoPorcentajeCantidadRegresion;
	private double maximoDesviacion;

	public boolean isRegresionValida() {
		return isR2Valido() && isPendienteValida() && isCantidadValida() && isDesviacionValida();
	}

	public boolean isValidacionRegresionValida() {
		return isR2Valido() && isValidacionPendienteValida() && isCantidadValida() && isDesviacionValida();
	}

	protected boolean isR2Valido() {
		return (this.getR2() > minimoR2 && this.getR2() < maximoR2);
	}

	protected boolean isPendienteValida() {
		return (Math.abs(this.getPendiente()) > minimoPendiente && Math.abs(this.getPendiente()) < maximoPendiente);
	}

	protected boolean isValidacionPendienteValida() {
		return (Math.abs(this.getPendiente()) > minimoValidacionPendiente && Math.abs(this.getPendiente()) < maximoPendiente);
	}

	private int getCantidadMinimaRegresion() {
		return (int) (((this.getTiempoTendencia() / 60.0D)) * minimoPorcentajeCantidadRegresion);
	}

	public boolean isRegresionValidaMaximoMinimo() {
		// return isR2Valido() && isPendienteValida() && isCantidadValida() &&
		// isDesviacionValida();
		return (this.cantidad > 0);
	}

	private boolean isDesviacionValida() {
		return (this.getDesviacion() < maximoDesviacion);
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

	public void setMinimoR2(double minR2) {
		this.minimoR2 = minR2;
	}

	public void setMaximoR2(double maxR2) {
		this.maximoR2 = maxR2;
	}

	public void setMinimoPendiente(double minPendiente) {
		this.minimoPendiente = minPendiente;
		this.minimoValidacionPendiente = minPendiente / 10.0D;
	}

	public void setMaximoPendiente(double maxPendiente) {
		this.maximoPendiente = maxPendiente;
	}

	public void setMinimoPorcentajeCantidadRegresion(double minPorcentajeCantidadRegresion) {
		this.minimoPorcentajeCantidadRegresion = minPorcentajeCantidadRegresion;
	}

	public void setMaximoDesviacion(double maxDesviacion) {
		this.maximoDesviacion = maxDesviacion;
	}

	public double getMinPrecioExtremo() {
		return minPrecioExtremo;
	}

	public void setMinPrecioExtremo(double minPrecioExtremo) {
		this.minPrecioExtremo = minPrecioExtremo;
	}

	public double getMaxPrecioExtremo() {
		return maxPrecioExtremo;
	}

	public void setMaxPrecioExtremo(double maxPrecioExtremo) {
		this.maxPrecioExtremo = maxPrecioExtremo;
	}

	public double getMinimoValidacionPendiente() {
		return minimoValidacionPendiente;
	}

	public void setMinimoValidacionPendiente(double minimoValidacionPendiente) {
		this.minimoValidacionPendiente = minimoValidacionPendiente;
	}

	public double getMinimoR2() {
		return minimoR2;
	}

	public double getMaximoR2() {
		return maximoR2;
	}

	public double getMinimoPendiente() {
		return minimoPendiente;
	}

	public double getMaximoPendiente() {
		return maximoPendiente;
	}

	public double getMinimoPorcentajeCantidadRegresion() {
		return minimoPorcentajeCantidadRegresion;
	}

	public double getMaximoDesviacion() {
		return maximoDesviacion;
	}

	public double getProbabilidad() {
		return probabilidad;
	}

	public void setProbabilidad(double probabilidad) {
		this.probabilidad = probabilidad;
	}

	public int getCantidadTotal() {
		return cantidadTotal;
	}

	public void setCantidadTotal(int cantidadTotal) {
		this.cantidadTotal = cantidadTotal;
	}

}

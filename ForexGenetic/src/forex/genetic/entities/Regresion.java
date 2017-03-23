package forex.genetic.entities;

public class Regresion {

	private double r2;
	private double pendiente;
	private double desviacion;
	private double minPrecio;
	private double maxPrecio;
	private int	   cantidad;

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

}

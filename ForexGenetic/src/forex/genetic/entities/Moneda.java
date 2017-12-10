package forex.genetic.entities;

public class Moneda {
	private String moneda;
	private int digitos;
	private int stopLevel;
	private double minVolume;
	private double maxVolume;

	public Moneda() {
	}

	public Moneda(String moneda, int digitos, int stopLevel, double minVolume, double maxVolume) {
		super();
		this.moneda = moneda;
		this.digitos = digitos;
		this.stopLevel = stopLevel;
		this.minVolume = minVolume;
		this.maxVolume = maxVolume;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public int getDigitos() {
		return digitos;
	}

	public void setDigitos(int digitos) {
		this.digitos = digitos;
	}

	public int getStopLevel() {
		return stopLevel;
	}

	public void setStopLevel(int stopLevel) {
		this.stopLevel = stopLevel;
	}

	public double getMinVolume() {
		return minVolume;
	}

	public void setMinVolume(double minVolume) {
		this.minVolume = minVolume;
	}

	public double getMaxVolume() {
		return maxVolume;
	}

	public void setMaxVolume(double maxVolume) {
		this.maxVolume = maxVolume;
	}

}

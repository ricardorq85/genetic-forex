package forex.genetic.entities;

public class Extremos {
	private DoubleInterval extremosFiltrados;
	private DoubleInterval extremosIntermedios;
	private DoubleInterval extremosExtremo;
	private DoubleInterval extremos;
	private Regresion maximaRegresionFiltrada;

	public Extremos(DoubleInterval extremos, DoubleInterval extremosIntermedios, DoubleInterval extremosExtremo,
			DoubleInterval extremosSinFiltrar) {
		super();
		this.extremosFiltrados = extremos;
		this.extremosIntermedios = extremosIntermedios;
		this.extremosExtremo = extremosExtremo;
		this.extremos = extremosSinFiltrar;
	}

	public DoubleInterval getExtremos() {
		return extremos;
	}

	public void setExtremos(DoubleInterval extremos) {
		this.extremos = extremos;
	}

	public DoubleInterval getExtremosFiltrados() {
		return extremosFiltrados;
	}

	public void setExtremosFiltrados(DoubleInterval extremosFiltrados) {
		this.extremosFiltrados = extremosFiltrados;
	}

	public DoubleInterval getExtremosIntermedios() {
		return extremosIntermedios;
	}

	public void setExtremosIntermedios(DoubleInterval extremosIntermedios) {
		this.extremosIntermedios = extremosIntermedios;
	}

	public DoubleInterval getExtremosExtremo() {
		return extremosExtremo;
	}

	public void setExtremosExtremo(DoubleInterval extremosExtremo) {
		this.extremosExtremo = extremosExtremo;
	}

	public Regresion getMaximaRegresionFiltrada() {
		return maximaRegresionFiltrada;
	}

	public void setMaximaRegresionFiltrada(Regresion maximaRegresionFiltrada) {
		this.maximaRegresionFiltrada = maximaRegresionFiltrada;
	}

}

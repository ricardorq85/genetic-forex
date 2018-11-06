package forex.genetic.entities;

public class Extremos {
	private DoubleInterval extremosExtremo;
	private DoubleInterval extremosFiltrados;
	private DoubleInterval extremosIntermedios;	
	private DoubleInterval extremosSinFiltrar;
	private DoubleInterval extremosPrimeraTendencia;
	private Regresion maximaRegresionFiltradaBuy, maximaRegresionFiltradaSell;
	private Regresion maximaRegresionJavaFiltradaBuy, maximaRegresionJavaFiltradaSell;
	
	public Extremos(DoubleInterval extremosFiltrados, DoubleInterval extremosIntermedios, DoubleInterval extremosExtremo,
			DoubleInterval extremosSinFiltrar) {
		super();
		this.extremosFiltrados = extremosFiltrados;
		this.extremosIntermedios = extremosIntermedios;
		this.extremosExtremo = extremosExtremo;
		this.extremosSinFiltrar = extremosSinFiltrar;
	}

	public DoubleInterval getExtremosSinFiltrar() {
		return extremosSinFiltrar;
	}

	public void setExtremosSinFiltrar(DoubleInterval extremosSinFiltrar) {
		this.extremosSinFiltrar = extremosSinFiltrar;
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

	public Regresion getMaximaRegresionFiltradaBuy() {
		return maximaRegresionFiltradaBuy;
	}

	public void setMaximaRegresionFiltradaBuy(Regresion maximaRegresionFiltradaBuy) {
		this.maximaRegresionFiltradaBuy = maximaRegresionFiltradaBuy;
	}

	public Regresion getMaximaRegresionFiltradaSell() {
		return maximaRegresionFiltradaSell;
	}

	public void setMaximaRegresionFiltradaSell(Regresion maximaRegresionFiltradaSell) {
		this.maximaRegresionFiltradaSell = maximaRegresionFiltradaSell;
	}

	public DoubleInterval getExtremosPrimeraTendencia() {
		return extremosPrimeraTendencia;
	}

	public void setExtremosPrimeraTendencia(DoubleInterval extremosPrimeraTendencia) {
		this.extremosPrimeraTendencia = extremosPrimeraTendencia;
	}

	public Regresion getMaximaRegresionJavaFiltradaSell() {
		return maximaRegresionJavaFiltradaSell;
	}

	public void setMaximaRegresionJavaFiltradaSell(Regresion maximaRegresionJavaFiltradaSell) {
		this.maximaRegresionJavaFiltradaSell = maximaRegresionJavaFiltradaSell;
	}

	public Regresion getMaximaRegresionJavaFiltradaBuy() {
		return maximaRegresionJavaFiltradaBuy;
	}

	public void setMaximaRegresionJavaFiltradaBuy(Regresion maximaRegresionJavaFiltradaBuy) {
		this.maximaRegresionJavaFiltradaBuy = maximaRegresionJavaFiltradaBuy;
	}

}

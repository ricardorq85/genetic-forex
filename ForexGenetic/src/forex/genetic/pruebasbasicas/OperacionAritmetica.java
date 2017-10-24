package forex.genetic.pruebasbasicas;

public abstract class OperacionAritmetica {
	protected double valor1;
	protected double valor2;

	public OperacionAritmetica(double x, double y) {
		this.valor1 = x;
		this.valor2 = y;
	}

	public abstract double realizarOperacion();

	public void metodoNoAbstracto() {

	}

}

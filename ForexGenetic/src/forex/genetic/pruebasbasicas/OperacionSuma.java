package forex.genetic.pruebasbasicas;

public class OperacionSuma extends OperacionAritmetica {

	public OperacionSuma(double x, double y) {
		super(x, y);
	}

	@Override
	public double realizarOperacion() {
		return (super.valor1 + super.valor2);
	}

}

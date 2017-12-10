package forex.genetic.pruebasbasicas;

public class OperacionMultiplicacion extends OperacionAritmetica {

	public OperacionMultiplicacion(double x, double y) {
		super(x, y);
	}

	@Override
	public double realizarOperacion() {
		return (super.valor1 * super.valor2);
	}

}

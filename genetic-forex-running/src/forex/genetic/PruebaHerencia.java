package forex.genetic;

import forex.genetic.pruebasbasicas.OperacionAritmetica;
import forex.genetic.pruebasbasicas.OperacionMultiplicacion;
import forex.genetic.pruebasbasicas.OperacionSuma;

public class PruebaHerencia {
	public static void main(String[] param) {
		double y = 10;
		double x = 2;
		OperacionAritmetica suma = new OperacionSuma(x, y);
		OperacionAritmetica multiplicacion = new OperacionMultiplicacion(x, y);

		operar(suma);
		operar(multiplicacion);

	}

	private static void operar(OperacionAritmetica operacion) {
		double resultado = operacion.realizarOperacion();
		System.out.println("Resultado=" + resultado);
	}
}

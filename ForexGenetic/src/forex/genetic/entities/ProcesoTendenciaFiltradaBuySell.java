/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.util.Date;
import java.util.List;

import forex.genetic.util.Constants.OperationType;

/**
 *
 * @author ricardorq85
 */
public class ProcesoTendenciaFiltradaBuySell extends ProcesoTendenciaBuySell {

	private Regresion regresionFiltrada;

	public ProcesoTendenciaFiltradaBuySell(String periodo2, String tipoTendencia2, double tiempoTendencia2,
			Date fechaBase2) {
		super(periodo2, tipoTendencia2, tiempoTendencia2, fechaBase2);
	}

	public Regresion getRegresionFiltrada() {
		return regresionFiltrada;
	}

	public void setRegresionFiltrada(Regresion regresionFiltrada) {
		this.regresionFiltrada = regresionFiltrada;
	}

	public boolean isRegresionValida() {
		return ((super.isRegresionValida()) && (this.regresionFiltrada != null)
				&& (this.regresionFiltrada.isRegresionValida()));
	}

}

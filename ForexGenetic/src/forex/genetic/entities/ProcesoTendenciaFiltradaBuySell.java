/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.util.Date;

/**
 *
 * @author ricardorq85
 */
public class ProcesoTendenciaFiltradaBuySell extends ProcesoTendenciaBuySell {

	public ProcesoTendenciaFiltradaBuySell(String periodo2, String tipoTendencia2, double tiempoTendencia2,
			Date fechaBase2) {
		super(periodo2, tipoTendencia2, tiempoTendencia2, fechaBase2);
	}

	public boolean isRegresionValida() {
		return ((super.isRegresionValida()) && (super.getRegresionFiltrada() != null)
				&& (super.getRegresionFiltrada().isRegresionValida()));
	}

	public boolean isValidacionRegresionValida() {
		return ((super.isValidacionRegresionValida()) && (super.getRegresionFiltrada() != null)
				&& (super.getRegresionFiltrada().isValidacionRegresionValida()));
	}

	public boolean isCantidadMinimaValida() {
		if (super.getRegresion() == null) {
			return false;
		}
		if (super.getRegresion().getCantidad() == 0) {
			return false;
		}
		if (super.getRegresionFiltrada() == null) {
			return false;
		}
		float cantidadRegresion = super.getRegresion().getCantidad();
		float cantidadRegresionFiltrada = super.getRegresionFiltrada().getCantidad();
		float valorCantidad = (cantidadRegresionFiltrada / cantidadRegresion);

		return (valorCantidad > 0.5);
	}

}

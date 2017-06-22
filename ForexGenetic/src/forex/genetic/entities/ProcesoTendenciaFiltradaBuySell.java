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

	public boolean isValidacionRegresionValida() {
		return ((super.isValidacionRegresionValida()) && (this.regresionFiltrada != null)
				&& (this.regresionFiltrada.isValidacionRegresionValida()));
	}

	public boolean isCantidadMinimaValida() {
		if (super.getRegresion() == null) {
			return false;
		}
		if (super.getRegresion().getCantidad() == 0) {
			return false;
		}
		if (this.regresionFiltrada == null) {
			return false;
		}
		float cantidadRegresion = super.getRegresion().getCantidad();
		float cantidadRegresionFiltrada = this.regresionFiltrada.getCantidad();
		float valorCantidad = (cantidadRegresionFiltrada / cantidadRegresion);

		return (valorCantidad > 0.5);
	}

}

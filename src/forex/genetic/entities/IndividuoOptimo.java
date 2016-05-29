/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

/**
 *
 * @author ricardorq85
 */
public class IndividuoOptimo extends Individuo {

    private int cantidadTotal = 0;
    private double factorPips = 0;
    private double factorCantidad = 0;

    public boolean isActive() {
        return true;
    }

    public int getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(int cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public double getFactorPips() {
        return factorPips;
    }

    public void setFactorPips(double factorPips) {
        this.factorPips = factorPips;
    }

    public double getFactorCantidad() {
        return factorCantidad;
    }

    public void setFactorCantidad(double factorCantidad) {
        this.factorCantidad = factorCantidad;
    }
}

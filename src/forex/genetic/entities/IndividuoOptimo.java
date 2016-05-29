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
public class IndividuoOptimo extends Individuo {

    private String nombreEstrategia = null;
    private Date fechaVigencia = null;
    private int minutosFuturo = 0;
    private int cantidadTotal = 0;
    private double factorPips = 0;
    private double factorCantidad = 0;

    public String getNombreEstrategia() {
        return nombreEstrategia;
    }

    public void setNombreEstrategia(String nombreEstrategia) {
        this.nombreEstrategia = nombreEstrategia;
    }

    public int getMinutosFuturo() {
        return minutosFuturo;
    }

    public void setMinutosFuturo(int minutosFuturo) {
        this.minutosFuturo = minutosFuturo;
    }

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

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

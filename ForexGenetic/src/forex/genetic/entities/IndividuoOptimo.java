/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author ricardorq85
 */
public class IndividuoOptimo extends Individuo {

    /**
     *
     */
    public static final long serialVersionUID = 201502161520L;
    private String nombreEstrategia = null;
    private Date fechaVigencia = null;
    private int minutosFuturo = 0;
    private int cantidadTotal = 0;
    private double factorPips = 0;
    private double factorCantidad = 0;

    /**
     *
     * @return
     */
    public String getNombreEstrategia() {
        return nombreEstrategia;
    }

    /**
     *
     * @param nombreEstrategia
     */
    public void setNombreEstrategia(String nombreEstrategia) {
        this.nombreEstrategia = nombreEstrategia;
    }

    /**
     *
     * @return
     */
    public int getMinutosFuturo() {
        return minutosFuturo;
    }

    /**
     *
     * @param minutosFuturo
     */
    public void setMinutosFuturo(int minutosFuturo) {
        this.minutosFuturo = minutosFuturo;
    }

    /**
     *
     * @return
     */
    public Date getFechaVigencia() {
        return (this.fechaVigencia = fechaVigencia != null ? new Date(fechaVigencia.getTime()) : null);
    }

    /**
     *
     * @param fechaVigencia
     */
    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia != null ? new Date(fechaVigencia.getTime()) : null;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isActive() {
        return true;
    }

    /**
     *
     * @return
     */
    public int getCantidadTotal() {
        return cantidadTotal;
    }

    /**
     *
     * @param cantidadTotal
     */
    public void setCantidadTotal(int cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    /**
     *
     * @return
     */
    public double getFactorPips() {
        return factorPips;
    }

    /**
     *
     * @param factorPips
     */
    public void setFactorPips(double factorPips) {
        this.factorPips = factorPips;
    }

    /**
     *
     * @return
     */
    public double getFactorCantidad() {
        return factorCantidad;
    }

    /**
     *
     * @param factorCantidad
     */
    public void setFactorCantidad(double factorCantidad) {
        this.factorCantidad = factorCantidad;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return super.equals(obj);
    }

}

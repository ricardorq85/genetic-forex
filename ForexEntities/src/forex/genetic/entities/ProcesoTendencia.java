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
public class ProcesoTendencia {

    private Date fechaBase = null;
    private String tipo = null;
    private Date fechaBaseFin = null;
    private Interval<Double> intervaloPrecio = null;
    private double valorMasProbable = 0.0D;
    private double pipsMasProbable = 0.0D;
    private int cantidad = 0;
    private Interval<Date> intervaloFecha = null;
    private double precioBasePromedio = 0.0D;
    private double probabilidad = 0.0D;

    /**
     *
     * @return
     */
    public double getProbabilidad() {
        return probabilidad;
    }

    /**
     *
     * @param probabilidad
     */
    public void setProbabilidad(double probabilidad) {
        this.probabilidad = probabilidad;
    }

    /**
     *
     * @return
     */
    public Date getFechaBase() {
        return (this.fechaBase = fechaBase != null ? new Date(fechaBase.getTime()) : null);
    }

    /**
     *
     * @param fechaBase
     */
    public void setFechaBase(Date fechaBase) {
        this.fechaBase = fechaBase != null ? new Date(fechaBase.getTime()) : null;
    }

    /**
     *
     * @return
     */
    public String getTipo() {
        return tipo;
    }

    /**
     *
     * @param tipo
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     *
     * @return
     */
    public Date getFechaBaseFin() {
        return (this.fechaBaseFin = fechaBaseFin != null ? new Date(fechaBaseFin.getTime()) : null);
    }

    /**
     *
     * @param fechaBaseFin
     */
    public void setFechaBaseFin(Date fechaBaseFin) {
        this.fechaBaseFin = fechaBaseFin != null ? new Date(fechaBaseFin.getTime()) : null;
    }

    /**
     *
     * @return
     */
    public double getPrecioBasePromedio() {
        return precioBasePromedio;
    }

    /**
     *
     * @param precioBasePromedio
     */
    public void setPrecioBasePromedio(double precioBasePromedio) {
        this.precioBasePromedio = precioBasePromedio;
    }

    /**
     *
     * @return
     */
    public double getPipsMasProbable() {
        return pipsMasProbable;
    }

    /**
     *
     * @param pipsMasProbable
     */
    public void setPipsMasProbable(double pipsMasProbable) {
        this.pipsMasProbable = pipsMasProbable;
    }

    /**
     *
     * @return
     */
    public Interval<Date> getIntervaloFecha() {
        return intervaloFecha;
    }

    /**
     *
     * @param intervaloFecha
     */
    public void setIntervaloFecha(Interval<Date> intervaloFecha) {
        this.intervaloFecha = intervaloFecha;
    }

    /**
     *
     * @return
     */
    public Interval<Double> getIntervaloPrecio() {
        return intervaloPrecio;
    }

    /**
     *
     * @param intervaloPrecio
     */
    public void setIntervaloPrecio(Interval<Double> intervaloPrecio) {
        this.intervaloPrecio = intervaloPrecio;
    }

    /**
     *
     * @return
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     *
     * @param cantidad
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     *
     * @return
     */
    public double getValorMasProbable() {
        return valorMasProbable;
    }

    /**
     *
     * @param valorMasProbable
     */
    public void setValorMasProbable(double valorMasProbable) {
        this.valorMasProbable = valorMasProbable;
    }

    @Override
    public String toString() {
        return "ProcesoTendencia{" + "fechaBase=" + fechaBase + ", tipo=" + tipo + ", fechaBaseFin=" + fechaBaseFin + ", "
                + " \n intervaloPrecio=" + intervaloPrecio + ", valorMasProbable=" + valorMasProbable + ", pipsMasProbable=" + pipsMasProbable + ", cantidad=" + cantidad + ", intervaloFecha=" + intervaloFecha + ", precioBasePromedio=" + precioBasePromedio + ", probabilidad=" + probabilidad + '}';
    }

}

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

    public double getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(double probabilidad) {
        this.probabilidad = probabilidad;
    }

    public Date getFechaBase() {
        return fechaBase;
    }

    public void setFechaBase(Date fechaBase) {
        this.fechaBase = fechaBase;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getFechaBaseFin() {
        return fechaBaseFin;
    }

    public void setFechaBaseFin(Date fechaBaseFin) {
        this.fechaBaseFin = fechaBaseFin;
    }

    public double getPrecioBasePromedio() {
        return precioBasePromedio;
    }

    public void setPrecioBasePromedio(double precioBasePromedio) {
        this.precioBasePromedio = precioBasePromedio;
    }

    public double getPipsMasProbable() {
        return pipsMasProbable;
    }

    public void setPipsMasProbable(double pipsMasProbable) {
        this.pipsMasProbable = pipsMasProbable;
    }

    public Interval<Date> getIntervaloFecha() {
        return intervaloFecha;
    }

    public void setIntervaloFecha(Interval<Date> intervaloFecha) {
        this.intervaloFecha = intervaloFecha;
    }

    public Interval<Double> getIntervaloPrecio() {
        return intervaloPrecio;
    }

    public void setIntervaloPrecio(Interval<Double> intervaloPrecio) {
        this.intervaloPrecio = intervaloPrecio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getValorMasProbable() {
        return valorMasProbable;
    }

    public void setValorMasProbable(double valorMasProbable) {
        this.valorMasProbable = valorMasProbable;
    }

    @Override
    public String toString() {
        return "ProcesoTendencia{" + "fechaBase=" + fechaBase + ", tipo=" + tipo + ", fechaBaseFin=" + fechaBaseFin + ", "
                + " \n intervaloPrecio=" + intervaloPrecio + ", valorMasProbable=" + valorMasProbable + ", pipsMasProbable=" + pipsMasProbable + ", cantidad=" + cantidad + ", intervaloFecha=" + intervaloFecha + ", precioBasePromedio=" + precioBasePromedio + ", probabilidad=" + probabilidad + '}';
    }
    
    
    
}

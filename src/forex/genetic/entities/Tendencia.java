/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ricardorq85
 */
public class Tendencia implements Serializable {

    public static final long serialVersionUID = 201208071601L;
    private double tendencia = 0.0D;
    private Date fechaBase;
    private double precioBase;
    private Individuo individuo;
    private Date fechaTendencia;
    private double pips;
    private double precioCalculado;
    private String tipoTendencia = null;
    private Date fechaApertura;
    private double precioApertura;
    private long duracion;
    private double pipsActuales;
    private long duracionActual;
    private double probabilidadPositivos;
    private double probabilidadNegativos;
    private Date fecha;
    private double probabilidad;

    public Tendencia() {
    }

    public Tendencia(double tendencia) {
        this.tendencia = tendencia;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(double probabilidad) {
        this.probabilidad = probabilidad;
    }
    
    public long getDuracionActual() {
        return duracionActual;
    }

    public void setDuracionActual(long duracionActual) {
        this.duracionActual = duracionActual;
    }

    public double getPipsActuales() {
        return pipsActuales;
    }

    public void setPipsActuales(double pipsActuales) {
        this.pipsActuales = pipsActuales;
    }

    public long getDuracion() {
        return duracion;
    }

    public void setDuracion(long duracion) {
        this.duracion = duracion;
    }

    public String getTipoTendencia() {
        return tipoTendencia;
    }

    public void setTipoTendencia(String tipoTendencia) {
        this.tipoTendencia = tipoTendencia;
    }

    public double getTendencia() {
        return tendencia;
    }

    public void setTendencia(double tendencia) {
        this.tendencia = tendencia;
    }

    public Date getFechaBase() {
        return fechaBase;
    }

    public void setFechaBase(Date fechaBase) {
        this.fechaBase = fechaBase;
    }

    public Date getFechaTendencia() {
        return fechaTendencia;
    }

    public void setFechaTendencia(Date fechaTendencia) {
        this.fechaTendencia = fechaTendencia;
    }

    public Individuo getIndividuo() {
        return individuo;
    }

    public void setIndividuo(Individuo individuo) {
        this.individuo = individuo;
    }

    public double getPips() {
        return pips;
    }

    public void setPips(double pips) {
        this.pips = pips;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public double getPrecioCalculado() {
        return precioCalculado;
    }

    public void setPrecioCalculado(double precioCalculado) {
        this.precioCalculado = precioCalculado;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public double getPrecioApertura() {
        return precioApertura;
    }

    public void setPrecioApertura(double precioApertura) {
        this.precioApertura = precioApertura;
    }

    public double getProbabilidadNegativos() {
        return probabilidadNegativos;
    }

    public void setProbabilidadNegativos(double probabilidadNegativos) {
        this.probabilidadNegativos = probabilidadNegativos;
    }

    public double getProbabilidadPositivos() {
        return probabilidadPositivos;
    }

    public void setProbabilidadPositivos(double probabilidadPositivos) {
        this.probabilidadPositivos = probabilidadPositivos;
    }
}

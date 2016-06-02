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

    /**
     *
     */
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
    private Date fechaCierre;
    private int tipoCalculo;
    private double pipsReales;

    /**
     *
     * @return
     */
    public double getPipsReales() {
        return pipsReales;
    }

    /**
     *
     * @param pipsReales
     */
    public void setPipsReales(double pipsReales) {
        this.pipsReales = pipsReales;
    }

    /**
     *
     * @return
     */
    public int getTipoCalculo() {
        return tipoCalculo;
    }

    /**
     *
     * @param tipoCalculo
     */
    public void setTipoCalculo(int tipoCalculo) {
        this.tipoCalculo = tipoCalculo;
    }

    /**
     *
     */
    public Tendencia() {
    }

    /**
     *
     * @param tendencia
     */
    public Tendencia(double tendencia) {
        this.tendencia = tendencia;
    }

    /**
     *
     * @return
     */
    public Date getFechaCierre() {
        return (this.fechaCierre = fechaCierre != null ? new Date(fechaCierre.getTime()) : null);
    }

    /**
     *
     * @param fechaCierre
     */
    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre != null ? new Date(fechaCierre.getTime()) : null;
    }

    /**
     *
     * @return
     */
    public Date getFecha() {
        return (this.fecha = fecha != null ? new Date(fecha.getTime()) : null);
    }

    /**
     *
     * @param fecha
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha != null ? new Date(fecha.getTime()) : null;
    }

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
    public long getDuracionActual() {
        return duracionActual;
    }

    /**
     *
     * @param duracionActual
     */
    public void setDuracionActual(long duracionActual) {
        this.duracionActual = duracionActual;
    }

    /**
     *
     * @return
     */
    public double getPipsActuales() {
        return pipsActuales;
    }

    /**
     *
     * @param pipsActuales
     */
    public void setPipsActuales(double pipsActuales) {
        this.pipsActuales = pipsActuales;
    }

    /**
     *
     * @return
     */
    public long getDuracion() {
        return duracion;
    }

    /**
     *
     * @param duracion
     */
    public void setDuracion(long duracion) {
        this.duracion = duracion;
    }

    /**
     *
     * @return
     */
    public String getTipoTendencia() {
        return tipoTendencia;
    }

    /**
     *
     * @param tipoTendencia
     */
    public void setTipoTendencia(String tipoTendencia) {
        this.tipoTendencia = tipoTendencia;
    }

    /**
     *
     * @return
     */
    public double getTendencia() {
        return tendencia;
    }

    /**
     *
     * @param tendencia
     */
    public void setTendencia(double tendencia) {
        this.tendencia = tendencia;
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
    public Date getFechaTendencia() {
        return (this.fechaTendencia = fechaTendencia != null ? new Date(fechaTendencia.getTime()) : null);
    }

    /**
     *
     * @param fechaTendencia
     */
    public void setFechaTendencia(Date fechaTendencia) {
        this.fechaTendencia = fechaTendencia != null ? new Date(fechaTendencia.getTime()) : null;
    }

    /**
     *
     * @return
     */
    public Individuo getIndividuo() {
        return individuo;
    }

    /**
     *
     * @param individuo
     */
    public void setIndividuo(Individuo individuo) {
        this.individuo = individuo;
    }

    /**
     *
     * @return
     */
    public double getPips() {
        return pips;
    }

    /**
     *
     * @param pips
     */
    public void setPips(double pips) {
        this.pips = pips;
    }

    /**
     *
     * @return
     */
    public double getPrecioBase() {
        return precioBase;
    }

    /**
     *
     * @param precioBase
     */
    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    /**
     *
     * @return
     */
    public double getPrecioCalculado() {
        return precioCalculado;
    }

    /**
     *
     * @param precioCalculado
     */
    public void setPrecioCalculado(double precioCalculado) {
        this.precioCalculado = precioCalculado;
    }

    /**
     *
     * @return
     */
    public Date getFechaApertura() {
        return (this.fechaApertura = fechaApertura != null ? new Date(fechaApertura.getTime()) : null);
    }

    /**
     *
     * @param fechaApertura
     */
    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura != null ? new Date(fechaApertura.getTime()) : null;
    }

    /**
     *
     * @return
     */
    public double getPrecioApertura() {
        return precioApertura;
    }

    /**
     *
     * @param precioApertura
     */
    public void setPrecioApertura(double precioApertura) {
        this.precioApertura = precioApertura;
    }

    /**
     *
     * @return
     */
    public double getProbabilidadNegativos() {
        return probabilidadNegativos;
    }

    /**
     *
     * @param probabilidadNegativos
     */
    public void setProbabilidadNegativos(double probabilidadNegativos) {
        this.probabilidadNegativos = probabilidadNegativos;
    }

    /**
     *
     * @return
     */
    public double getProbabilidadPositivos() {
        return probabilidadPositivos;
    }

    /**
     *
     * @param probabilidadPositivos
     */
    public void setProbabilidadPositivos(double probabilidadPositivos) {
        this.probabilidadPositivos = probabilidadPositivos;
    }

    @Override
    public String toString() {
        return "Tendencia{" + "tendencia=" + ", individuo=" + individuo + tendencia + ", fechaBase=" + fechaBase + ", precioBase=" + precioBase + ", fechaTendencia=" + fechaTendencia + ", pips=" + pips + ", precioCalculado=" + precioCalculado + ", tipoTendencia=" + tipoTendencia + ", fechaApertura=" + fechaApertura + ", precioApertura=" + precioApertura + ", duracion=" + duracion + ", pipsActuales=" + pipsActuales + ", duracionActual=" + duracionActual + ", probabilidadPositivos=" + probabilidadPositivos + ", probabilidadNegativos=" + probabilidadNegativos + ", fecha=" + fecha + ", probabilidad=" + probabilidad + ", fechaCierre=" + fechaCierre + ", tipoCalculo=" + tipoCalculo + '}';
    }

}

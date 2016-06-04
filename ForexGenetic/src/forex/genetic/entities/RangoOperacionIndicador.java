/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.util.Date;
import java.util.Objects;

import forex.genetic.entities.indicator.Indicator;
import forex.genetic.util.DateUtil;

/**
 *
 * @author ricardorq85
 */
public class RangoOperacionIndicador {

    private String idIndicator;
    private double pips;
    private double retroceso;
    private Date fechaFiltro;
    private Indicator indicador;
    private int takeProfit;
    private int stopLoss;
    private int cantidad;
    double porcentajeCumplimiento;
    double promedio;
    private Date fechaFiltro2;

    public boolean cumplePorcentajeIndicador() {
        return (porcentajeCumplimiento > 0) && (porcentajeCumplimiento < 0.7) && (porcentajeCumplimiento > 0.1);
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public Date getFechaFiltro() {
        return fechaFiltro;
    }

    public void setFechaFiltro(Date fechaFiltro) {
        this.fechaFiltro = fechaFiltro;
    }

    public double getPorcentajeCumplimiento() {
        return porcentajeCumplimiento;
    }

    public void setPorcentajeCumplimiento(double porcentajeCumplimiento) {
        this.porcentajeCumplimiento = porcentajeCumplimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(int stopLoss) {
        this.stopLoss = stopLoss;
    }

    public int getTakeProfit() {
        return takeProfit;
    }

    public void setTakeProfit(int takeProfit) {
        this.takeProfit = takeProfit;
    }

    public String getIdIndicator() {
        return idIndicator;
    }

    public void setIdIndicator(String idIndicator) {
        this.idIndicator = idIndicator;
    }

    public double getPips() {
        return pips;
    }

    public void setPips(double pips) {
        this.pips = pips;
    }

    public double getRetroceso() {
        return retroceso;
    }

    public void setRetroceso(double retroceso) {
        this.retroceso = retroceso;
    }

    public Indicator getIndicador() {
        return indicador;
    }

    public void setIndicador(Indicator indicador) {
        this.indicador = indicador;
    }

    public void setFechaFiltro2(Date fechaFiltro2) {
        this.fechaFiltro2 = fechaFiltro2;
    }

    public Date getFechaFiltro2() {
        return this.fechaFiltro2;
    }

    @Override
    public String toString() {
        return "RangoOperacionIndicador{" + "idIndicator=" + idIndicator + ", pips=" + pips
                + ", retroceso=" + retroceso
                + ", fechaFiltro=" + DateUtil.getDateString(fechaFiltro)
                + ", fechaFiltro2=" + DateUtil.getDateString(fechaFiltro2)
                + ", indicador=" + indicador + ", takeProfit=" + takeProfit + ", stopLoss=" + stopLoss + ", cantidad=" + cantidad + ", porcentajeCumplimiento=" + porcentajeCumplimiento + ", promedio=" + promedio + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.idIndicator);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RangoOperacionIndicador other = (RangoOperacionIndicador) obj;
        return (this.idIndicator.equals(other.idIndicator));
    }

}

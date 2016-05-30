/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

/**
 *
 * @author USER
 */
public class ParametroTendenciaGenetica {

    private int horas = 1;
    private int minutos = 1;
    private int rangoTendenciaMinutos = 1;
    private int pipsMinimos = 0;
    private int cantidadRegistroIndividuosMinimos = 0;
    private int cantidadTotalIndividuosMinimos = 0;
    private int horasFechaTendencia = 1;
    private int minutosFechaTendencia = 1;
    private int horasFechaApertura = 1;
    private int minutosFechaApertura = 1;

    public ParametroTendenciaGenetica() {
    }

    /**
     * @return the horas
     */
    public int getHoras() {
        return horas;
    }

    /**
     * @param horas the horas to set
     */
    public void setHoras(int horas) {
        this.horas = horas;
    }

    /**
     * @return the minutos
     */
    public int getMinutos() {
        return minutos;
    }

    /**
     * @param minutos the minutos to set
     */
    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    /**
     * @return the rangoTendenciaMinutos
     */
    public int getRangoTendenciaMinutos() {
        return rangoTendenciaMinutos;
    }

    /**
     * @param rangoTendenciaMinutos the rangoTendenciaMinutos to set
     */
    public void setRangoTendenciaMinutos(int rangoTendenciaMinutos) {
        this.rangoTendenciaMinutos = rangoTendenciaMinutos;
    }

    /**
     * @return the pipsMinimos
     */
    public int getPipsMinimos() {
        return pipsMinimos;
    }

    /**
     * @param pipsMinimos the pipsMinimos to set
     */
    public void setPipsMinimos(int pipsMinimos) {
        this.pipsMinimos = pipsMinimos;
    }

    /**
     * @return the cantidadRegistroIndividuosMinimos
     */
    public int getCantidadRegistroIndividuosMinimos() {
        return cantidadRegistroIndividuosMinimos;
    }

    /**
     * @param cantidadRegistroIndividuosMinimos the
     * cantidadRegistroIndividuosMinimos to set
     */
    public void setCantidadRegistroIndividuosMinimos(int cantidadRegistroIndividuosMinimos) {
        this.cantidadRegistroIndividuosMinimos = cantidadRegistroIndividuosMinimos;
    }

    /**
     * @return the cantidadTotalIndividuosMinimos
     */
    public int getCantidadTotalIndividuosMinimos() {
        return cantidadTotalIndividuosMinimos;
    }

    /**
     * @param cantidadTotalIndividuosMinimos the cantidadTotalIndividuosMinimos
     * to set
     */
    public void setCantidadTotalIndividuosMinimos(int cantidadTotalIndividuosMinimos) {
        this.cantidadTotalIndividuosMinimos = cantidadTotalIndividuosMinimos;
    }

    /**
     * @return the horasFechaTendencia
     */
    public int getHorasFechaTendencia() {
        return horasFechaTendencia;
    }

    /**
     * @param horasFechaTendencia the horasFechaTendencia to set
     */
    public void setHorasFechaTendencia(int horasFechaTendencia) {
        this.horasFechaTendencia = horasFechaTendencia;
    }

    /**
     * @return the minutosFechaTendencia
     */
    public int getMinutosFechaTendencia() {
        return minutosFechaTendencia;
    }

    /**
     * @param minutosFechaTendencia the minutosFechaTendencia to set
     */
    public void setMinutosFechaTendencia(int minutosFechaTendencia) {
        this.minutosFechaTendencia = minutosFechaTendencia;
    }

    /**
     * @return the horasFechaApertura
     */
    public int getHorasFechaApertura() {
        return horasFechaApertura;
    }

    /**
     * @param horasFechaApertura the horasFechaApertura to set
     */
    public void setHorasFechaApertura(int horasFechaApertura) {
        this.horasFechaApertura = horasFechaApertura;
    }

    /**
     * @return the minutosFechaApertura
     */
    public int getMinutosFechaApertura() {
        return minutosFechaApertura;
    }

    /**
     * @param minutosFechaApertura the minutosFechaApertura to set
     */
    public void setMinutosFechaApertura(int minutosFechaApertura) {
        this.minutosFechaApertura = minutosFechaApertura;
    }

    @Override
    public String toString() {
        return "ParametroTendenciaGenetica{" + "horas=" + horas + ", minutos=" + minutos + ", rangoTendenciaMinutos=" + rangoTendenciaMinutos + ", pipsMinimos=" + pipsMinimos + ", cantidadRegistroIndividuosMinimos=" + cantidadRegistroIndividuosMinimos + ", cantidadTotalIndividuosMinimos=" + cantidadTotalIndividuosMinimos + ", horasFechaTendencia=" + horasFechaTendencia + ", minutosFechaTendencia=" + minutosFechaTendencia + ", horasFechaApertura=" + horasFechaApertura + ", minutosFechaApertura=" + minutosFechaApertura + '}';
    }
    
}

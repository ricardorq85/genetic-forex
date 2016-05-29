/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

/**
 *
 * @author ricardorq85
 */
public class CalculoTendencia {

    private double pips = 0;
    private long duracion = 0L;
    private double probabilidadPositivos = 0;
    private double probabilidadNegativos = 0;

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

    public long getDuracion() {
        return duracion;
    }

    public void setDuracion(long duracion) {
        this.duracion = duracion;
    }

    public double getPips() {
        return pips;
    }

    public void setPips(double pips) {
        this.pips = pips;
    }
}

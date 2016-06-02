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

    @Override
    public String toString() {
        return "CalculoTendencia{" + "pips=" + pips + ", duracion=" + duracion + ", probabilidadPositivos=" + probabilidadPositivos + ", probabilidadNegativos=" + probabilidadNegativos + '}';
    }
}

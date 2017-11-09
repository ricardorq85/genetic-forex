/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

/**
 *
 * @author ricardorq85
 */
public class DiferenciaMaximaHistorico {

    private double movHistxMinuto = 0.0D;
    private double movHistxHora = 0.0D;
    private double movHistxDia = 0.0D;
    private double movHistxSemana = 0.0D;
    private double movHistxMes = 0.0D;

    /**
     *
     */
    public DiferenciaMaximaHistorico() {
    }    
    
    /**
     *
     * @return
     */
    public double getMovHistxMinuto() {
        return movHistxMinuto;
    }

    /**
     *
     * @param movHistxMinuto
     */
    public void setMovHistxMinuto(double movHistxMinuto) {
        this.movHistxMinuto = movHistxMinuto;
    }

    /**
     *
     * @return
     */
    public double getMovHistxHora() {
        return movHistxHora;
    }

    /**
     *
     * @param movHistxHora
     */
    public void setMovHistxHora(double movHistxHora) {
        this.movHistxHora = movHistxHora;
    }

    /**
     *
     * @return
     */
    public double getMovHistxDia() {
        return movHistxDia;
    }

    /**
     *
     * @param movHistxDia
     */
    public void setMovHistxDia(double movHistxDia) {
        this.movHistxDia = movHistxDia;
    }

    /**
     *
     * @return
     */
    public double getMovHistxSemana() {
        return movHistxSemana;
    }

    /**
     *
     * @param movHistxSemana
     */
    public void setMovHistxSemana(double movHistxSemana) {
        this.movHistxSemana = movHistxSemana;
    }

    /**
     *
     * @return
     */
    public double getMovHistxMes() {
        return movHistxMes;
    }

    /**
     *
     * @param movHistxMes
     */
    public void setMovHistxMes(double movHistxMes) {
        this.movHistxMes = movHistxMes;
    }

    
}

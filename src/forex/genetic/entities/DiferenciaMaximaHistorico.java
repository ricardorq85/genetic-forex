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

    public DiferenciaMaximaHistorico() {
    }    
    
    public double getMovHistxMinuto() {
        return movHistxMinuto;
    }

    public void setMovHistxMinuto(double movHistxMinuto) {
        this.movHistxMinuto = movHistxMinuto;
    }

    public double getMovHistxHora() {
        return movHistxHora;
    }

    public void setMovHistxHora(double movHistxHora) {
        this.movHistxHora = movHistxHora;
    }

    public double getMovHistxDia() {
        return movHistxDia;
    }

    public void setMovHistxDia(double movHistxDia) {
        this.movHistxDia = movHistxDia;
    }

    public double getMovHistxSemana() {
        return movHistxSemana;
    }

    public void setMovHistxSemana(double movHistxSemana) {
        this.movHistxSemana = movHistxSemana;
    }

    public double getMovHistxMes() {
        return movHistxMes;
    }

    public void setMovHistxMes(double movHistxMes) {
        this.movHistxMes = movHistxMes;
    }

    
}

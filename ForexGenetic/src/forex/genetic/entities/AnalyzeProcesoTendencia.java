/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

/**
 *
 * @author ricardorq85
 */
public class AnalyzeProcesoTendencia {

    ProcesoTendencia procesoTendencia = null;
    double stopLoss = 0.0D;

    /**
     *
     */
    public AnalyzeProcesoTendencia() {
    }

    /**
     *
     * @return
     */
    public ProcesoTendencia getProcesoTendencia() {
        return procesoTendencia;
    }

    /**
     *
     * @param procesoTendencia
     */
    public void setProcesoTendencia(ProcesoTendencia procesoTendencia) {
        this.procesoTendencia = procesoTendencia;
    }

    /**
     *
     * @return
     */
    public double getStopLoss() {
        return stopLoss;
    }

    /**
     *
     * @param stopLoss
     */
    public void setStopLoss(double stopLoss) {
        this.stopLoss = stopLoss;
    }

    @Override
    public String toString() {
        return "AnalyzeProcesoTendencia{" + "procesoTendencia=" + procesoTendencia + ", stopLoss=" + stopLoss + '}';
    }
   
    
}

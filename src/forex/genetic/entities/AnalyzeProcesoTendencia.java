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
public class AnalyzeProcesoTendencia {

    ProcesoTendencia procesoTendencia = null;
    double stopLoss = 0.0D;

    public AnalyzeProcesoTendencia() {
    }

    public ProcesoTendencia getProcesoTendencia() {
        return procesoTendencia;
    }

    public void setProcesoTendencia(ProcesoTendencia procesoTendencia) {
        this.procesoTendencia = procesoTendencia;
    }

    public double getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(double stopLoss) {
        this.stopLoss = stopLoss;
    }

    @Override
    public String toString() {
        return "AnalyzeProcesoTendencia{" + "procesoTendencia=" + procesoTendencia + ", stopLoss=" + stopLoss + '}';
    }
   
    
}

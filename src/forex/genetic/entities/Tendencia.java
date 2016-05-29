/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.io.Serializable;

/**
 *
 * @author ricardorq85
 */
public class Tendencia implements Serializable {

    public static final long serialVersionUID = 201208071601L;
    private double tendencia = 0.0D;

    public Tendencia() {
    }

    public Tendencia(double tendencia) {
        this.tendencia = tendencia;
    }

    public double getTendencia() {
        return tendencia;
    }

    public void setTendencia(double tendencia) {
        this.tendencia = tendencia;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.indicator;

import java.io.Serializable;

/**
 *
 * @author ricardorq85
 */
public class Sar extends IntervalIndicator implements Serializable {

    public static final long serialVersionUID = 201101251800L;
    private double sar = 0.0;
    private double parameter1 = 0.0;
    private double parameter2 = 0.0;

    public Sar(String name) {
        super(name);
    }

    public double getParameter1() {
        return parameter1;
    }

    public void setParameter1(double parameter1) {
        this.parameter1 = parameter1;
    }

    public double getParameter2() {
        return parameter2;
    }

    public void setParameter2(double parameter2) {
        this.parameter2 = parameter2;
    }

    public double getSar() {
        return sar;
    }

    public void setSar(double sar) {
        this.sar = sar;
    }
}

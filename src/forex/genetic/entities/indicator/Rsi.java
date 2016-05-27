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
public class Rsi extends IntervalIndicator implements Serializable {

    public static final long serialVersionUID = 201102142113L;
    private double rsi = 0.0;
    private double parameter1 = 0.0;

    public Rsi(String name) {
        super(name);
    }

    public double getParameter1() {
        return parameter1;
    }

    public void setParameter1(double parameter1) {
        this.parameter1 = parameter1;
    }

    public double getRsi() {
        return rsi;
    }

    public void setRsi(double rsi) {
        this.rsi = rsi;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rsi) {
            Rsi objRsi = (Rsi) obj;
            return (this.interval.equals(objRsi.interval));
        } else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

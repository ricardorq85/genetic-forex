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
public class Momentum extends IntervalIndicator implements Serializable {

    public static final long serialVersionUID = 201102142113L;
    private double momentum = 0.0;
    private double period = 0.0;

    public Momentum(String name) {
        super(name);
    }

    public double getMomentum() {
        return momentum;
    }

    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    public double getPeriod() {
        return period;
    }

    public void setPeriod(double period) {
        this.period = period;
    }

 
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Momentum) {
            Momentum objMomentum = (Momentum) obj;
            return (this.interval.equals(objMomentum.interval));
        } else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        return this.interval.hashCode();
    }
}

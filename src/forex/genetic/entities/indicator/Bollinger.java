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
public class Bollinger extends IntervalIndicator implements Serializable {

    public static final long serialVersionUID = 201102281800L;
    private double upper = 0.0;
    private double lower = 0.0;
    private double period = 0.0;
    private double desviation = 0.0;

    public Bollinger(String name) {
        super(name);
    }

    public double getDesviation() {
        return desviation;
    }

    public void setDesviation(double desviation) {
        this.desviation = desviation;
    }

    public double getLower() {
        return lower;
    }

    public void setLower(double lower) {
        this.lower = lower;
    }

    public double getUpper() {
        return upper;
    }

    public void setUpper(double upper) {
        this.upper = upper;
    }

    public double getPeriod() {
        return period;
    }

    public void setPeriod(double period) {
        this.period = period;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Bollinger) {
            Bollinger objMacd = (Bollinger) obj;
            return (this.interval.equals(objMacd.interval));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.indicator;

import forex.genetic.entities.DoubleInterval;
import forex.genetic.entities.Interval;
import java.io.Serializable;

/**
 *
 * @author ricardorq85
 */
public abstract class IntervalIndicator extends Indicator implements Serializable {

    public static final long serialVersionUID = -1166339844322682100L;
    protected Interval<Double> interval = null;
    protected String name = null;

    public IntervalIndicator(String name) {
        this.name = name;
        //interval = new DoubleInterval(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Interval<Double> getInterval() {
        return interval;
    }

    public void setInterval(Interval<Double> interval) {
        this.interval = interval;
    }

    public String toFileString(String prefix) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(prefix + this.interval.getName() + "Lower=" + (this.interval.getLowInterval() * 100) + ",");
        buffer.append(prefix + this.interval.getName() + "Higher=" + (this.interval.getHighInterval() * 100) + ",");

        return buffer.toString();
    }

    @Override
    public String toString() {
        return this.interval.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntervalIndicator) {
            IntervalIndicator objIndicator = (IntervalIndicator) obj;
            boolean value = (this.interval.equals(objIndicator.interval));
            return value;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.interval != null ? this.interval.hashCode() : 0);
        return hash;
    }
}

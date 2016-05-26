/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.indicator;

import forex.genetic.entities.Indicator;
import forex.genetic.entities.Interval;

/**
 *
 * @author ricardorq85
 */
public class Average extends Indicator {

    private double average = 0.0;
    private double parameter1 = 0.0;
    private Interval interval = new Interval();

    public double getParameter1() {
        return parameter1;
    }

    public void setParameter1(double parameter1) {
        this.parameter1 = parameter1;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Average) {
            Average objAvg = (Average) obj;
            return (this.interval.equals(objAvg.interval));
        } else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("\n\t\t{");
        buffer.append("Average=" + ((this.average == 0.0) ? 0.0 : this.average));
        buffer.append("\n\t\t");
        buffer.append("; " + (this.interval) + "}");

        return buffer.toString();
    }
}

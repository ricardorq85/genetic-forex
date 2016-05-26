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
public class Sar extends Indicator {

    private double sar = 0.0;
    private double parameter1 = 0.0;
    private double parameter2 = 0.0;
    private Interval interval = new Interval();

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

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Sar) {
            Sar objSar = (Sar) obj;
            return (this.interval.equals(objSar.interval));
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
        buffer.append("Sar=" + ((this.sar == 0.0) ? 0.0 : this.sar));
        buffer.append("\n\t\t");
        buffer.append("; " + (this.interval) + "}");

        return buffer.toString();
    }
}

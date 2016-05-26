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
public class Macd extends Indicator {

    private double macdValue = 0.0;
    private double macdSignal = 0.0;
    private double parameter1 = 0.0;
    private double parameter2 = 0.0;
    private double parameter3 = 0.0;
    private Interval interval = new Interval();

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public double getParameter3() {
        return parameter3;
    }

    public void setParameter3(double parameter3) {
        this.parameter3 = parameter3;
    }

    public double getMacdSignal() {
        return macdSignal;
    }

    public void setMacdSignal(double macdSignal) {
        this.macdSignal = macdSignal;
    }

    public double getMacdValue() {
        return macdValue;
    }

    public void setMacdValue(double macdValue) {
        this.macdValue = macdValue;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Macd) {
            Macd objMacd = (Macd) obj;
            return (this.interval.equals(objMacd.interval));
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
        buffer.append(" MACD Value=" + ((this.macdValue == 0.0) ? 0.0 : this.macdValue));
        buffer.append("; MACD Signal=" + ((this.macdSignal == 0.0) ? 0.0 : this.macdSignal));
        buffer.append("\n\t\t");
        buffer.append("; " + (this.interval) + "}");
        
        return buffer.toString();
    }
}

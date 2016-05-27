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
public class Macd extends IntervalIndicator implements Serializable {

    public static final long serialVersionUID = 201101251800L;
    private double macdValue = 0.0;
    private double macdSignal = 0.0;
    private double parameter1 = 0.0;
    private double parameter2 = 0.0;
    private double parameter3 = 0.0;

    public Macd(String name) {
        super(name);
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
}

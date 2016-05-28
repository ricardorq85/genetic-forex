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
public class Average extends IntervalIndicator implements Serializable {

    public static final long serialVersionUID = 201101251800L;
    private double average = 0.0;
    private double parameter1 = 0.0;

    public Average(String name) {
        super(name);
    }

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

}

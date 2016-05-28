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
public class Adx extends IntervalIndicator implements Serializable {

    public static final long serialVersionUID = 201102041228L;
    private double adxValue = 0.0;
    private double adxPlus = 0.0;
    private double adxMinus = 0.0;
    private double parameter1 = 0.0;

    public Adx(String name) {
        super(name);
    }

    public double getAdxMinus() {
        return adxMinus;
    }

    public void setAdxMinus(double adxMinus) {
        this.adxMinus = adxMinus;
    }

    public double getAdxPlus() {
        return adxPlus;
    }

    public void setAdxPlus(double adxPlus) {
        this.adxPlus = adxPlus;
    }

    public double getAdxValue() {
        return adxValue;
    }

    public void setAdxValue(double adxValue) {
        this.adxValue = adxValue;
    }

    public double getParameter1() {
        return parameter1;
    }

    public void setParameter1(double parameter1) {
        this.parameter1 = parameter1;
    }

}

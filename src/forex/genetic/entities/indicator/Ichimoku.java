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
public class Ichimoku extends IntervalIndicator implements Serializable {

    public static final long serialVersionUID = 201102041228L;
    private double tenkanSen = 0.0;
    private double kijunSen = 0.0;
    private double senkouSpanA = 0.0;
    private double senkouSpanB = 0.0;
    private double chinkouSpan = 0.0;

    public Ichimoku(String name) {
        super(name);
    }

    public double getChinkouSpan() {
        return chinkouSpan;
    }

    public void setChinkouSpan(double chinkouSpan) {
        this.chinkouSpan = chinkouSpan;
    }

    public double getKijunSen() {
        return kijunSen;
    }

    public void setKijunSen(double kijunSen) {
        this.kijunSen = kijunSen;
    }

    public double getSenkouSpanA() {
        return senkouSpanA;
    }

    public void setSenkouSpanA(double senkouSpanA) {
        this.senkouSpanA = senkouSpanA;
    }

    public double getSenkouSpanB() {
        return senkouSpanB;
    }

    public void setSenkouSpanB(double senkouSpanB) {
        this.senkouSpanB = senkouSpanB;
    }

    public double getTenkanSen() {
        return tenkanSen;
    }

    public void setTenkanSen(double tenkanSen) {
        this.tenkanSen = tenkanSen;
    }
}

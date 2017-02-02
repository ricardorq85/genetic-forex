/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.indicator;

/**
 *
 * @author ricardorq85
 */
public class Ichimoku extends IntervalIndicator {

    /**
     *
     */
    public static final long serialVersionUID = 201102041228L;
    private double tenkanSen = 0.0;
    private double kijunSen = 0.0;
    private double senkouSpanA = 0.0;
    private double senkouSpanB = 0.0;
    private double chinkouSpan = 0.0;

    /**
     *
     * @param name
     */
    public Ichimoku(String name) {
        super(name);
    }

    /**
     *
     * @return
     */
    public double getChinkouSpan() {
        return chinkouSpan;
    }

    /**
     *
     * @param chinkouSpan
     */
    public void setChinkouSpan(double chinkouSpan) {
        this.chinkouSpan = chinkouSpan;
    }

    /**
     *
     * @return
     */
    public double getKijunSen() {
        return kijunSen;
    }

    /**
     *
     * @param kijunSen
     */
    public void setKijunSen(double kijunSen) {
        this.kijunSen = kijunSen;
    }

    /**
     *
     * @return
     */
    public double getSenkouSpanA() {
        return senkouSpanA;
    }

    /**
     *
     * @param senkouSpanA
     */
    public void setSenkouSpanA(double senkouSpanA) {
        this.senkouSpanA = senkouSpanA;
    }

    /**
     *
     * @return
     */
    public double getSenkouSpanB() {
        return senkouSpanB;
    }

    /**
     *
     * @param senkouSpanB
     */
    public void setSenkouSpanB(double senkouSpanB) {
        this.senkouSpanB = senkouSpanB;
    }

    /**
     *
     * @return
     */
    public double getTenkanSen() {
        return tenkanSen;
    }

    /**
     *
     * @param tenkanSen
     */
    public void setTenkanSen(double tenkanSen) {
        this.tenkanSen = tenkanSen;
    }
}

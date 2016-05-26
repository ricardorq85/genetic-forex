/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.util.Date;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class Point {

    private int index = 0;
    private Date date = null;
    private double open = 0.0;
    private double low = 0.0;
    private double high = 0.0;
    private double close = 0.0;
    private int volume = 0;
    private double closeCompare = 0.0;
    private double min = 0.0;
    private double max = 0.0;
    private List<? extends Indicator> indicators = null;

    public Point() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getCloseCompare() {
        return closeCompare;
    }

    public void setCloseCompare(double closeCompare) {
        this.closeCompare = closeCompare;
    }

    public double getClose() {
        return close;
    }

    public double getWeihgted() {
        return ((this.getClose() + this.getOpen() + this.getHigh() + this.getLow()) / 4);
    }

    public void setClose(double close) {
        this.close = close;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<? extends Indicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<? extends Indicator> indicators) {
        this.indicators = indicators;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    @Override
    public String toString() {
        return "Index=" + this.index
                + " Date=" + this.date
                + " Open=" + this.open
                + " Close=" + this.close
                + " Low=" + this.low
                + " High=" + this.high;
    }
}

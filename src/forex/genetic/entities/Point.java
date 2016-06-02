/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import forex.genetic.entities.indicator.Indicator;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ricardorq85
 */
public class Point implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 201203120716L;
    private int index = 0;
    private Date date = null;
    private double open = 0.0;
    private double low = 0.0;
    private double high = 0.0;
    private double close = 0.0;
    private int volume = 0;
    private double closeCompare = 0.0;
    private double spread = 0.0;
    private List<? extends Indicator> indicators = null;

    /**
     *
     */
    public Point() {
    }

    /**
     *
     * @return
     */
    public double getSpread() {
        return spread;
    }

    /**
     *
     * @param spread
     */
    public void setSpread(double spread) {
        this.spread = spread;
    }

    /**
     *
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     *
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     *
     * @return
     */
    public double getCloseCompare() {
        return closeCompare;
    }

    /**
     *
     * @param closeCompare
     */
    public void setCloseCompare(double closeCompare) {
        this.closeCompare = closeCompare;
    }

    /**
     *
     * @return
     */
    public double getClose() {
        return close;
    }

    /**
     *
     * @return
     */
    public double getWeihgted() {
        return ((this.getClose() + this.getOpen() + this.getHigh() + this.getLow()) / 4);
    }

    /**
     *
     * @param close
     */
    public void setClose(double close) {
        this.close = close;
    }

    /**
     *
     * @return
     */
    public Date getDate() {
        return (this.date = date != null ? new Date(date.getTime()) : null);
    }

    /**
     *
     * @param date
     */
    public void setDate(Date date) {
        this.date = date != null ? new Date(date.getTime()) : null;
    }

    /**
     *
     * @return
     */
    public List<? extends Indicator> getIndicators() {
        return indicators;
    }

    /**
     *
     * @param indicators
     */
    public void setIndicators(List<? extends Indicator> indicators) {
        this.indicators = indicators;
    }

    /**
     *
     * @return
     */
    public double getOpen() {
        return open;
    }

    /**
     *
     * @param open
     */
    public void setOpen(double open) {
        this.open = open;
    }

    /**
     *
     * @return
     */
    public int getVolume() {
        return volume;
    }

    /**
     *
     * @param volume
     */
    public void setVolume(int volume) {
        this.volume = volume;
    }

    /**
     *
     * @return
     */
    public double getHigh() {
        return high;
    }

    /**
     *
     * @param high
     */
    public void setHigh(double high) {
        this.high = high;
    }

    /**
     *
     * @return
     */
    public double getLow() {
        return low;
    }

    /**
     *
     * @param low
     */
    public void setLow(double low) {
        this.low = low;
    }

    @Override
    public String toString() {
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return ";Index=" + this.index
                + ";Date=" + format.format(this.date)
                + ";Open=" + this.open
                + ";Close=" + this.close
                + ";Low=" + this.low
                + ";High=" + this.high
                + ";Spread=" + this.spread;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point other = (Point) obj;
        /*        if (this.index != other.index) {
         return false;
         }*/
        if (this.date != other.date && (this.date == null || !this.date.equals(other.date))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.index;
        hash = 97 * hash + (this.date != null ? this.date.hashCode() : 0);
        return hash;
    }
}

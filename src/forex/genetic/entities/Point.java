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

    public Point() {
    }

    public double getSpread() {
        return spread;
    }

    public void setSpread(double spread) {
        this.spread = spread;
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

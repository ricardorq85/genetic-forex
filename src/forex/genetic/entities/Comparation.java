/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

/**
 *
 * @author ricardorq85
 */
public class Comparation extends Indicator {

    private double close = 0.0;
    private Interval interval = new Interval();

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Comparation) {
            Comparation objAvg = (Comparation) obj;
            return (this.interval.equals(objAvg.interval));
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
        buffer.append("Compare Close=" + ((this.close == 0.0) ? 0.0 : this.close));
        buffer.append("; " + (this.interval));

        return buffer.toString();
    }
}

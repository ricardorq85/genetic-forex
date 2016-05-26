/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

/**
 *
 * @author ricardorq85
 */
public class Interval extends Indicator {

    private double lowInterval = 0.0;
    private double highInterval = 0.0;

    public Interval() {
    }

    public Interval(double lowInterval, double highInterval) {
        this.setLowInterval(lowInterval);
        this.setHighInterval(highInterval);
    }

    public double getHighInterval() {
        return highInterval;
    }

    public void setHighInterval(double highInterval) {
        this.highInterval = highInterval;
    }

    public double getLowInterval() {
        return lowInterval;
    }

    public void setLowInterval(double lowInterval) {
        this.lowInterval = lowInterval;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Interval) {
            Interval objInterval = (Interval) obj;
            return (((objInterval.lowInterval - this.lowInterval) / this.lowInterval) < 0.005
                    && ((objInterval.highInterval - this.highInterval) / this.highInterval) < 0.005);
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
        buffer.append("lowInterval=" + (this.lowInterval));
        buffer.append("; highInterval=" + this.highInterval);

        return buffer.toString();
    }
}

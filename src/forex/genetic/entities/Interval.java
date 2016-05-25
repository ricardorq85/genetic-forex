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

    private double lowerInterval = 0.0;
    private double higherInterval = 0.0;

    public Interval() {
    }

    public Interval(double lowerInterval, double higherInterval) {
        this.setLowerInterval(lowerInterval);
        this.setHigherInterval(higherInterval);
    }

    public double getHigherInterval() {
        return higherInterval;
    }

    public void setHigherInterval(double higherInterval) {
        this.higherInterval = higherInterval;
    }

    public double getLowerInterval() {
        return lowerInterval;
    }

    public void setLowerInterval(double lowerInterval) {
        this.lowerInterval = lowerInterval;
    }

    @Override
    public boolean  equals(Object obj) {
        if (obj instanceof Interval) {
            Interval objInterval = (Interval) obj;
            return (this.lowerInterval == objInterval.lowerInterval)
                    && (this.higherInterval == objInterval.higherInterval);
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
        buffer.append("LowerInterval=" + (this.lowerInterval));
        buffer.append("; HigherInterval=" + this.higherInterval);

        return buffer.toString();
    }
}

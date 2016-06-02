/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import forex.genetic.manager.PropertiesManager;
import java.io.Serializable;

/**
 *
 * @author ricardorq85
 */
public class DoubleInterval extends Interval<Double> {

    /**
     *
     */
    public static final long serialVersionUID = 201101251800L;

    /**
     *
     * @param name
     */
    public DoubleInterval(String name) {
        super(name);
    }

    /**
     *
     * @param lowInterval
     * @param highInterval
     */
    public DoubleInterval(Double lowInterval, Double highInterval) {
        this(null, lowInterval, highInterval);
    }

    /**
     *
     * @param name
     * @param lowInterval
     * @param highInterval
     */
    public DoubleInterval(String name, Double lowInterval, Double highInterval) {
        super(name);
        this.setLowInterval(lowInterval);
        this.setHighInterval(highInterval);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DoubleInterval) {
            Interval<Double> objInterval = (DoubleInterval) obj;
            if (objInterval.getLowInterval().isNaN() && objInterval.getHighInterval().isNaN() && this.getLowInterval().isNaN() && this.getHighInterval().isNaN()) {
                return true;
            } else {
                return ((Math.abs((objInterval.getLowInterval() - this.getLowInterval()) / this.getLowInterval()) < 0.001)
                        && (Math.abs((objInterval.getHighInterval() - this.getHighInterval()) / this.getHighInterval()) < 0.001));
            }
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
        buffer.append(PropertiesManager.getOperationType()).append(this.name).append("Lower=").append(this.lowInterval * 100.0).append(",");
        buffer.append(PropertiesManager.getOperationType()).append(this.name).append("Higher=").append(this.highInterval * 100.0);

        return buffer.toString();
    }
}

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
public class DoubleInterval extends Interval<Double> implements Serializable {

    public static final long serialVersionUID = 201101251800L;

    public DoubleInterval(String name) {
        super(name);
    }

    public DoubleInterval(Double lowInterval, Double highInterval) {
        this(null, lowInterval, highInterval);
    }

    public DoubleInterval(String name, Double lowInterval, Double highInterval) {
        super(name);
        this.setLowInterval(lowInterval);
        this.setHighInterval(highInterval);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DoubleInterval) {
            Interval<Double> objInterval = (DoubleInterval) obj;
            double d1 = Math.abs((objInterval.getLowInterval() - this.getLowInterval()) / this.getLowInterval());
            double d2 = Math.abs((objInterval.getHighInterval() - this.getHighInterval()) / this.getHighInterval());
            return ((Math.abs((objInterval.getLowInterval() - this.getLowInterval()) / this.getLowInterval()) < 0.0005)
                    && (Math.abs((objInterval.getHighInterval() - this.getHighInterval()) / this.getHighInterval()) < 0.0005));
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
        buffer.append(PropertiesManager.getOperationType() + this.name + "Lower=" + (this.lowInterval * 100.0) + ",");
        buffer.append(PropertiesManager.getOperationType() + this.name + "Higher=" + (this.highInterval * 100.0));

        return buffer.toString();
    }
}

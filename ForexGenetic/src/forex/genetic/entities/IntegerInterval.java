/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.io.Serializable;

/**
 *
 * @author ricardorq85
 */
public class IntegerInterval extends Interval<Integer> implements Comparable<IntegerInterval> {

    /**
     *
     */
    public static final long serialVersionUID = 201206011720L;

    /**
     *
     * @param name
     */
    public IntegerInterval(String name) {
        super(name);
    }

    /**
     *
     * @param lowInterval
     * @param highInterval
     */
    public IntegerInterval(Integer lowInterval, Integer highInterval) {
        this(null, lowInterval, highInterval);
    }

    /**
     *
     * @param name
     * @param lowInterval
     * @param highInterval
     */
    public IntegerInterval(String name, Integer lowInterval, Integer highInterval) {
        super(name);
        this.setLowInterval(lowInterval);
        this.setHighInterval(highInterval);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntegerInterval) {
            Interval<Integer> objInterval = (IntegerInterval) obj;
            double d1 = Math.abs((objInterval.getLowInterval() - this.getLowInterval()) / this.getLowInterval());
            double d2 = Math.abs((objInterval.getHighInterval() - this.getHighInterval()) / this.getHighInterval());
            return ((objInterval.getLowInterval() == this.getLowInterval())
                    && (objInterval.getHighInterval() == this.getHighInterval()));
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
        buffer.append(this.name + "Lower=" + (this.lowInterval) + ",");
        buffer.append(this.name + "Higher=" + (this.highInterval));

        return buffer.toString();
    }

    @Override
    public int compareTo(IntegerInterval o) {
        return new Integer(this.lowInterval + this.highInterval).compareTo(new Integer(o.lowInterval + o.highInterval));
    }
}

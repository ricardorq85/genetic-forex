/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.io.Serializable;

/**
 *
 * @author ricardorq85
 * @param <E>
 */
public abstract class Interval<E> implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 6007574011942155419L;

    /**
     *
     */
    protected String name = null;

    /**
     *
     */
    protected E lowInterval = null;

    /**
     *
     */
    protected E highInterval = null;

    /**
     *
     * @param name
     */
    public Interval(String name) {
        this.name = name;
    }

    /**
     *
     * @param lowInterval
     * @param highInterval
     */
    public Interval(E lowInterval, E highInterval) {
        this.lowInterval = lowInterval;
        this.highInterval = highInterval;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public E getHighInterval() {
        return highInterval;
    }

    /**
     *
     * @param highInterval
     */
    public void setHighInterval(E highInterval) {
        this.highInterval = highInterval;
    }

    /**
     *
     * @return
     */
    public E getLowInterval() {
        return lowInterval;
    }

    /**
     *
     * @param lowInterval
     */
    public void setLowInterval(E lowInterval) {
        this.lowInterval = lowInterval;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Interval<E> other = (Interval<E>) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.lowInterval != other.lowInterval && (this.lowInterval == null || !this.lowInterval.equals(other.lowInterval))) {
            return false;
        }
        if (this.highInterval != other.highInterval && (this.highInterval == null || !this.highInterval.equals(other.highInterval))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 29 * hash + (this.lowInterval != null ? this.lowInterval.hashCode() : 0);
        hash = 29 * hash + (this.highInterval != null ? this.highInterval.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.name).append("Lower=").append(this.lowInterval.toString()).append(",");
        buffer.append(this.name).append("Higher=").append(this.highInterval.toString());

        return buffer.toString();
    }
}

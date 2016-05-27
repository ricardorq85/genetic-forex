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
public abstract class Interval<E> implements Serializable {

    public static final long serialVersionUID = 6007574011942155419L;
    protected String name = null;
    protected E lowInterval = null;
    protected E highInterval = null;

    public Interval(String name) {
        this.name = name;
    }

    public Interval(E lowInterval, E highInterval) {
        this.setLowInterval(lowInterval);
        this.setHighInterval(highInterval);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public E getHighInterval() {
        return highInterval;
    }

    public void setHighInterval(E highInterval) {
        this.highInterval = highInterval;
    }

    public E getLowInterval() {
        return lowInterval;
    }

    public void setLowInterval(E lowInterval) {
        this.lowInterval = lowInterval;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.name + "Lower=" + (this.lowInterval) + ",");
        buffer.append(this.name + "Higher=" + this.highInterval);

        return buffer.toString();
    }
}

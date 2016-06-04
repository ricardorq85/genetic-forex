/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.indicator;

import forex.genetic.entities.Interval;
import java.io.Serializable;

/**
 *
 * @author ricardorq85
 */
public abstract class IntervalIndicator extends Indicator implements Serializable {

	/**
	 *
	 */
	public static final long serialVersionUID = -1166339844322682100L;

	/**
	 *
	 */
	protected Interval<Double> interval = null;

	/**
	 *
	 */
	protected String name = null;

	/**
	 *
	 * @param name
	 */
	public IntervalIndicator(String name) {
		this.name = name;
		// interval = new DoubleInterval(name);
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
	public Interval<Double> getInterval() {
		return interval;
	}

	/**
	 *
	 * @param interval
	 */
	public void setInterval(Interval<Double> interval) {
		this.interval = interval;
		this.interval.setName(this.name);
	}

	/**
	 *
	 * @param prefix
	 * @return
	 */
	@Override
	public String toFileString(String prefix) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(prefix).append(this.interval.getName()).append("Lower=")
				.append(this.interval.getLowInterval() * 100).append(",");
		buffer.append(prefix).append(this.interval.getName()).append("Higher=")
				.append(this.interval.getHighInterval() * 100).append(",");

		return buffer.toString();
	}

	@Override
	public String toString() {
		return this.interval.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IntervalIndicator) {
			IntervalIndicator objIndicator = (IntervalIndicator) obj;
			boolean value = (this.interval.equals(objIndicator.interval));
			return value;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 79 * hash + (this.interval != null ? this.interval.hashCode() : 0);
		return hash;
	}

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

/**
 *
 * @author ricardorq85
 * @param <E>
 */
public abstract class NumberInterval<E extends Number> extends Interval<E> {

	public static final long serialVersionUID = 201612290755L;

	protected NumberInterval() {

	}

	/**
	 *
	 * @param name
	 */
	public NumberInterval(String name) {
		this.name = name;
	}

	/**
	 *
	 * @param lowInterval
	 * @param highInterval
	 */
	public NumberInterval(E lowInterval, E highInterval) {
		this.lowInterval = lowInterval;
		this.highInterval = highInterval;
	}

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.indicator;

/**
 *
 * @author ricardorq85
 */
public abstract class Indicator {

	protected String name = null;

	/**
	 *
	 * @param prefix
	 * @return
	 */
	public abstract String toFileString(String prefix);

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

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.util.Date;

import forex.genetic.util.DateUtil;

/**
 *
 * @author ricardorq85
 */
public class DateInterval extends Interval<Date> {

	/**
	 *
	 */
	public static final long serialVersionUID = 201101251800L;

	/**
	 *
	 */
	public DateInterval() {
		this(null);
	}

	/**
	 *
	 * @param name
	 */
	public DateInterval(String name) {
		super(name);
	}

	/**
	 *
	 * @param lowInterval
	 * @param highInterval
	 */
	public DateInterval(Date lowInterval, Date highInterval) {
		this(null, lowInterval, highInterval);
	}

	/**
	 *
	 * @param name
	 * @param lowInterval
	 * @param highInterval
	 */
	public DateInterval(String name, Date lowInterval, Date highInterval) {
		super(name);
		this.setLowInterval(lowInterval);
		this.setHighInterval(highInterval);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DateInterval) {
			Interval<Date> objInterval = (DateInterval) obj;
			return ((objInterval.getLowInterval().equals(this.getLowInterval()))
					&& (objInterval.getHighInterval().equals(this.getHighInterval())));
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
		buffer.append("lowInterval=" + DateUtil.getDateString(this.getLowInterval()));
		buffer.append("; highInterval=" + DateUtil.getDateString(this.getHighInterval()));

		return buffer.toString();
	}

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.indicator;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ricardorq85
 */
public class Bollinger extends IntervalIndicator {

	/**
	 *
	 */
	public static final long serialVersionUID = 201102281800L;
	private double upper = 0.0;
	private double lower = 0.0;
	private double period = 0.0;
	private double desviation = 0.0;

	/**
	 *
	 * @param name
	 */
	public Bollinger(String name) {
		super(name);
	}

	/**
	 *
	 * @return
	 */
	public double getDesviation() {
		return desviation;
	}

	/**
	 *
	 * @param desviation
	 */
	public void setDesviation(double desviation) {
		this.desviation = desviation;
	}

	/**
	 *
	 * @return
	 */
	public double getLower() {
		return lower;
	}

	/**
	 *
	 * @param lower
	 */
	public void setLower(double lower) {
		this.lower = lower;
	}

	/**
	 *
	 * @return
	 */
	public double getUpper() {
		return upper;
	}

	/**
	 *
	 * @param upper
	 */
	public void setUpper(double upper) {
		this.upper = upper;
	}

	/**
	 *
	 * @return
	 */
	public double getPeriod() {
		return period;
	}

	/**
	 *
	 * @param period
	 */
	public void setPeriod(double period) {
		this.period = period;
	}

	@Override
	public Map<String, Object> valuesToMap() {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		boolean allSet = true;
		if (Double.isInfinite(this.lower) || Double.isNaN(this.lower)) {
			objectMap.put("lower", null);
			allSet = false;
		} else {
			objectMap.put("lower", this.lower);
		}
		if (Double.isInfinite(this.upper) || Double.isNaN(this.upper)) {
			objectMap.put("upper", null);
			allSet = false;
		} else {
			objectMap.put("upper", this.upper);
		}
		if (allSet) {
			objectMap.put("calculado", (this.upper - this.lower));
		} else {
			objectMap.put("calculado", null);
		}

		return objectMap;
	}

}

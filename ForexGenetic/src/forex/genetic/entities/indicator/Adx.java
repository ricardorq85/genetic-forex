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
public class Adx extends IntervalIndicator {

	/**
	 *
	 */
	public static final long serialVersionUID = 201102041228L;
	private double adxValue = 0.0;
	private double adxPlus = 0.0;
	private double adxMinus = 0.0;
	private double parameter1 = 0.0;

	/**
	 *
	 * @param name
	 */
	public Adx(String name) {
		super(name);
	}

	/**
	 *
	 * @return
	 */
	public double getAdxMinus() {
		return adxMinus;
	}

	/**
	 *
	 * @param adxMinus
	 */
	public void setAdxMinus(double adxMinus) {
		this.adxMinus = adxMinus;
	}

	/**
	 *
	 * @return
	 */
	public double getAdxPlus() {
		return adxPlus;
	}

	/**
	 *
	 * @param adxPlus
	 */
	public void setAdxPlus(double adxPlus) {
		this.adxPlus = adxPlus;
	}

	/**
	 *
	 * @return
	 */
	public double getAdxValue() {
		return adxValue;
	}

	/**
	 *
	 * @param adxValue
	 */
	public void setAdxValue(double adxValue) {
		this.adxValue = adxValue;
	}

	/**
	 *
	 * @return
	 */
	public double getParameter1() {
		return parameter1;
	}

	/**
	 *
	 * @param parameter1
	 */
	public void setParameter1(double parameter1) {
		this.parameter1 = parameter1;
	}

	@Override
	public Map<String, Object> valuesToMap() {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		boolean allSet = true;
		if (Double.isInfinite(this.adxValue) || Double.isNaN(this.adxValue)) {
			objectMap.put("adxValue", null);
			allSet = false;
		} else {
			objectMap.put("adxValue", this.adxValue);
		}
		if (Double.isInfinite(this.adxMinus) || Double.isNaN(this.adxMinus)) {
			objectMap.put("adxMinus", null);
			allSet = false;
		} else {
			objectMap.put("adxMinus", this.adxMinus);
		}
		if (Double.isInfinite(this.adxPlus) || Double.isNaN(this.adxPlus)) {
			objectMap.put("adxPlus", null);
			allSet = false;
		} else {
			objectMap.put("adxPlus", this.adxPlus);
		}
		if (allSet) {
			objectMap.put("calculado", this.adxValue * (this.adxPlus - this.adxMinus));
		} else {
			objectMap.put("calculado", null);
		}
		return objectMap;
	}

}

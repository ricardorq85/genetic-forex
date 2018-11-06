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
public class Macd extends IntervalIndicator {

	/**
	 *
	 */
	public static final long serialVersionUID = 201101251800L;
	private double macdValue = 0.0;
	private double macdSignal = 0.0;
	private double parameter1 = 0.0;
	private double parameter2 = 0.0;
	private double parameter3 = 0.0;

	/**
	 *
	 * @param name
	 */
	public Macd(String name) {
		super(name);
	}

	/**
	 *
	 * @return
	 */
	public double getParameter3() {
		return parameter3;
	}

	/**
	 *
	 * @param parameter3
	 */
	public void setParameter3(double parameter3) {
		this.parameter3 = parameter3;
	}

	/**
	 *
	 * @return
	 */
	public double getMacdSignal() {
		return macdSignal;
	}

	/**
	 *
	 * @param macdSignal
	 */
	public void setMacdSignal(double macdSignal) {
		this.macdSignal = macdSignal;
	}

	/**
	 *
	 * @return
	 */
	public double getMacdValue() {
		return macdValue;
	}

	/**
	 *
	 * @param macdValue
	 */
	public void setMacdValue(double macdValue) {
		this.macdValue = macdValue;
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

	/**
	 *
	 * @return
	 */
	public double getParameter2() {
		return parameter2;
	}

	/**
	 *
	 * @param parameter2
	 */
	public void setParameter2(double parameter2) {
		this.parameter2 = parameter2;
	}

	@Override
	public Map<String, Object> valuesToMap() {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		boolean allSet = true;
		if (Double.isInfinite(this.macdSignal) || Double.isNaN(this.macdSignal)) {
			objectMap.put("macdSignal", null);
			allSet = false;
		} else {
			objectMap.put("macdSignal", this.macdSignal);
		}
		if (Double.isInfinite(this.macdValue) || Double.isNaN(this.macdValue)) {
			objectMap.put("macdValue", null);
			allSet = false;
		} else {
			objectMap.put("macdValue", this.macdValue);
		}
		if (allSet) {
			objectMap.put("calculado", this.macdValue - this.macdSignal);
		} else {
			objectMap.put("calculado", null);
		}

		return objectMap;
	}

}

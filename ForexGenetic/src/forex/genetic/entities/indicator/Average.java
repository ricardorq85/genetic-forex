/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.indicator;

import java.util.HashMap;
import java.util.Map;

import forex.genetic.entities.Point;

/**
 *
 * @author ricardorq85
 */
public class Average extends IntervalIndicator {

	/**
	 *
	 */
	public static final long serialVersionUID = 201101251800L;
	private double average = 0.0;
	private double parameter1 = 0.0;

	/**
	 *
	 * @param name
	 */
	public Average(String name) {
		super(name);
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
	public double getAverage() {
		return average;
	}

	/**
	 *
	 * @param average
	 */
	public void setAverage(double average) {
		this.average = average;
	}

	@Override
	public Map<String, Object> valuesToMap(Point datoHistorico) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		if (!Double.isInfinite(this.average) && !Double.isNaN(this.average)) {
			objectMap.put("average", this.average);
			objectMap.put("calculado", (this.average - datoHistorico.getClose()));
			objectMap.put("calculado_compare", (this.average - datoHistorico.getCloseCompare()));
		}
		return objectMap;
	}

}

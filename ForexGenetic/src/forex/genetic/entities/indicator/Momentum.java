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
public class Momentum extends IntervalIndicator {

	/**
	 *
	 */
	public static final long serialVersionUID = 201102142113L;
	private double momentum = 0.0;
	private double period = 0.0;

	/**
	 *
	 * @param name
	 */
	public Momentum(String name) {
		super(name);
	}

	/**
	 *
	 * @return
	 */
	public double getMomentum() {
		return momentum;
	}

	/**
	 *
	 * @param momentum
	 */
	public void setMomentum(double momentum) {
		this.momentum = momentum;
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
		if (Double.isInfinite(this.momentum) || Double.isNaN(this.momentum)) {
			objectMap.put("momentum", null);
			objectMap.put("calculado", null);
		} else {
			objectMap.put("momentum", this.momentum);
			objectMap.put("calculado", this.momentum);
		}
		return objectMap;
	}

}

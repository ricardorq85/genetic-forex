/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import java.util.HashMap;
import java.util.Map;

import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.Momentum;
import forex.genetic.entities.indicator.Rsi;
import forex.genetic.util.NumberUtil;

/**
 *
 * @author ricardorq85
 */
public class MomentumIndicatorManager extends IntervalIndicatorManager<Momentum> {

	public MomentumIndicatorManager(boolean priceDependence, boolean obligatory, String name) {
		super(priceDependence, obligatory, name);
	}

	public MomentumIndicatorManager() {
		super(false, "Momentum");
		this.id = "MOMENTUM";
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Momentum getIndicatorInstance() {
		return new Momentum("Momentum");
	}

	/**
	 *
	 * @param indicator
	 * @param point
	 * @return
	 */
	@Override
	public Indicator generate(Momentum indicator, Point point) {
		Interval interval = null;
		Momentum momentum = new Momentum("Momentum");
		if (indicator != null) {
			momentum.setMomentum(indicator.getMomentum());
			double value = indicator.getMomentum();
			interval = intervalManager.generate(value, -value * 0.1, value * 0.1);
		} else {
			interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
		}
		momentum.setInterval(interval);

		return momentum;
	}

	/**
	 *
	 * @param momentumIndividuo
	 * @param iMomentum
	 * @param point
	 * @return
	 */
	@Override
	public boolean operate(Momentum momentumIndividuo, Momentum iMomentum, Point point) {
		return intervalManager.operate(momentumIndividuo.getInterval(), iMomentum.getMomentum(), 0.0);
	}

	/*
	 * public Indicator optimize(Momentum individuo, Momentum optimizedIndividuo,
	 * Momentum indicator, Point point) { Momentum optimized =
	 * this.getIndicatorInstance(); double value = indicator.getMomentum(); Interval
	 * generated = intervalManager.generate(value, 0.0, 0.0);
	 * intervalManager.round(generated); Interval intersected =
	 * IntervalManager.intersect(generated, individuo.getInterval());
	 * optimized.setInterval(intervalManager.optimize((optimizedIndividuo == null) ?
	 * null : optimizedIndividuo.getInterval(), intersected)); if
	 * (optimized.getInterval() == null) { optimized = optimizedIndividuo; } return
	 * optimized; }
	 */
	/**
	 *
	 * @param indicator
	 * @param prevPoint
	 * @param point
	 * @return
	 */
	@Override
	public double getValue(Momentum indicator, Point prevPoint, Point point) {
		double value = indicator.getMomentum();
		return value;
	}

	@Override
	public String[] queryRangoOperacionIndicador() {
		String[] s = new String[2];
		s[0] = " MIN(DH.MOMENTUM) INF_" + this.id + ",  MAX(DH.MOMENTUM) SUP_" + this.id + ", "
				+ "  ROUND(AVG(DH.MOMENTUM), 5) PROM_" + this.id + ", ";
		s[1] = " AND DH.MOMENTUM IS NOT NULL ";
		return s;
	}

	@Override
	public String[] queryPorcentajeCumplimientoIndicador() {
		String[] s = new String[1];
		s[0] = " ((DH.MOMENTUM) BETWEEN ? AND ?) ";
		return s;
	}

	@Override
	public Map<String, Object> getCalculatedValues(Momentum prevIndicator, Momentum indicator, Point prevPoint,
			Point point) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		if (!NumberUtil.isInfiniteOrNan(indicator.getMomentum())) {
			objectMap.put("momentum", indicator.getMomentum());
		}
		if (prevIndicator != null) {
			if (!NumberUtil.isAnyInfiniteOrNan(prevIndicator.getMomentum())) {
				objectMap.put("calculado", NumberUtil.round((prevIndicator.getMomentum())));
			}
		}
		return objectMap;
	}

}

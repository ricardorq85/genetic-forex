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
import forex.genetic.entities.indicator.Sar;
import forex.genetic.util.NumberUtil;

/**
 *
 * @author ricardorq85
 */
public class SarIndicatorManager extends IntervalIndicatorManager<Sar> {

	/**
	 *
	 * @param priceDependence
	 */
	public SarIndicatorManager(boolean priceDependence) {
		super(priceDependence, "Sar");
		this.id = "SAR";
	}

	public SarIndicatorManager(boolean priceDependence, boolean obligatory, String name) {
		super(priceDependence, obligatory, name);
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Sar getIndicatorInstance() {
		return new Sar("Sar");
	}

	public String[] getNombresCalculados() {
		return new String[] { "calculado_low", "calculado_high" };
	}

	/**
	 *
	 * @param indicator
	 * @param point
	 * @return
	 */
	@Override
	public Indicator generate(Sar indicator, Point point) {
		Interval interval = null;
		Sar sar = new Sar("Sar");
		if (indicator != null) {
			sar.setSar(indicator.getSar());
			interval = intervalManager.generate(indicator.getSar(), point.getLow(), point.getHigh());
		} else {
			interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
		}
		sar.setInterval(interval);

		return sar;
	}

	/**
	 *
	 * @param sarIndividuo
	 * @param iSar
	 * @param point
	 * @return
	 */
	@Override
	public boolean operate(Sar sarIndividuo, Sar iSar, Point point) {
		return intervalManager.operate(sarIndividuo.getInterval(), iSar.getSar(), point);
	}

	/**
	 *
	 * @param sarIndividuo
	 * @param iSar
	 * @param point
	 * @return
	 */
	@Override
	public Interval calculateInterval(Sar sarIndividuo, Sar iSar, Point point) {
		return intervalManager.calculateInterval(sarIndividuo.getInterval(), iSar.getSar(), point);
	}

	/*
	 * public Indicator optimize(Sar individuo, Sar optimizedIndividuo, Sar
	 * indicator, Point point) { Sar optimized = this.getIndicatorInstance(); double
	 * sar = indicator.getSar(); Interval generated = intervalManager.generate(sar,
	 * point.getLow(), point.getHigh()); intervalManager.round(generated); Interval
	 * intersected = IntervalManager.intersect(generated, individuo.getInterval());
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
	public double getValue(Sar indicator, Point prevPoint, Point point) {
		double value = indicator.getSar();
		return value;
	}

	@Override
	public String[] queryRangoOperacionIndicador() {
		String[] s = new String[2];
		s[0] = " MIN(DH.SAR-OPER.OPEN_PRICE) INF_" + this.id + ",  MAX(DH.SAR-OPER.OPEN_PRICE) SUP_" + this.id + ", "
				+ " ROUND(AVG(DH.SAR-OPER.OPEN_PRICE), 5) PROM_" + this.id + ", ";
		s[1] = " AND DH.SAR IS NOT NULL ";
		return s;
	}

	@Override
	public String[] queryPorcentajeCumplimientoIndicador() {
		String[] s = new String[1];
		s[0] = " ((DH.SAR-DH.LOW) BETWEEN ? AND ? " + "  OR (DH.SAR-DH.HIGH) BETWEEN ? AND ? ) ";
		return s;
	}

	@Override
	public Map<String, Double> getCalculatedValues(Sar prevIndicator, Sar indicator, Point prevPoint, Point point) {
		Map<String, Double> objectMap = new HashMap<String, Double>();
		if (!NumberUtil.isInfiniteOrNan(indicator.getSar())) {
			objectMap.put("sar", indicator.getSar());
		}
		if (prevIndicator != null) {
			if (!NumberUtil.isAnyInfiniteOrNan(prevIndicator.getSar(), point.getLow(), point.getHigh())) {
				objectMap.put("calculado_low", NumberUtil.round((prevIndicator.getSar() - point.getLow())));
				objectMap.put("calculado_high", NumberUtil.round((prevIndicator.getSar() - point.getHigh())));
			}
		}
		return objectMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Sar getIndicatorInstance(Map<String, Object> indMap) {
		Sar instance = getIndicatorInstance();
		if (indMap != null) {
			if (indMap.containsKey(instance.getName())) {
				if (indMap.get(instance.getName()) != null) {
					Map<String, Double> values = ((Map<String, Double>) indMap.get(instance.getName()));
					if (values.containsKey("sar")) {
						instance.setSar(values.get("sar"));
					}
				}
			}
		}
		return instance;
	}

}

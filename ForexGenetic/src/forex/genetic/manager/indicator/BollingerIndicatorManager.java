/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import java.util.HashMap;
import java.util.Map;

import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Bollinger;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.util.NumberUtil;

/**
 *
 * @author ricardorq85
 */
public class BollingerIndicatorManager extends IntervalIndicatorManager<Bollinger> {

	public BollingerIndicatorManager(boolean priceDependence, boolean obligatory, String name) {
		super(priceDependence, obligatory, name);
	}

	public BollingerIndicatorManager() {
		super(false, "Bollinger");
		this.id = "BOLLINGER";
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Bollinger getIndicatorInstance() {
		return new Bollinger("Bollinger");
	}

	/**
	 *
	 * @param indicator
	 * @param point
	 * @return
	 */
	@Override
	public Indicator generate(Bollinger indicator, Point point) {
		Interval interval = null;
		Bollinger bollingerBand = this.getIndicatorInstance();
		if (indicator != null) {
			bollingerBand.setPeriod(indicator.getPeriod());
			bollingerBand.setDesviation(indicator.getDesviation());
			double value = indicator.getUpper() - indicator.getLower();
			interval = intervalManager.generate(value, -value * 0.1, value * 0.1);
		} else {
			interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
		}
		bollingerBand.setInterval(interval);
		return bollingerBand;
	}

	/**
	 *
	 * @param bollingerBandIndividuo
	 * @param iBollinger
	 * @param point
	 * @return
	 */
	@Override
	public boolean operate(Bollinger bollingerBandIndividuo, Bollinger iBollinger, Point point) {
		return intervalManager.operate(bollingerBandIndividuo.getInterval(),
				iBollinger.getUpper() - iBollinger.getLower(), 0.0);
	}

	/**
	 *
	 * @param indicator
	 * @param prevPoint
	 * @param point
	 * @return
	 */
	@Override
	public double getValue(Bollinger indicator, Point prevPoint, Point point) {
		double value = indicator.getUpper() - indicator.getLower();
		return value;
	}

	@Override
	public String[] queryRangoOperacionIndicador() {
		String[] s = new String[2];
		s[0] = " MIN(DH.BOLLINGER_UPPER-DH.BOLLINGER_LOWER) INF_" + this.id
				+ ",  MAX(DH.BOLLINGER_UPPER-DH.BOLLINGER_LOWER) SUP_" + this.id + ",  "
				+ "  ROUND(AVG(DH.BOLLINGER_UPPER-DH.BOLLINGER_LOWER), 5) PROM_" + this.id + ", ";
		s[1] = " AND DH.BOLLINGER_UPPER IS NOT NULL AND DH.BOLLINGER_LOWER IS NOT NULL ";
		return s;
	}

	@Override
	public String[] queryPorcentajeCumplimientoIndicador() {
		String[] s = new String[1];
		s[0] = " ((DH.BOLLINGER_UPPER-DH.BOLLINGER_LOWER) BETWEEN ? AND ?) ";
		return s;
	}

	@Override
	public Map<String, Double> getCalculatedValues(Bollinger prevIndicator, Bollinger indicator, Point prevPoint,
			Point point) {
		Map<String, Double> objectMap = new HashMap<String, Double>();
		if (!NumberUtil.isInfiniteOrNan(indicator.getLower())) {
			objectMap.put("lower", indicator.getLower());
		}
		if (!NumberUtil.isInfiniteOrNan(indicator.getUpper())) {
			objectMap.put("upper", indicator.getUpper());
		}
		if (prevIndicator != null) {
			if (!NumberUtil.isAnyInfiniteOrNan(prevIndicator.getLower(), prevIndicator.getUpper())) {
				objectMap.put("calculado", NumberUtil.round((prevIndicator.getUpper() - prevIndicator.getLower())));
			}
		}
		return objectMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Bollinger getIndicatorInstance(Map<String, Object> indMap) {
		Bollinger instance = getIndicatorInstance();
		if (indMap != null) {
			if (indMap.containsKey(instance.getName())) {
				if (indMap.get(instance.getName()) != null) {
					Map<String, Double> values = ((Map<String, Double>) indMap.get(instance.getName()));
					if (values.containsKey("lower")) {
						instance.setLower(values.get("lower"));
					}
					if (values.containsKey("upper")) {
						instance.setUpper(values.get("upper"));
					}
				}
			}
		}
		return instance;
	}
}

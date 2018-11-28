/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import java.util.HashMap;
import java.util.Map;

import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Adx;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.util.NumberUtil;

/**
 *
 * @author ricardorq85
 */
public class AdxIndicatorManager extends IntervalIndicatorManager<Adx> {

	public AdxIndicatorManager(boolean priceDependence, boolean obligatory, String name) {
		super(priceDependence, obligatory, name);
	}

	public AdxIndicatorManager() {
		super(false, true, "Adx");
		this.id = "ADX";
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Adx getIndicatorInstance() {
		return new Adx("Adx");
	}

	/**
	 *
	 * @param indicator
	 * @param point
	 * @return
	 */
	@Override
	public Indicator generate(Adx indicator, Point point) {
		Interval interval = null;
		Adx adx = new Adx("Adx");
		if (indicator != null) {
			adx.setAdxValue(indicator.getAdxValue());
			adx.setAdxPlus(indicator.getAdxPlus());
			adx.setAdxMinus(indicator.getAdxMinus());
			// interval = intervalManager.generate(indicator.getAdxPlus(),
			// adx.getAdxMinus(), Double.NaN);
			double value = indicator.getAdxValue() * (indicator.getAdxPlus() - indicator.getAdxMinus());
			interval = intervalManager.generate(value, -value * 0.1, value * 0.1);
		} else {
			interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
		}
		adx.setInterval(interval);
		return adx;
	}

	/**
	 *
	 * @param adxIndividuo
	 * @param iAdx
	 * @param point
	 * @return
	 */
	@Override
	public boolean operate(Adx adxIndividuo, Adx iAdx, Point point) {
		return intervalManager.operate(adxIndividuo.getInterval(),
				iAdx.getAdxValue() * (iAdx.getAdxPlus() - iAdx.getAdxMinus()), 0.0);
	}

	/**
	 *
	 * @param indicator
	 * @param prevPoint
	 * @param point
	 * @return
	 */
	@Override
	public double getValue(Adx indicator, Point prevPoint, Point point) {
		double value = indicator.getAdxValue() * (indicator.getAdxPlus() - indicator.getAdxMinus());
		return value;
	}

	@Override
	public String[] queryRangoOperacionIndicador() {
		String[] s = new String[2];
		s[0] = "  MIN((DH.ADX_VALUE*(DH.ADX_PLUS-DH.ADX_MINUS))) INF_" + this.id
				+ ",  MAX((DH.ADX_VALUE*(DH.ADX_PLUS-DH.ADX_MINUS))) SUP_" + this.id + ",  "
				+ "  ROUND(AVG((DH.ADX_VALUE*(DH.ADX_PLUS-DH.ADX_MINUS))), 5) PROM_" + this.id + ", ";
		s[1] = " AND DH.ADX_VALUE IS NOT NULL AND DH.ADX_PLUS IS NOT NULL AND DH.ADX_MINUS IS NOT NULL ";
		return s;
	}

	@Override
	public String[] queryPorcentajeCumplimientoIndicador() {
		String[] s = new String[1];
		s[0] = " ((DH.ADX_VALUE*(DH.ADX_PLUS-DH.ADX_MINUS)) BETWEEN ? AND ?) ";
		return s;
	}

	@Override
	public Indicator mutate(Adx obj) {
		// TODO Auto-generated method stub
		return super.mutate(obj);
	}

	@Override
	public Map<String, Double> getCalculatedValues(Adx prevIndicator, Adx indicator, Point prevPoint, Point point) {
		Map<String, Double> objectMap = new HashMap<String, Double>();
		if (!NumberUtil.isInfiniteOrNan(indicator.getAdxValue())) {
			objectMap.put("adxValue", indicator.getAdxValue());
		}
		if (!NumberUtil.isInfiniteOrNan(indicator.getAdxMinus())) {
			objectMap.put("adxMinus", indicator.getAdxMinus());
		}
		if (!NumberUtil.isInfiniteOrNan(indicator.getAdxPlus())) {
			objectMap.put("adxPlus", indicator.getAdxPlus());
		}
		if (prevIndicator != null) {
			if (!NumberUtil.isAnyInfiniteOrNan(prevIndicator.getAdxValue(), prevIndicator.getAdxPlus(),
					prevIndicator.getAdxMinus())) {
				objectMap.put("calculado", NumberUtil.round(
						prevIndicator.getAdxValue() * (prevIndicator.getAdxPlus() - prevIndicator.getAdxMinus())));
			}
		}
		return objectMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Adx getIndicatorInstance(Map<String, Object> indMap) {
		Adx instance = getIndicatorInstance();
		if (indMap != null) {
			if (indMap.containsKey(instance.getName())) {
				if (indMap.get(instance.getName()) != null) {
					Map<String, Double> values = ((Map<String, Double>) indMap.get(instance.getName()));
					if (values.containsKey("adxValue")) {
						instance.setAdxValue(values.get("adxValue"));
					}
					if (values.containsKey("adxMinus")) {
						instance.setAdxMinus(values.get("adxMinus"));
					}
					if (values.containsKey("adxPlus")) {
						instance.setAdxPlus(values.get("adxPlus"));
					}
				}
			}
		}
		return instance;
	}

}

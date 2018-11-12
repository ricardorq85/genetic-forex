/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import java.util.HashMap;
import java.util.Map;

import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Ichimoku;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.util.NumberUtil;

/**
 *
 * @author ricardorq85
 */
public class IchimokuSignalIndicatorManager extends IchimokuIndicatorManager {

	public IchimokuSignalIndicatorManager(boolean priceDependence, boolean obligatory, String name) {
		super(priceDependence, obligatory, name);
	}

	public IchimokuSignalIndicatorManager() {
		super(false, false, "IchiSignal");
		this.id = "ICHIMOKU_SIGNAL";
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Ichimoku getIndicatorInstance() {
		return new Ichimoku("IchiSignal");
	}

	public String[] getNombresCalculados() {
		return new String[] { "calculado_signal" };
	}

	/**
	 *
	 * @param indicator
	 * @param point
	 * @return
	 */
	@Override
	public Indicator generate(Ichimoku indicator, Point point) {
		Interval interval = null;
		Ichimoku ichimoku = getIndicatorInstance();
		if (indicator != null) {
			ichimoku.setChinkouSpan(indicator.getChinkouSpan());
			ichimoku.setKijunSen(indicator.getKijunSen());
			ichimoku.setSenkouSpanA(indicator.getSenkouSpanA());
			ichimoku.setSenkouSpanB(indicator.getSenkouSpanB());
			ichimoku.setTenkanSen(indicator.getTenkanSen());
			double value = getValue(indicator, point, point);
			interval = intervalManager.generate(value, -value * 0.1, value * 0.1);
		} else {
			interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
		}
		ichimoku.setInterval(interval);
		return ichimoku;
	}

	/**
	 *
	 * @param ichiIndividuo
	 * @param iIchimoku
	 * @param point
	 * @return
	 */
	@Override
	public boolean operate(Ichimoku ichiIndividuo, Ichimoku iIchimoku, Point point) {
		boolean operate = intervalManager.operate(ichiIndividuo.getInterval(), getValue(iIchimoku, point, point), 0.0);
		return operate;
	}

	/**
	 *
	 * @param indicator
	 * @param prevPoint
	 * @param point
	 * @return
	 */
	@Override
	public double getValue(Ichimoku indicator, Point prevPoint, Point point) {
		double value = indicator.getChinkouSpan() * (indicator.getTenkanSen() - indicator.getKijunSen());
		return value;
	}

	@Override
	public String[] queryRangoOperacionIndicador() {
		String[] s = new String[2];
		s[0] = "  MIN((DH.ICHIMOKUCHINKOUSPAN*(DH.ICHIMOKUTENKANSEN-DH.ICHIMOKUKIJUNSEN))) INF_" + this.id + ",  "
				+ " MAX((DH.ICHIMOKUCHINKOUSPAN*(DH.ICHIMOKUTENKANSEN-DH.ICHIMOKUKIJUNSEN))) SUP_" + this.id + ",  "
				+ " ROUND(AVG((DH.ICHIMOKUCHINKOUSPAN*(DH.ICHIMOKUTENKANSEN-DH.ICHIMOKUKIJUNSEN))), 5) PROM_" + this.id
				+ ", ";
		s[1] = " AND DH.ICHIMOKUCHINKOUSPAN IS NOT NULL " + " AND DH.ICHIMOKUTENKANSEN IS NOT NULL "
				+ " AND DH.ICHIMOKUKIJUNSEN IS NOT NULL ";
		return s;
	}

	@Override
	public String[] queryPorcentajeCumplimientoIndicador() {
		String[] s = new String[1];
		s[0] = " ((DH.ICHIMOKUCHINKOUSPAN*(DH.ICHIMOKUTENKANSEN-DH.ICHIMOKUKIJUNSEN)) BETWEEN ? AND ?) ";
		return s;
	}

	@Override
	public Map<String, Object> getCalculatedValues(Ichimoku prevIndicator, Ichimoku indicator, Point prevPoint,
			Point point) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		if (!NumberUtil.isInfiniteOrNan(indicator.getChinkouSpan())) {
			objectMap.put("chinkouSpan", indicator.getChinkouSpan());
		}
		if (!NumberUtil.isInfiniteOrNan(indicator.getKijunSen())) {
			objectMap.put("kijunSen", indicator.getKijunSen());
		}
		if (!NumberUtil.isInfiniteOrNan(indicator.getTenkanSen())) {
			objectMap.put("tenkanSen", indicator.getTenkanSen());
		}
		if (prevIndicator != null) {
			if (!NumberUtil.isAnyInfiniteOrNan(prevIndicator.getSenkouSpanA(), prevIndicator.getSenkouSpanB())) {
				objectMap.put("calculado_signal", NumberUtil.round(
						prevIndicator.getChinkouSpan() * (prevIndicator.getTenkanSen() - prevIndicator.getKijunSen())));
			}
		}
		return objectMap;
	}

}

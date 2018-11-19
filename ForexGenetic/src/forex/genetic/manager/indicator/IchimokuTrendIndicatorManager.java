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
public class IchimokuTrendIndicatorManager extends IchimokuIndicatorManager {

	public IchimokuTrendIndicatorManager(boolean priceDependence, boolean obligatory, String name) {
		super(priceDependence, obligatory, name);
	}

	public IchimokuTrendIndicatorManager() {
		super(true, false, "IchiTrend");
		this.id = "ICHIMOKU_TREND";
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Ichimoku getIndicatorInstance() {
		return new Ichimoku("IchiTrend");
	}

	public String[] getNombresCalculados() {
		return new String[] { "calculado_trend_low", "calculado_trend_high" };
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
			interval = intervalManager.generate(getValue(indicator, point, point), point.getLow(), point.getHigh());
		} else {
			interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
		}
		ichimoku.setInterval(interval);

		return ichimoku;
	}

	/**
	 *
	 * @param ichimokuIndividuo
	 * @param iIchimoku
	 * @param currentPoint
	 * @param previousPoint
	 * @return
	 */
	@Override
	public boolean operate(Ichimoku ichimokuIndividuo, Ichimoku iIchimoku, Point currentPoint, Point previousPoint) {
		boolean operate = intervalManager.operate(ichimokuIndividuo.getInterval(),
				getValue(iIchimoku, previousPoint, previousPoint), currentPoint);
		return operate;
	}

	/**
	 *
	 * @param ichimokuIndividuo
	 * @param iIchimoku
	 * @param point
	 * @return
	 */
	@Override
	public Interval calculateInterval(Ichimoku ichimokuIndividuo, Ichimoku iIchimoku, Point point) {
		return intervalManager.calculateInterval(ichimokuIndividuo.getInterval(), getValue(iIchimoku, point, point),
				point);
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
		double value = (indicator.getSenkouSpanA() - indicator.getSenkouSpanB());
		return value;
	}

	@Override
	public String[] queryRangoOperacionIndicador() {
		String[] s = new String[2];
		s[0] = " MIN(DH.ICHIMOKUSENKOUSPANA-DH.ICHIMOKUSENKOUSPANB-OPER.OPEN_PRICE) INF_" + this.id + ",  "
				+ " MAX(DH.ICHIMOKUSENKOUSPANA-DH.ICHIMOKUSENKOUSPANB-OPER.OPEN_PRICE) SUP_" + this.id + ",  "
				+ "  ROUND(AVG(DH.ICHIMOKUSENKOUSPANA-DH.ICHIMOKUSENKOUSPANB-OPER.OPEN_PRICE), 5) PROM_" + this.id
				+ ", ";
		s[1] = " AND DH.ICHIMOKUSENKOUSPANA IS NOT NULL AND DH.ICHIMOKUSENKOUSPANB IS NOT NULL ";
		return s;
	}

	@Override
	public String[] queryPorcentajeCumplimientoIndicador() {
		String[] s = new String[1];
		s[0] = " ((DH.ICHIMOKUSENKOUSPANA-DH.ICHIMOKUSENKOUSPANB-DH.LOW) BETWEEN ? AND ? "
				+ "  OR (DH.ICHIMOKUSENKOUSPANA-DH.ICHIMOKUSENKOUSPANB-DH.HIGH) BETWEEN ? AND ?) ";
		return s;
	}

	@Override
	public Map<String, Double> getCalculatedValues(Ichimoku prevIndicator, Ichimoku indicator, Point prevPoint,
			Point point) {
		Map<String, Double> objectMap = new HashMap<String, Double>();
		if (!NumberUtil.isInfiniteOrNan(indicator.getSenkouSpanA())) {
			objectMap.put("senkouSpanA", indicator.getSenkouSpanA());
		}
		if (!NumberUtil.isInfiniteOrNan(indicator.getSenkouSpanB())) {
			objectMap.put("senkouSpanB", indicator.getSenkouSpanB());
		}
		if (prevIndicator != null) {
			if (!NumberUtil.isAnyInfiniteOrNan(prevIndicator.getSenkouSpanA(), prevIndicator.getSenkouSpanB())) {
				objectMap.put("calculado_trend_low", NumberUtil
						.round((prevIndicator.getSenkouSpanA() - prevIndicator.getSenkouSpanB() - point.getLow())));
				objectMap.put("calculado_trend_high", NumberUtil
						.round((prevIndicator.getSenkouSpanA() - prevIndicator.getSenkouSpanB() - point.getHigh())));
			}
		}
		return objectMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Ichimoku getIndicatorInstance(Map<String, Object> indMap) {
		Ichimoku instance = getIndicatorInstance();
		if (indMap != null) {
			if (indMap.containsKey(instance.getName())) {
				Map<String, Double> values = ((Map<String, Double>) indMap.get(instance.getName()));
				instance.setSenkouSpanA(values.get("senkouSpanA"));
				instance.setSenkouSpanB(values.get("senkouSpanB"));
			}
		}
		return instance;
	}
}

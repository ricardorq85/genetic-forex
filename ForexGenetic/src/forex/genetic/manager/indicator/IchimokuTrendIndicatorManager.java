/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Ichimoku;
import forex.genetic.entities.indicator.Indicator;

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
		s[0] = " MIN(DH.ICHIMOKUSENKOUSPANA-DH.ICHIMOKUSENKOUSPANB-OPER.OPEN_PRICE) INF_" + this.id
				+ ",  " + " MAX(DH.ICHIMOKUSENKOUSPANA-DH.ICHIMOKUSENKOUSPANB-OPER.OPEN_PRICE) SUP_"
				+ this.id + ",  "
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

}

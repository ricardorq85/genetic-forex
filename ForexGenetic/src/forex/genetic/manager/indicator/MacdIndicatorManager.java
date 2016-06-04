/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.entities.indicator.Macd;

/**
 *
 * @author ricardorq85
 */
public class MacdIndicatorManager extends IntervalIndicatorManager<Macd> {

	public MacdIndicatorManager(boolean priceDependence, boolean obligatory, String name) {
		super(priceDependence, obligatory, name);
	}

	public MacdIndicatorManager() {
		super(false, "Macd");
		this.id = "MACD";
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Macd getIndicatorInstance() {
		return new Macd("Macd");
	}

	/**
	 *
	 * @param indicator
	 * @param point
	 * @return
	 */
	@Override
	public Indicator generate(Macd indicator, Point point) {
		Interval interval;
		Macd macd = getIndicatorInstance();
		if (indicator != null) {
			macd.setMacdSignal(indicator.getMacdSignal());
			macd.setMacdValue(indicator.getMacdValue());
			double value = indicator.getMacdValue() - indicator.getMacdSignal();
			interval = intervalManager.generate(value, -value * 0.1, value * 0.1);
		} else {
			interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
		}
		macd.setInterval(interval);
		return macd;
	}

	/**
	 *
	 * @param macdIndividuo
	 * @param iMacd
	 * @param point
	 * @return
	 */
	@Override
	public boolean operate(Macd macdIndividuo, Macd iMacd, Point point) {
		return intervalManager.operate(macdIndividuo.getInterval(), iMacd.getMacdValue() - iMacd.getMacdSignal(), 0.0);
	}

	/**
	 *
	 * @param indicator
	 * @param prevPoint
	 * @param point
	 * @return
	 */
	@Override
	public double getValue(Macd indicator, Point prevPoint, Point point) {
		double value = indicator.getMacdValue() - indicator.getMacdSignal();
		return value;
	}

	@Override
	public String[] queryRangoOperacionIndicador() {
		String[] s = new String[2];
		s[0] = " MIN(DH.MACD_VALUE-DH.MACD_SIGNAL) INF_" + this.id
				+ ", MAX(DH.MACD_VALUE-DH.MACD_SIGNAL) SUP_" + this.id
				+ ",  ROUND(AVG(DH.MACD_VALUE-DH.MACD_SIGNAL), 5) PROM_" + this.id + ", ";
		s[1] = " AND DH.MACD_VALUE IS NOT NULL AND DH.MACD_SIGNAL IS NOT NULL ";
		return s;
	}

	@Override
	public String[] queryPorcentajeCumplimientoIndicador() {
		String[] s = new String[1];
		s[0] = " ((DH.MACD_VALUE-DH.MACD_SIGNAL) BETWEEN ? AND ?) ";
		return s;
	}

}

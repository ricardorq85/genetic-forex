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
public class Macd20XIndicatorManager extends MacdIndicatorManager {

	/**
	 *
	 */
	public Macd20XIndicatorManager() {
		super(false, false, "Macd20x");
		this.id = "MACD20X";
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Macd getIndicatorInstance() {
		return new Macd("Macd20x");
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
		interval = null;
		Macd macd = getIndicatorInstance();
		if (indicator != null) {
			macd.setMacdSignal(indicator.getMacdSignal());
			macd.setMacdValue(indicator.getMacdValue());
			double value = indicator.getMacdValue() + indicator.getMacdSignal();
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
		return intervalManager.operate(macdIndividuo.getInterval(), iMacd.getMacdValue() + iMacd.getMacdSignal(), 0.0);
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
		double value = indicator.getMacdValue() + indicator.getMacdSignal();
		return value;
	}

	@Override
	public String[] queryRangoOperacionIndicador() {
		String[] s = new String[2];
		s[0] = " MIN(DH.MACD20X_VALUE+DH.MACD20X_SIGNAL) INF_" + this.id
				+ ",  MAX(DH.MACD20X_VALUE+DH.MACD20X_SIGNAL) SUP_" + this.id + ",  "
				+ "  ROUND(AVG(DH.MACD20X_VALUE+DH.MACD20X_SIGNAL), 5) PROM_" + this.id + ", ";
		s[1] = " AND DH.MACD20X_VALUE IS NOT NULL AND DH.MACD20X_SIGNAL IS NOT NULL ";
		return s;
	}

	@Override
	public String[] queryCumplimientoIndicador() {
		String[] s = new String[1];
		s[0] = " ((DH.MACD20X_VALUE+DH.MACD20X_SIGNAL) BETWEEN ? AND ?) ";
		return s;
	}

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import java.util.HashMap;
import java.util.Map;

import forex.genetic.entities.Interval;
import forex.genetic.entities.Point;
import forex.genetic.entities.indicator.Average;
import forex.genetic.entities.indicator.Indicator;
import forex.genetic.util.NumberUtil;

/**
 *
 * @author ricardorq85
 */
public class CompareMaIndicatorManager extends MaIndicatorManager {

	public CompareMaIndicatorManager(boolean priceDependence, boolean obligatory, String name) {
		super(priceDependence, obligatory, name);
	}

	public CompareMaIndicatorManager() {
		super(false, false, "MaCompare");
		this.id = "COMPARE_MA";
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Average getIndicatorInstance() {
		return new Average("MaCompare");
	}

	/**
	 *
	 * @param indicator
	 * @param point
	 * @return
	 */
	@Override
	public Indicator generate(Average indicator, Point point) {
		Interval interval = null;
		Average average = getIndicatorInstance();
		if (indicator != null) {
			average.setAverage(indicator.getAverage());
			double value = indicator.getAverage() - point.getCloseCompare();
			interval = intervalManager.generate(value, -value * 0.1, value * 0.1);
		} else {
			interval = intervalManager.generate(Double.NaN, Double.NaN, Double.NaN);
		}
		average.setInterval(interval);

		return average;
	}

	/**
	 *
	 * @param averageIndividuo
	 * @param iAverage
	 * @param currentPoint
	 * @param previousPoint
	 * @return
	 */
	@Override
	public boolean operate(Average averageIndividuo, Average iAverage, Point currentPoint, Point previousPoint) {
		return intervalManager.operate(averageIndividuo.getInterval(),
				iAverage.getAverage() - previousPoint.getCloseCompare(), 0.0);
	}

	/**
	 *
	 * @param indicator
	 * @param prevPoint
	 * @param point
	 * @return
	 */
	@Override
	public double getValue(Average indicator, Point prevPoint, Point point) {
		double value;
		value = indicator.getAverage() - prevPoint.getCloseCompare();
		return value;
	}

	@Override
	public String[] queryRangoOperacionIndicador() {
		String[] s = new String[2];
		s[0] = " MIN(DH.AVERAGE_COMPARE-DH.COMPARE_VALUE) INF_" + this.id
				+ ",  MAX(DH.AVERAGE_COMPARE-DH.COMPARE_VALUE) SUP_" + this.id + ",  "
				+ " ROUND(AVG(DH.AVERAGE_COMPARE-DH.COMPARE_VALUE), 5) PROM_" + this.id + ", ";
		s[1] = " AND DH.COMPARE_VALUE IS NOT NULL AND DH.AVERAGE_COMPARE IS NOT NULL ";
		return s;
	}

	@Override
	public String[] queryCumplimientoIndicador() {
		String[] s = new String[1];
		s[0] = " ((DH.AVERAGE_COMPARE-DH.COMPARE_VALUE) BETWEEN ? AND ?) ";
		return s;
	}

	public String[] getNombresCalculados() {
		return new String[] { "calculado"};
	}

	@Override
	public Map<String, Double> getCalculatedValues(Average prevIndicator, Average indicator, Point prevPoint,
			Point point) {
		Map<String, Double> objectMap = new HashMap<String, Double>();
		if (!NumberUtil.isInfiniteOrNan(indicator.getAverage())) {
			objectMap.put("average", indicator.getAverage());
		}
		if (prevIndicator != null) {
			if (!NumberUtil.isAnyInfiniteOrNan(prevIndicator.getAverage(), prevPoint.getCloseCompare())) {
				objectMap.put("calculado",
						NumberUtil.round((prevIndicator.getAverage() - prevPoint.getCloseCompare())));
			}
		}
		return objectMap;
	}
}

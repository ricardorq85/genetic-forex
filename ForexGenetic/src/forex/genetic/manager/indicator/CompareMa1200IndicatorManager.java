/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Average;

/**
 *
 * @author ricardorq85
 */
public class CompareMa1200IndicatorManager extends CompareMaIndicatorManager {

	public CompareMa1200IndicatorManager() {
		super(false, false, "MaCompare1200");
		this.id = "COMPARE_MA1200";
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Average getIndicatorInstance() {
		return new Average("MaCompare1200");
	}

	@Override
	public String[] queryRangoOperacionIndicador() {
		String[] s = new String[2];
		s[0] = " MIN(DH.AVERAGE_COMPARE1200-DH.COMPARE_VALUE) INF_" + this.id
				+ ",  MAX(DH.AVERAGE_COMPARE1200-DH.COMPARE_VALUE) SUP_" + this.id + ",  "
				+ " ROUND(AVG(DH.AVERAGE_COMPARE1200-DH.COMPARE_VALUE), 5) PROM_" + this.id + ", ";
		s[1] = " AND DH.COMPARE_VALUE IS NOT NULL AND DH.AVERAGE_COMPARE1200 IS NOT NULL ";
		return s;
	}

	@Override
	public String[] queryPorcentajeCumplimientoIndicador() {
		String[] s = new String[1];
		s[0] = " ((DH.AVERAGE_COMPARE1200-DH.COMPARE_VALUE) BETWEEN ? AND ?) ";
		return s;
	}

}

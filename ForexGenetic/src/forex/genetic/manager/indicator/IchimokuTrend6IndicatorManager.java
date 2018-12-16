/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Ichimoku;

/**
 *
 * @author ricardorq85
 */
public class IchimokuTrend6IndicatorManager extends IchimokuTrendIndicatorManager {

	public IchimokuTrend6IndicatorManager() {
		super(true, false, "IchiTrend6");
		this.id = "ICHIMOKU_TREND6";
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Ichimoku getIndicatorInstance() {
		return new Ichimoku("IchiTrend6");
	}

	@Override
	public String[] queryRangoOperacionIndicador() {
		String[] s = new String[2];
		s[0] = " MIN(DH.ICHIMOKUSENKOUSPANA6-DH.ICHIMOKUSENKOUSPANB6-OPER.OPEN_PRICE) INF_" + this.id
				+ ",  " + " MAX(DH.ICHIMOKUSENKOUSPANA6-DH.ICHIMOKUSENKOUSPANB6-OPER.OPEN_PRICE) SUP_"
				+ this.id + ",  "
				+ "  ROUND(AVG(DH.ICHIMOKUSENKOUSPANA6-DH.ICHIMOKUSENKOUSPANB6-OPER.OPEN_PRICE), 5) PROM_" + this.id
				+ ", ";
		s[1] = " AND DH.ICHIMOKUSENKOUSPANA6 IS NOT NULL AND DH.ICHIMOKUSENKOUSPANB6 IS NOT NULL ";
		return s;
	}

	@Override
	public String[] queryCumplimientoIndicador() {
		String[] s = new String[1];
		s[0] = " ((DH.ICHIMOKUSENKOUSPANA6-DH.ICHIMOKUSENKOUSPANB6-DH.LOW) BETWEEN ? AND ? "
				+ "  OR (DH.ICHIMOKUSENKOUSPANA6-DH.ICHIMOKUSENKOUSPANB6-DH.HIGH) BETWEEN ? AND ?) ";
		return s;
	}

}

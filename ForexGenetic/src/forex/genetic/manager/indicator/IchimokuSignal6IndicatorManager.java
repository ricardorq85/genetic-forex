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
public class IchimokuSignal6IndicatorManager extends IchimokuSignalIndicatorManager {

	public IchimokuSignal6IndicatorManager() {
		super(false, false, "IchiSignal6");
		this.id = "ICHIMOKU_SIGNAL6";
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Ichimoku getIndicatorInstance() {
		return new Ichimoku("IchiSignal6");
	}

	@Override
	public String[] queryRangoOperacionIndicador() {
		String[] s = new String[2];
		s[0] = "  MIN((DH.ICHIMOKUCHINKOUSPAN6*(DH.ICHIMOKUTENKANSEN6-DH.ICHIMOKUKIJUNSEN6))) INF_"
				+ this.id + ",  "
				+ " MAX((DH.ICHIMOKUCHINKOUSPAN6*(DH.ICHIMOKUTENKANSEN6-DH.ICHIMOKUKIJUNSEN6))) SUP_"
				+ this.id + ",  "
				+ " ROUND(AVG((DH.ICHIMOKUCHINKOUSPAN6*(DH.ICHIMOKUTENKANSEN6-DH.ICHIMOKUKIJUNSEN6))), 5) PROM_"
				+ this.id + ", ";
		s[1] = " AND DH.ICHIMOKUCHINKOUSPAN6 IS NOT NULL " + " AND DH.ICHIMOKUTENKANSEN6 IS NOT NULL "
				+ " AND DH.ICHIMOKUKIJUNSEN6 IS NOT NULL ";
		return s;
	}

	@Override
	public String[] queryPorcentajeCumplimientoIndicador() {
		String[] s = new String[1];
		s[0] = " ((DH.ICHIMOKUCHINKOUSPAN6*(DH.ICHIMOKUTENKANSEN6-DH.ICHIMOKUKIJUNSEN6)) BETWEEN ? AND ?) ";
		return s;
	}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Rsi;

/**
 *
 * @author ricardorq85
 */
public class Rsi84IndicatorManager extends RsiIndicatorManager {

	public Rsi84IndicatorManager() {
		super(false, false, "Rsi84");
		this.id = "RSI84";
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Rsi getIndicatorInstance() {
		return new Rsi("Rsi84");
	}

	@Override
	public String[] queryRangoOperacionIndicador() {
		String[] s = new String[2];
		s[0] = " MIN(DH.RSI84) INF_" + this.id + ",  MAX(DH.RSI84) SUP_" + this.id + ",  "
				+ "  ROUND(AVG(DH.RSI84), 5) PROM_" + this.id + ", ";
		s[1] = " AND DH.RSI84 IS NOT NULL ";
		return s;
	}

	@Override
	public String[] queryPorcentajeCumplimientoIndicador() {
		String[] s = new String[1];
		s[0] = " ((DH.RSI84) BETWEEN ? AND ?) ";
		return s;
	}

}

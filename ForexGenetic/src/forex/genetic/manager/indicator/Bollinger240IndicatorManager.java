/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Bollinger;

/**
 *
 * @author ricardorq85
 */
public class Bollinger240IndicatorManager extends BollingerIndicatorManager {

	public Bollinger240IndicatorManager() {
		super(false, false, "Bollinger240");
		this.id = "BOLLINGER240";
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Bollinger getIndicatorInstance() {
		return new Bollinger("Bollinger240");
	}

	@Override
	public String[] queryRangoOperacionIndicador() {
		String[] s = new String[2];
		s[0] = " MIN(DH.BOLLINGER_UPPER240-DH.BOLLINGER_LOWER240) INF_" + this.id
				+ ",  MAX(DH.BOLLINGER_UPPER240-DH.BOLLINGER_LOWER240) SUP_" + this.id + ",  "
				+ "  ROUND(AVG(DH.BOLLINGER_UPPER240-DH.BOLLINGER_LOWER240), 5) PROM_" + this.id + ", ";
		s[1] = " AND DH.BOLLINGER_UPPER240 IS NOT NULL AND DH.BOLLINGER_LOWER240 IS NOT NULL ";
		return s;
	}

	@Override
	public String[] queryPorcentajeCumplimientoIndicador() {
		String[] s = new String[1];
		s[0] = " ((DH.BOLLINGER_UPPER240-DH.BOLLINGER_LOWER240) BETWEEN ? AND ?) ";
		return s;
	}

}

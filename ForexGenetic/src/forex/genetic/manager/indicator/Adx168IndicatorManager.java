/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Adx;

/**
 *
 * @author ricardorq85
 */
public class Adx168IndicatorManager extends AdxIndicatorManager {

	public Adx168IndicatorManager() {
		super(false, true, "Adx168");
		this.id = "ADX168";
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Adx getIndicatorInstance() {
		return new Adx("Adx168");
	}

	@Override
	public String[] queryRangoOperacionIndicador() {
		String[] s = new String[2];
		s[0] = "  MIN((DH.ADX_VALUE168*(DH.ADX_PLUS168-DH.ADX_MINUS168))) INF_" + this.id
				+ ",  MAX((DH.ADX_VALUE168*(DH.ADX_PLUS168-DH.ADX_MINUS168))) SUP_" + this.id + ",  "
				+ "  ROUND(AVG((DH.ADX_VALUE168*(DH.ADX_PLUS168-DH.ADX_MINUS168))), 5) PROM_" + this.id + ", ";
		s[1] = " AND DH.ADX_VALUE168 IS NOT NULL AND DH.ADX_PLUS168 IS NOT NULL AND DH.ADX_MINUS168 IS NOT NULL ";
		return s;
	}

	@Override
	public String[] queryCumplimientoIndicador() {
		String[] s = new String[1];
		s[0] = " ((DH.ADX_VALUE168*(DH.ADX_PLUS168-DH.ADX_MINUS168)) BETWEEN ? AND ?) ";
		return s;
	}
}

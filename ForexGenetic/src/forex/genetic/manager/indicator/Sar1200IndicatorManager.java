/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.manager.indicator;

import forex.genetic.entities.indicator.Sar;

/**
 *
 * @author ricardorq85
 */
public class Sar1200IndicatorManager extends SarIndicatorManager {

	/**
	 *
	 * @param priceDependence
	 */
	public Sar1200IndicatorManager(boolean priceDependence) {
		super(priceDependence, false, "Sar1200");
		this.id = "SAR1200";
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Sar getIndicatorInstance() {
		return new Sar("Sar1200");
	}

	@Override
	public String[] queryRangoOperacionIndicador() {
		String[] s = new String[2];
		s[0] = " MIN(DH.SAR1200-OPER.OPEN_PRICE) INF_" + this.id
				+ ",  MAX(DH.SAR1200-OPER.OPEN_PRICE) SUP_" + this.id + ",  "
				+ " ROUND(AVG(DH.SAR1200-OPER.OPEN_PRICE), 5) PROM_" + this.id + ", ";
		s[1] = " AND DH.SAR1200 IS NOT NULL ";
		return s;
	}

	@Override
	public String[] queryPorcentajeCumplimientoIndicador() {
		String[] s = new String[1];
		s[0] = " ((DH.SAR1200-DH.LOW) BETWEEN ? AND ? " + "  OR (DH.SAR1200-DH.HIGH) BETWEEN ? AND ? ) ";
		return s;
	}

}

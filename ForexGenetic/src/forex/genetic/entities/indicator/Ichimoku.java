/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.indicator;

import java.util.HashMap;
import java.util.Map;

import forex.genetic.entities.Point;

/**
 *
 * @author ricardorq85
 */
public class Ichimoku extends IntervalIndicator {

	/**
	 *
	 */
	public static final long serialVersionUID = 201102041228L;
	private double tenkanSen = 0.0;
	private double kijunSen = 0.0;
	private double senkouSpanA = 0.0;
	private double senkouSpanB = 0.0;
	private double chinkouSpan = 0.0;

	/**
	 *
	 * @param name
	 */
	public Ichimoku(String name) {
		super(name);
	}

	/**
	 *
	 * @return
	 */
	public double getChinkouSpan() {
		return chinkouSpan;
	}

	/**
	 *
	 * @param chinkouSpan
	 */
	public void setChinkouSpan(double chinkouSpan) {
		this.chinkouSpan = chinkouSpan;
	}

	/**
	 *
	 * @return
	 */
	public double getKijunSen() {
		return kijunSen;
	}

	/**
	 *
	 * @param kijunSen
	 */
	public void setKijunSen(double kijunSen) {
		this.kijunSen = kijunSen;
	}

	/**
	 *
	 * @return
	 */
	public double getSenkouSpanA() {
		return senkouSpanA;
	}

	/**
	 *
	 * @param senkouSpanA
	 */
	public void setSenkouSpanA(double senkouSpanA) {
		this.senkouSpanA = senkouSpanA;
	}

	/**
	 *
	 * @return
	 */
	public double getSenkouSpanB() {
		return senkouSpanB;
	}

	/**
	 *
	 * @param senkouSpanB
	 */
	public void setSenkouSpanB(double senkouSpanB) {
		this.senkouSpanB = senkouSpanB;
	}

	/**
	 *
	 * @return
	 */
	public double getTenkanSen() {
		return tenkanSen;
	}

	/**
	 *
	 * @param tenkanSen
	 */
	public void setTenkanSen(double tenkanSen) {
		this.tenkanSen = tenkanSen;
	}

	@Override
	public Map<String, Object> valuesToMap(Point datoHistorico) {
		Map<String, Object> objectMap = new HashMap<String, Object>();
		if (!Double.isInfinite(this.chinkouSpan) && !Double.isNaN(this.chinkouSpan)) {
			objectMap.put("chinkouSpan", this.chinkouSpan);
		}
		if (!Double.isInfinite(this.kijunSen) && !Double.isNaN(this.kijunSen)) {
			objectMap.put("kijunSen", this.kijunSen);
		}
		if (!Double.isInfinite(this.senkouSpanA) && !Double.isNaN(this.senkouSpanA)) {
			objectMap.put("senkouSpanA", this.senkouSpanA);
		}
		if (!Double.isInfinite(this.senkouSpanB) && !Double.isNaN(this.senkouSpanB)) {
			objectMap.put("senkouSpanB", this.senkouSpanB);
		}
		if (!Double.isInfinite(this.tenkanSen) && !Double.isNaN(this.tenkanSen)) {
			objectMap.put("tenkanSen", this.tenkanSen);
		}
		if (objectMap.size() == 5) {
			objectMap.put("calculado", (this.senkouSpanA - this.senkouSpanB - datoHistorico.getLow()));
			objectMap.put("calculado2", this.chinkouSpan * (this.tenkanSen - this.kijunSen));
		}
		return objectMap;
	}

}

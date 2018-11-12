/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities.indicator;

import java.util.HashMap;
import java.util.Map;

import forex.genetic.entities.Point;
import forex.genetic.util.NumberUtil;

/**
 *
 * @author ricardorq85
 */
public class Sar extends IntervalIndicator {

	/**
	 *
	 */
	public static final long serialVersionUID = 201101251800L;
	private double sar = 0.0;
	private double parameter1 = 0.0;
	private double parameter2 = 0.0;

	/**
	 *
	 * @param name
	 */
	public Sar(String name) {
		super(name);
	}

	/**
	 *
	 * @return
	 */
	public double getParameter1() {
		return parameter1;
	}

	/**
	 *
	 * @param parameter1
	 */
	public void setParameter1(double parameter1) {
		this.parameter1 = parameter1;
	}

	/**
	 *
	 * @return
	 */
	public double getParameter2() {
		return parameter2;
	}

	/**
	 *
	 * @param parameter2
	 */
	public void setParameter2(double parameter2) {
		this.parameter2 = parameter2;
	}

	/**
	 *
	 * @return
	 */
	public double getSar() {
		return sar;
	}

	/**
	 *
	 * @param sar
	 */
	public void setSar(double sar) {
		this.sar = sar;
	}

}

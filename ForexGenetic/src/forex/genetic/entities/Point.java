/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forex.genetic.entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import forex.genetic.entities.indicator.Indicator;

/**
 *
 * @author ricardorq85
 */
public class Point implements Serializable {

	/**
	 *
	 */
	public static final long serialVersionUID = 201203120716L;
	private String moneda = "USDCAD";
	private int periodo = 1;
	private String monedaComparacion = "EURUSD";
	private int index = 0;
	private Date date = null;
	private double open = 0.0;
	private double low = 0.0;
	private double high = 0.0;
	private double close = 0.0;
	private int volume = 0;
	private double closeCompare = 0.0;
	private double spread = 0.0;
	private List<? extends Indicator> indicators = null;

	/**
	 *
	 */
	public Point() {
	}

	/**
	 *
	 * @return
	 */
	public double getSpread() {
		return spread;
	}

	/**
	 *
	 * @param spread
	 */
	public void setSpread(double spread) {
		this.spread = spread;
	}

	/**
	 *
	 * @return
	 */
	public int getIndex() {
		return index;
	}

	/**
	 *
	 * @param index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 *
	 * @return
	 */
	public double getCloseCompare() {
		return closeCompare;
	}

	/**
	 *
	 * @param closeCompare
	 */
	public void setCloseCompare(double closeCompare) {
		this.closeCompare = closeCompare;
	}

	/**
	 *
	 * @return
	 */
	public double getClose() {
		return close;
	}

	/**
	 *
	 * @return
	 */
	public double getWeihgted() {
		return ((this.getClose() + this.getOpen() + this.getHigh() + this.getLow()) / 4);
	}

	/**
	 *
	 * @param close
	 */
	public void setClose(double close) {
		this.close = close;
	}

	/**
	 *
	 * @return
	 */
	public Date getDate() {
		return (this.date = date != null ? new Date(date.getTime()) : null);
	}

	/**
	 *
	 * @param date
	 */
	public void setDate(Date date) {
		this.date = date != null ? new Date(date.getTime()) : null;
	}

	/**
	 *
	 * @return
	 */
	public List<? extends Indicator> getIndicators() {
		return indicators;
	}

	/**
	 *
	 * @param indicators
	 */
	public void setIndicators(List<? extends Indicator> indicators) {
		this.indicators = indicators;
	}

	/**
	 *
	 * @return
	 */
	public double getOpen() {
		return open;
	}

	/**
	 *
	 * @param open
	 */
	public void setOpen(double open) {
		this.open = open;
	}

	/**
	 *
	 * @return
	 */
	public int getVolume() {
		return volume;
	}

	/**
	 *
	 * @param volume
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}

	/**
	 *
	 * @return
	 */
	public double getHigh() {
		return high;
	}

	/**
	 *
	 * @param high
	 */
	public void setHigh(double high) {
		this.high = high;
	}

	/**
	 *
	 * @return
	 */
	public double getLow() {
		return low;
	}

	/**
	 *
	 * @param low
	 */
	public void setLow(double low) {
		this.low = low;
	}

	@Override
	public String toString() {
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		return "Point [moneda=" + moneda + ", periodo=" + periodo + ", monedaComparacion=" + monedaComparacion
				+ ", index=" + index + ", date=" + format.format(this.date) + ", open=" + open + ", low=" + low + ", high=" + high
				+ ", close=" + close + ", volume=" + volume + ", closeCompare=" + closeCompare + ", spread=" + spread;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((moneda == null) ? 0 : moneda.hashCode());
		result = prime * result + periodo;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (moneda == null) {
			if (other.moneda != null)
				return false;
		} else if (!moneda.equals(other.moneda))
			return false;
		if (periodo != other.periodo)
			return false;
		return true;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public int getPeriodo() {
		return periodo;
	}

	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}

	public String getMonedaComparacion() {
		return monedaComparacion;
	}

	public void setMonedaComparacion(String monedaComparacion) {
		this.monedaComparacion = monedaComparacion;
	}

}
